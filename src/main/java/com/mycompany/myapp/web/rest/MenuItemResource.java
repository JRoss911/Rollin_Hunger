package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.MenuItem;
import com.mycompany.myapp.repository.MenuItemRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.MenuItem}.
 */
@RestController
@RequestMapping("/api/menu-items")
@Transactional
public class MenuItemResource {

    private final Logger log = LoggerFactory.getLogger(MenuItemResource.class);

    private static final String ENTITY_NAME = "menuItem";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MenuItemRepository menuItemRepository;

    public MenuItemResource(MenuItemRepository menuItemRepository) {
        this.menuItemRepository = menuItemRepository;
    }

    /**
     * {@code POST  /menu-items} : Create a new menuItem.
     *
     * @param menuItem the menuItem to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new menuItem, or with status {@code 400 (Bad Request)} if the menuItem has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<MenuItem> createMenuItem(@RequestBody MenuItem menuItem) throws URISyntaxException {
        log.debug("REST request to save MenuItem : {}", menuItem);
        if (menuItem.getId() != null) {
            throw new BadRequestAlertException("A new menuItem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        menuItem = menuItemRepository.save(menuItem);
        return ResponseEntity.created(new URI("/api/menu-items/" + menuItem.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, menuItem.getId().toString()))
            .body(menuItem);
    }

    /**
     * {@code PUT  /menu-items/:id} : Updates an existing menuItem.
     *
     * @param id the id of the menuItem to save.
     * @param menuItem the menuItem to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated menuItem,
     * or with status {@code 400 (Bad Request)} if the menuItem is not valid,
     * or with status {@code 500 (Internal Server Error)} if the menuItem couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<MenuItem> updateMenuItem(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody MenuItem menuItem
    ) throws URISyntaxException {
        log.debug("REST request to update MenuItem : {}, {}", id, menuItem);
        if (menuItem.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, menuItem.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!menuItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        menuItem = menuItemRepository.save(menuItem);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, menuItem.getId().toString()))
            .body(menuItem);
    }

    /**
     * {@code PATCH  /menu-items/:id} : Partial updates given fields of an existing menuItem, field will ignore if it is null
     *
     * @param id the id of the menuItem to save.
     * @param menuItem the menuItem to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated menuItem,
     * or with status {@code 400 (Bad Request)} if the menuItem is not valid,
     * or with status {@code 404 (Not Found)} if the menuItem is not found,
     * or with status {@code 500 (Internal Server Error)} if the menuItem couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MenuItem> partialUpdateMenuItem(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody MenuItem menuItem
    ) throws URISyntaxException {
        log.debug("REST request to partial update MenuItem partially : {}, {}", id, menuItem);
        if (menuItem.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, menuItem.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!menuItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MenuItem> result = menuItemRepository
            .findById(menuItem.getId())
            .map(existingMenuItem -> {
                if (menuItem.getName() != null) {
                    existingMenuItem.setName(menuItem.getName());
                }
                if (menuItem.getDescription() != null) {
                    existingMenuItem.setDescription(menuItem.getDescription());
                }
                if (menuItem.getPrice() != null) {
                    existingMenuItem.setPrice(menuItem.getPrice());
                }

                return existingMenuItem;
            })
            .map(menuItemRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, menuItem.getId().toString())
        );
    }

    /**
     * {@code GET  /menu-items} : get all the menuItems.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of menuItems in body.
     */
    @GetMapping("")
    public List<MenuItem> getAllMenuItems() {
        log.debug("REST request to get all MenuItems");
        return menuItemRepository.findAll();
    }

    /**
     * {@code GET  /menu-items/:id} : get the "id" menuItem.
     *
     * @param id the id of the menuItem to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the menuItem, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<MenuItem> getMenuItem(@PathVariable("id") Long id) {
        log.debug("REST request to get MenuItem : {}", id);
        Optional<MenuItem> menuItem = menuItemRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(menuItem);
    }

    /**
     * {@code DELETE  /menu-items/:id} : delete the "id" menuItem.
     *
     * @param id the id of the menuItem to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMenuItem(@PathVariable("id") Long id) {
        log.debug("REST request to delete MenuItem : {}", id);
        menuItemRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
