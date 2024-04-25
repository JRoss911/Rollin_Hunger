package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.FoodTruckAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.FoodTruck;
import com.mycompany.myapp.repository.FoodTruckRepository;
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
 * Integration tests for the {@link FoodTruckResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FoodTruckResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Float DEFAULT_RATING = 1F;
    private static final Float UPDATED_RATING = 2F;

    private static final String DEFAULT_PROFILE_PICTURE = "AAAAAAAAAA";
    private static final String UPDATED_PROFILE_PICTURE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/food-trucks";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private FoodTruckRepository foodTruckRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFoodTruckMockMvc;

    private FoodTruck foodTruck;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FoodTruck createEntity(EntityManager em) {
        FoodTruck foodTruck = new FoodTruck()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .rating(DEFAULT_RATING)
            .profilePicture(DEFAULT_PROFILE_PICTURE);
        return foodTruck;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FoodTruck createUpdatedEntity(EntityManager em) {
        FoodTruck foodTruck = new FoodTruck()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .rating(UPDATED_RATING)
            .profilePicture(UPDATED_PROFILE_PICTURE);
        return foodTruck;
    }

    @BeforeEach
    public void initTest() {
        foodTruck = createEntity(em);
    }

    @Test
    @Transactional
    void createFoodTruck() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the FoodTruck
        var returnedFoodTruck = om.readValue(
            restFoodTruckMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(foodTruck)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            FoodTruck.class
        );

        // Validate the FoodTruck in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertFoodTruckUpdatableFieldsEquals(returnedFoodTruck, getPersistedFoodTruck(returnedFoodTruck));
    }

    @Test
    @Transactional
    void createFoodTruckWithExistingId() throws Exception {
        // Create the FoodTruck with an existing ID
        foodTruck.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFoodTruckMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(foodTruck)))
            .andExpect(status().isBadRequest());

        // Validate the FoodTruck in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllFoodTrucks() throws Exception {
        // Initialize the database
        foodTruckRepository.saveAndFlush(foodTruck);

        // Get all the foodTruckList
        restFoodTruckMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(foodTruck.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].rating").value(hasItem(DEFAULT_RATING.doubleValue())))
            .andExpect(jsonPath("$.[*].profilePicture").value(hasItem(DEFAULT_PROFILE_PICTURE)));
    }

    @Test
    @Transactional
    void getFoodTruck() throws Exception {
        // Initialize the database
        foodTruckRepository.saveAndFlush(foodTruck);

        // Get the foodTruck
        restFoodTruckMockMvc
            .perform(get(ENTITY_API_URL_ID, foodTruck.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(foodTruck.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.rating").value(DEFAULT_RATING.doubleValue()))
            .andExpect(jsonPath("$.profilePicture").value(DEFAULT_PROFILE_PICTURE));
    }

    @Test
    @Transactional
    void getNonExistingFoodTruck() throws Exception {
        // Get the foodTruck
        restFoodTruckMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFoodTruck() throws Exception {
        // Initialize the database
        foodTruckRepository.saveAndFlush(foodTruck);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the foodTruck
        FoodTruck updatedFoodTruck = foodTruckRepository.findById(foodTruck.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedFoodTruck are not directly saved in db
        em.detach(updatedFoodTruck);
        updatedFoodTruck.name(UPDATED_NAME).description(UPDATED_DESCRIPTION).rating(UPDATED_RATING).profilePicture(UPDATED_PROFILE_PICTURE);

        restFoodTruckMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedFoodTruck.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedFoodTruck))
            )
            .andExpect(status().isOk());

        // Validate the FoodTruck in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedFoodTruckToMatchAllProperties(updatedFoodTruck);
    }

    @Test
    @Transactional
    void putNonExistingFoodTruck() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        foodTruck.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFoodTruckMockMvc
            .perform(
                put(ENTITY_API_URL_ID, foodTruck.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(foodTruck))
            )
            .andExpect(status().isBadRequest());

        // Validate the FoodTruck in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFoodTruck() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        foodTruck.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFoodTruckMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(foodTruck))
            )
            .andExpect(status().isBadRequest());

        // Validate the FoodTruck in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFoodTruck() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        foodTruck.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFoodTruckMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(foodTruck)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FoodTruck in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFoodTruckWithPatch() throws Exception {
        // Initialize the database
        foodTruckRepository.saveAndFlush(foodTruck);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the foodTruck using partial update
        FoodTruck partialUpdatedFoodTruck = new FoodTruck();
        partialUpdatedFoodTruck.setId(foodTruck.getId());

        partialUpdatedFoodTruck.name(UPDATED_NAME).description(UPDATED_DESCRIPTION).profilePicture(UPDATED_PROFILE_PICTURE);

        restFoodTruckMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFoodTruck.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFoodTruck))
            )
            .andExpect(status().isOk());

        // Validate the FoodTruck in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFoodTruckUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedFoodTruck, foodTruck),
            getPersistedFoodTruck(foodTruck)
        );
    }

    @Test
    @Transactional
    void fullUpdateFoodTruckWithPatch() throws Exception {
        // Initialize the database
        foodTruckRepository.saveAndFlush(foodTruck);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the foodTruck using partial update
        FoodTruck partialUpdatedFoodTruck = new FoodTruck();
        partialUpdatedFoodTruck.setId(foodTruck.getId());

        partialUpdatedFoodTruck
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .rating(UPDATED_RATING)
            .profilePicture(UPDATED_PROFILE_PICTURE);

        restFoodTruckMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFoodTruck.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFoodTruck))
            )
            .andExpect(status().isOk());

        // Validate the FoodTruck in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFoodTruckUpdatableFieldsEquals(partialUpdatedFoodTruck, getPersistedFoodTruck(partialUpdatedFoodTruck));
    }

    @Test
    @Transactional
    void patchNonExistingFoodTruck() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        foodTruck.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFoodTruckMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, foodTruck.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(foodTruck))
            )
            .andExpect(status().isBadRequest());

        // Validate the FoodTruck in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFoodTruck() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        foodTruck.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFoodTruckMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(foodTruck))
            )
            .andExpect(status().isBadRequest());

        // Validate the FoodTruck in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFoodTruck() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        foodTruck.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFoodTruckMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(foodTruck)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FoodTruck in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFoodTruck() throws Exception {
        // Initialize the database
        foodTruckRepository.saveAndFlush(foodTruck);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the foodTruck
        restFoodTruckMockMvc
            .perform(delete(ENTITY_API_URL_ID, foodTruck.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return foodTruckRepository.count();
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

    protected FoodTruck getPersistedFoodTruck(FoodTruck foodTruck) {
        return foodTruckRepository.findById(foodTruck.getId()).orElseThrow();
    }

    protected void assertPersistedFoodTruckToMatchAllProperties(FoodTruck expectedFoodTruck) {
        assertFoodTruckAllPropertiesEquals(expectedFoodTruck, getPersistedFoodTruck(expectedFoodTruck));
    }

    protected void assertPersistedFoodTruckToMatchUpdatableProperties(FoodTruck expectedFoodTruck) {
        assertFoodTruckAllUpdatablePropertiesEquals(expectedFoodTruck, getPersistedFoodTruck(expectedFoodTruck));
    }
}
