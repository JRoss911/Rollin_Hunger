package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.MenuItemAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.MenuItem;
import com.mycompany.myapp.repository.MenuItemRepository;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link MenuItemResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MenuItemResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Float DEFAULT_PRICE = 1F;
    private static final Float UPDATED_PRICE = 2F;

    private static final String ENTITY_API_URL = "/api/menu-items";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MenuItemRepository menuItemRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMenuItemMockMvc;

    private MenuItem menuItem;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MenuItem createEntity(EntityManager em) {
        MenuItem menuItem = new MenuItem().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION).price(DEFAULT_PRICE);
        return menuItem;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MenuItem createUpdatedEntity(EntityManager em) {
        MenuItem menuItem = new MenuItem().name(UPDATED_NAME).description(UPDATED_DESCRIPTION).price(UPDATED_PRICE);
        return menuItem;
    }

    @BeforeEach
    public void initTest() {
        menuItem = createEntity(em);
    }

    @Test
    @Transactional
    void createMenuItem() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the MenuItem
        var returnedMenuItem = om.readValue(
            restMenuItemMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(menuItem)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            MenuItem.class
        );

        // Validate the MenuItem in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertMenuItemUpdatableFieldsEquals(returnedMenuItem, getPersistedMenuItem(returnedMenuItem));
    }

    @Test
    @Transactional
    void createMenuItemWithExistingId() throws Exception {
        // Create the MenuItem with an existing ID
        menuItem.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMenuItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(menuItem)))
            .andExpect(status().isBadRequest());

        // Validate the MenuItem in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllMenuItems() throws Exception {
        // Initialize the database
        menuItemRepository.saveAndFlush(menuItem);

        // Get all the menuItemList
        restMenuItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(menuItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())));
    }

    @Test
    @Transactional
    void getMenuItem() throws Exception {
        // Initialize the database
        menuItemRepository.saveAndFlush(menuItem);

        // Get the menuItem
        restMenuItemMockMvc
            .perform(get(ENTITY_API_URL_ID, menuItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(menuItem.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingMenuItem() throws Exception {
        // Get the menuItem
        restMenuItemMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMenuItem() throws Exception {
        // Initialize the database
        menuItemRepository.saveAndFlush(menuItem);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the menuItem
        MenuItem updatedMenuItem = menuItemRepository.findById(menuItem.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMenuItem are not directly saved in db
        em.detach(updatedMenuItem);
        updatedMenuItem.name(UPDATED_NAME).description(UPDATED_DESCRIPTION).price(UPDATED_PRICE);

        restMenuItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedMenuItem.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedMenuItem))
            )
            .andExpect(status().isOk());

        // Validate the MenuItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedMenuItemToMatchAllProperties(updatedMenuItem);
    }

    @Test
    @Transactional
    void putNonExistingMenuItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        menuItem.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMenuItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, menuItem.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(menuItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the MenuItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMenuItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        menuItem.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMenuItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(menuItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the MenuItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMenuItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        menuItem.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMenuItemMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(menuItem)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MenuItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMenuItemWithPatch() throws Exception {
        // Initialize the database
        menuItemRepository.saveAndFlush(menuItem);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the menuItem using partial update
        MenuItem partialUpdatedMenuItem = new MenuItem();
        partialUpdatedMenuItem.setId(menuItem.getId());

        partialUpdatedMenuItem.description(UPDATED_DESCRIPTION).price(UPDATED_PRICE);

        restMenuItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMenuItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMenuItem))
            )
            .andExpect(status().isOk());

        // Validate the MenuItem in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMenuItemUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedMenuItem, menuItem), getPersistedMenuItem(menuItem));
    }

    @Test
    @Transactional
    void fullUpdateMenuItemWithPatch() throws Exception {
        // Initialize the database
        menuItemRepository.saveAndFlush(menuItem);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the menuItem using partial update
        MenuItem partialUpdatedMenuItem = new MenuItem();
        partialUpdatedMenuItem.setId(menuItem.getId());

        partialUpdatedMenuItem.name(UPDATED_NAME).description(UPDATED_DESCRIPTION).price(UPDATED_PRICE);

        restMenuItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMenuItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMenuItem))
            )
            .andExpect(status().isOk());

        // Validate the MenuItem in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMenuItemUpdatableFieldsEquals(partialUpdatedMenuItem, getPersistedMenuItem(partialUpdatedMenuItem));
    }

    @Test
    @Transactional
    void patchNonExistingMenuItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        menuItem.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMenuItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, menuItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(menuItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the MenuItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMenuItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        menuItem.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMenuItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(menuItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the MenuItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMenuItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        menuItem.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMenuItemMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(menuItem)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MenuItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMenuItem() throws Exception {
        // Initialize the database
        menuItemRepository.saveAndFlush(menuItem);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the menuItem
        restMenuItemMockMvc
            .perform(delete(ENTITY_API_URL_ID, menuItem.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return menuItemRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected MenuItem getPersistedMenuItem(MenuItem menuItem) {
        return menuItemRepository.findById(menuItem.getId()).orElseThrow();
    }

    protected void assertPersistedMenuItemToMatchAllProperties(MenuItem expectedMenuItem) {
        assertMenuItemAllPropertiesEquals(expectedMenuItem, getPersistedMenuItem(expectedMenuItem));
    }

    protected void assertPersistedMenuItemToMatchUpdatableProperties(MenuItem expectedMenuItem) {
        assertMenuItemAllUpdatablePropertiesEquals(expectedMenuItem, getPersistedMenuItem(expectedMenuItem));
    }
}
