package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.CuisineType;
import com.mycompany.myapp.repository.CuisineTypeRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.CuisineType}.
 */
@RestController
@RequestMapping("/api/cuisine-types")
@Transactional
public class CuisineTypeResource {

    private final Logger log = LoggerFactory.getLogger(CuisineTypeResource.class);

    private static final String ENTITY_NAME = "cuisineType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CuisineTypeRepository cuisineTypeRepository;

    public CuisineTypeResource(CuisineTypeRepository cuisineTypeRepository) {
        this.cuisineTypeRepository = cuisineTypeRepository;
    }

    /**
     * {@code POST  /cuisine-types} : Create a new cuisineType.
     *
     * @param cuisineType the cuisineType to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cuisineType, or with status {@code 400 (Bad Request)} if the cuisineType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CuisineType> createCuisineType(@RequestBody CuisineType cuisineType) throws URISyntaxException {
        log.debug("REST request to save CuisineType : {}", cuisineType);
        if (cuisineType.getId() != null) {
            throw new BadRequestAlertException("A new cuisineType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        cuisineType = cuisineTypeRepository.save(cuisineType);
        return ResponseEntity.created(new URI("/api/cuisine-types/" + cuisineType.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, cuisineType.getId().toString()))
            .body(cuisineType);
    }

    /**
     * {@code PUT  /cuisine-types/:id} : Updates an existing cuisineType.
     *
     * @param id the id of the cuisineType to save.
     * @param cuisineType the cuisineType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cuisineType,
     * or with status {@code 400 (Bad Request)} if the cuisineType is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cuisineType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CuisineType> updateCuisineType(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CuisineType cuisineType
    ) throws URISyntaxException {
        log.debug("REST request to update CuisineType : {}, {}", id, cuisineType);
        if (cuisineType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cuisineType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cuisineTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        cuisineType = cuisineTypeRepository.save(cuisineType);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cuisineType.getId().toString()))
            .body(cuisineType);
    }

    /**
     * {@code PATCH  /cuisine-types/:id} : Partial updates given fields of an existing cuisineType, field will ignore if it is null
     *
     * @param id the id of the cuisineType to save.
     * @param cuisineType the cuisineType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cuisineType,
     * or with status {@code 400 (Bad Request)} if the cuisineType is not valid,
     * or with status {@code 404 (Not Found)} if the cuisineType is not found,
     * or with status {@code 500 (Internal Server Error)} if the cuisineType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CuisineType> partialUpdateCuisineType(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CuisineType cuisineType
    ) throws URISyntaxException {
        log.debug("REST request to partial update CuisineType partially : {}, {}", id, cuisineType);
        if (cuisineType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cuisineType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cuisineTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CuisineType> result = cuisineTypeRepository
            .findById(cuisineType.getId())
            .map(existingCuisineType -> {
                if (cuisineType.getName() != null) {
                    existingCuisineType.setName(cuisineType.getName());
                }

                return existingCuisineType;
            })
            .map(cuisineTypeRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cuisineType.getId().toString())
        );
    }

    /**
     * {@code GET  /cuisine-types} : get all the cuisineTypes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cuisineTypes in body.
     */
    @GetMapping("")
    public List<CuisineType> getAllCuisineTypes() {
        log.debug("REST request to get all CuisineTypes");
        return cuisineTypeRepository.findAll();
    }

    /**
     * {@code GET  /cuisine-types/:id} : get the "id" cuisineType.
     *
     * @param id the id of the cuisineType to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cuisineType, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CuisineType> getCuisineType(@PathVariable("id") Long id) {
        log.debug("REST request to get CuisineType : {}", id);
        Optional<CuisineType> cuisineType = cuisineTypeRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(cuisineType);
    }

    /**
     * {@code DELETE  /cuisine-types/:id} : delete the "id" cuisineType.
     *
     * @param id the id of the cuisineType to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCuisineType(@PathVariable("id") Long id) {
        log.debug("REST request to delete CuisineType : {}", id);
        cuisineTypeRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
