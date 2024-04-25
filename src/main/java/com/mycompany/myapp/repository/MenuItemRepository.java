package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.MenuItem;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the MenuItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {}
