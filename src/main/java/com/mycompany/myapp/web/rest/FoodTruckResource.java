package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.FoodTruck;
import com.mycompany.myapp.repository.FoodTruckRepository;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.FoodTruck}.
 */
@RestController
@RequestMapping("/api/food-trucks")
@Transactional
public class FoodTruckResource {

    private final Logger log = LoggerFactory.getLogger(FoodTruckResource.class);

    private static final String ENTITY_NAME = "foodTruck";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FoodTruckRepository foodTruckRepository;

    public FoodTruckResource(FoodTruckRepository foodTruckRepository) {
        this.foodTruckRepository = foodTruckRepository;
    }

    /**
     * {@code POST  /food-trucks} : Create a new foodTruck.
     *
     * @param foodTruck the foodTruck to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new foodTruck, or with status {@code 400 (Bad Request)} if the foodTruck has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<FoodTruck> createFoodTruck(@RequestBody FoodTruck foodTruck) throws URISyntaxException {
        log.debug("REST request to save FoodTruck : {}", foodTruck);
        if (foodTruck.getId() != null) {
            throw new BadRequestAlertException("A new foodTruck cannot already have an ID", ENTITY_NAME, "idexists");
        }
        foodTruck = foodTruckRepository.save(foodTruck);
        return ResponseEntity.created(new URI("/api/food-trucks/" + foodTruck.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, foodTruck.getId().toString()))
            .body(foodTruck);
    }

    /**
     * {@code PUT  /food-trucks/:id} : Updates an existing foodTruck.
     *
     * @param id the id of the foodTruck to save.
     * @param foodTruck the foodTruck to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated foodTruck,
     * or with status {@code 400 (Bad Request)} if the foodTruck is not valid,
     * or with status {@code 500 (Internal Server Error)} if the foodTruck couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<FoodTruck> updateFoodTruck(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FoodTruck foodTruck
    ) throws URISyntaxException {
        log.debug("REST request to update FoodTruck : {}, {}", id, foodTruck);
        if (foodTruck.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, foodTruck.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!foodTruckRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        foodTruck = foodTruckRepository.save(foodTruck);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, foodTruck.getId().toString()))
            .body(foodTruck);
    }

    /**
     * {@code PATCH  /food-trucks/:id} : Partial updates given fields of an existing foodTruck, field will ignore if it is null
     *
     * @param id the id of the foodTruck to save.
     * @param foodTruck the foodTruck to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated foodTruck,
     * or with status {@code 400 (Bad Request)} if the foodTruck is not valid,
     * or with status {@code 404 (Not Found)} if the foodTruck is not found,
     * or with status {@code 500 (Internal Server Error)} if the foodTruck couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FoodTruck> partialUpdateFoodTruck(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FoodTruck foodTruck
    ) throws URISyntaxException {
        log.debug("REST request to partial update FoodTruck partially : {}, {}", id, foodTruck);
        if (foodTruck.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, foodTruck.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!foodTruckRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FoodTruck> result = foodTruckRepository
            .findById(foodTruck.getId())
            .map(existingFoodTruck -> {
                if (foodTruck.getName() != null) {
                    existingFoodTruck.setName(foodTruck.getName());
                }
                if (foodTruck.getDescription() != null) {
                    existingFoodTruck.setDescription(foodTruck.getDescription());
                }
                if (foodTruck.getRating() != null) {
                    existingFoodTruck.setRating(foodTruck.getRating());
                }
                if (foodTruck.getProfilePicture() != null) {
                    existingFoodTruck.setProfilePicture(foodTruck.getProfilePicture());
                }

                return existingFoodTruck;
            })
            .map(foodTruckRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, foodTruck.getId().toString())
        );
    }

    /**
     * {@code GET  /food-trucks} : get all the foodTrucks.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of foodTrucks in body.
     */
    @GetMapping("")
    public List<FoodTruck> getAllFoodTrucks() {
        log.debug("REST request to get all FoodTrucks");
        return foodTruckRepository.findAll();
    }

    /**
     * {@code GET  /food-trucks/:id} : get the "id" foodTruck.
     *
     * @param id the id of the foodTruck to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the foodTruck, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<FoodTruck> getFoodTruck(@PathVariable("id") Long id) {
        log.debug("REST request to get FoodTruck : {}", id);
        Optional<FoodTruck> foodTruck = foodTruckRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(foodTruck);
    }

    /**
     * {@code DELETE  /food-trucks/:id} : delete the "id" foodTruck.
     *
     * @param id the id of the foodTruck to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFoodTruck(@PathVariable("id") Long id) {
        log.debug("REST request to delete FoodTruck : {}", id);
        foodTruckRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
