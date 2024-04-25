package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.CuisineTypeAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.CuisineType;
import com.mycompany.myapp.repository.CuisineTypeRepository;
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
 * Integration tests for the {@link CuisineTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CuisineTypeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/cuisine-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CuisineTypeRepository cuisineTypeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCuisineTypeMockMvc;

    private CuisineType cuisineType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CuisineType createEntity(EntityManager em) {
        CuisineType cuisineType = new CuisineType().name(DEFAULT_NAME);
        return cuisineType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CuisineType createUpdatedEntity(EntityManager em) {
        CuisineType cuisineType = new CuisineType().name(UPDATED_NAME);
        return cuisineType;
    }

    @BeforeEach
    public void initTest() {
        cuisineType = createEntity(em);
    }

    @Test
    @Transactional
    void createCuisineType() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CuisineType
        var returnedCuisineType = om.readValue(
            restCuisineTypeMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cuisineType)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CuisineType.class
        );

        // Validate the CuisineType in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertCuisineTypeUpdatableFieldsEquals(returnedCuisineType, getPersistedCuisineType(returnedCuisineType));
    }

    @Test
    @Transactional
    void createCuisineTypeWithExistingId() throws Exception {
        // Create the CuisineType with an existing ID
        cuisineType.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCuisineTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cuisineType)))
            .andExpect(status().isBadRequest());

        // Validate the CuisineType in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCuisineTypes() throws Exception {
        // Initialize the database
        cuisineTypeRepository.saveAndFlush(cuisineType);

        // Get all the cuisineTypeList
        restCuisineTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cuisineType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getCuisineType() throws Exception {
        // Initialize the database
        cuisineTypeRepository.saveAndFlush(cuisineType);

        // Get the cuisineType
        restCuisineTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, cuisineType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cuisineType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingCuisineType() throws Exception {
        // Get the cuisineType
        restCuisineTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCuisineType() throws Exception {
        // Initialize the database
        cuisineTypeRepository.saveAndFlush(cuisineType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cuisineType
        CuisineType updatedCuisineType = cuisineTypeRepository.findById(cuisineType.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCuisineType are not directly saved in db
        em.detach(updatedCuisineType);
        updatedCuisineType.name(UPDATED_NAME);

        restCuisineTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCuisineType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedCuisineType))
            )
            .andExpect(status().isOk());

        // Validate the CuisineType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCuisineTypeToMatchAllProperties(updatedCuisineType);
    }

    @Test
    @Transactional
    void putNonExistingCuisineType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cuisineType.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCuisineTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cuisineType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cuisineType))
            )
            .andExpect(status().isBadRequest());

        // Validate the CuisineType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCuisineType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cuisineType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCuisineTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cuisineType))
            )
            .andExpect(status().isBadRequest());

        // Validate the CuisineType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCuisineType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cuisineType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCuisineTypeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cuisineType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CuisineType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCuisineTypeWithPatch() throws Exception {
        // Initialize the database
        cuisineTypeRepository.saveAndFlush(cuisineType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cuisineType using partial update
        CuisineType partialUpdatedCuisineType = new CuisineType();
        partialUpdatedCuisineType.setId(cuisineType.getId());

        partialUpdatedCuisineType.name(UPDATED_NAME);

        restCuisineTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCuisineType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCuisineType))
            )
            .andExpect(status().isOk());

        // Validate the CuisineType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCuisineTypeUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCuisineType, cuisineType),
            getPersistedCuisineType(cuisineType)
        );
    }

    @Test
    @Transactional
    void fullUpdateCuisineTypeWithPatch() throws Exception {
        // Initialize the database
        cuisineTypeRepository.saveAndFlush(cuisineType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cuisineType using partial update
        CuisineType partialUpdatedCuisineType = new CuisineType();
        partialUpdatedCuisineType.setId(cuisineType.getId());

        partialUpdatedCuisineType.name(UPDATED_NAME);

        restCuisineTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCuisineType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCuisineType))
            )
            .andExpect(status().isOk());

        // Validate the CuisineType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCuisineTypeUpdatableFieldsEquals(partialUpdatedCuisineType, getPersistedCuisineType(partialUpdatedCuisineType));
    }

    @Test
    @Transactional
    void patchNonExistingCuisineType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cuisineType.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCuisineTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cuisineType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cuisineType))
            )
            .andExpect(status().isBadRequest());

        // Validate the CuisineType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCuisineType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cuisineType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCuisineTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cuisineType))
            )
            .andExpect(status().isBadRequest());

        // Validate the CuisineType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCuisineType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cuisineType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCuisineTypeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(cuisineType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CuisineType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCuisineType() throws Exception {
        // Initialize the database
        cuisineTypeRepository.saveAndFlush(cuisineType);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the cuisineType
        restCuisineTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, cuisineType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return cuisineTypeRepository.count();
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

    protected CuisineType getPersistedCuisineType(CuisineType cuisineType) {
        return cuisineTypeRepository.findById(cuisineType.getId()).orElseThrow();
    }

    protected void assertPersistedCuisineTypeToMatchAllProperties(CuisineType expectedCuisineType) {
        assertCuisineTypeAllPropertiesEquals(expectedCuisineType, getPersistedCuisineType(expectedCuisineType));
    }

    protected void assertPersistedCuisineTypeToMatchUpdatableProperties(CuisineType expectedCuisineType) {
        assertCuisineTypeAllUpdatablePropertiesEquals(expectedCuisineType, getPersistedCuisineType(expectedCuisineType));
    }
}
