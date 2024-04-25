package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.CuisineType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CuisineType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CuisineTypeRepository extends JpaRepository<CuisineType, Long> {}
