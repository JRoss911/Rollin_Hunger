package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.FoodTruck;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the FoodTruck entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FoodTruckRepository extends JpaRepository<FoodTruck, Long> {}
