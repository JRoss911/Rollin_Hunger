package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.LocationAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Location;
import com.mycompany.myapp.repository.LocationRepository;
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
 * Integration tests for the {@link LocationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LocationResourceIT {

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final Float DEFAULT_LATITUDE = 1F;
    private static final Float UPDATED_LATITUDE = 2F;

    private static final Float DEFAULT_LONGITUDE = 1F;
    private static final Float UPDATED_LONGITUDE = 2F;

    private static final String ENTITY_API_URL = "/api/locations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLocationMockMvc;

    private Location location;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Location createEntity(EntityManager em) {
        Location location = new Location().address(DEFAULT_ADDRESS).latitude(DEFAULT_LATITUDE).longitude(DEFAULT_LONGITUDE);
        return location;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Location createUpdatedEntity(EntityManager em) {
        Location location = new Location().address(UPDATED_ADDRESS).latitude(UPDATED_LATITUDE).longitude(UPDATED_LONGITUDE);
        return location;
    }

    @BeforeEach
    public void initTest() {
        location = createEntity(em);
    }

    @Test
    @Transactional
    void createLocation() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Location
        var returnedLocation = om.readValue(
            restLocationMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(location)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Location.class
        );

        // Validate the Location in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertLocationUpdatableFieldsEquals(returnedLocation, getPersistedLocation(returnedLocation));
    }

    @Test
    @Transactional
    void createLocationWithExistingId() throws Exception {
        // Create the Location with an existing ID
        location.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLocationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(location)))
            .andExpect(status().isBadRequest());

        // Validate the Location in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllLocations() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList
        restLocationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(location.getId().intValue())))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].latitude").value(hasItem(DEFAULT_LATITUDE.doubleValue())))
            .andExpect(jsonPath("$.[*].longitude").value(hasItem(DEFAULT_LONGITUDE.doubleValue())));
    }

    @Test
    @Transactional
    void getLocation() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get the location
        restLocationMockMvc
            .perform(get(ENTITY_API_URL_ID, location.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(location.getId().intValue()))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.latitude").value(DEFAULT_LATITUDE.doubleValue()))
            .andExpect(jsonPath("$.longitude").value(DEFAULT_LONGITUDE.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingLocation() throws Exception {
        // Get the location
        restLocationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingLocation() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the location
        Location updatedLocation = locationRepository.findById(location.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedLocation are not directly saved in db
        em.detach(updatedLocation);
        updatedLocation.address(UPDATED_ADDRESS).latitude(UPDATED_LATITUDE).longitude(UPDATED_LONGITUDE);

        restLocationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedLocation.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedLocation))
            )
            .andExpect(status().isOk());

        // Validate the Location in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedLocationToMatchAllProperties(updatedLocation);
    }

    @Test
    @Transactional
    void putNonExistingLocation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        location.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLocationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, location.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(location))
            )
            .andExpect(status().isBadRequest());

        // Validate the Location in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLocation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        location.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLocationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(location))
            )
            .andExpect(status().isBadRequest());

        // Validate the Location in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLocation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        location.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLocationMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(location)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Location in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLocationWithPatch() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the location using partial update
        Location partialUpdatedLocation = new Location();
        partialUpdatedLocation.setId(location.getId());

        partialUpdatedLocation.address(UPDATED_ADDRESS).latitude(UPDATED_LATITUDE);

        restLocationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLocation.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedLocation))
            )
            .andExpect(status().isOk());

        // Validate the Location in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertLocationUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedLocation, location), getPersistedLocation(location));
    }

    @Test
    @Transactional
    void fullUpdateLocationWithPatch() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the location using partial update
        Location partialUpdatedLocation = new Location();
        partialUpdatedLocation.setId(location.getId());

        partialUpdatedLocation.address(UPDATED_ADDRESS).latitude(UPDATED_LATITUDE).longitude(UPDATED_LONGITUDE);

        restLocationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLocation.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedLocation))
            )
            .andExpect(status().isOk());

        // Validate the Location in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertLocationUpdatableFieldsEquals(partialUpdatedLocation, getPersistedLocation(partialUpdatedLocation));
    }

    @Test
    @Transactional
    void patchNonExistingLocation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        location.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLocationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, location.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(location))
            )
            .andExpect(status().isBadRequest());

        // Validate the Location in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLocation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        location.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLocationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(location))
            )
            .andExpect(status().isBadRequest());

        // Validate the Location in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLocation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        location.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLocationMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(location)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Location in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLocation() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the location
        restLocationMockMvc
            .perform(delete(ENTITY_API_URL_ID, location.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return locationRepository.count();
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

    protected Location getPersistedLocation(Location location) {
        return locationRepository.findById(location.getId()).orElseThrow();
    }

    protected void assertPersistedLocationToMatchAllProperties(Location expectedLocation) {
        assertLocationAllPropertiesEquals(expectedLocation, getPersistedLocation(expectedLocation));
    }

    protected void assertPersistedLocationToMatchUpdatableProperties(Location expectedLocation) {
        assertLocationAllUpdatablePropertiesEquals(expectedLocation, getPersistedLocation(expectedLocation));
    }
}
