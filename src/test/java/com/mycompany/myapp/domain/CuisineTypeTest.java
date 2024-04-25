package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.CuisineTypeTestSamples.*;
import static com.mycompany.myapp.domain.FoodTruckTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class CuisineTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CuisineType.class);
        CuisineType cuisineType1 = getCuisineTypeSample1();
        CuisineType cuisineType2 = new CuisineType();
        assertThat(cuisineType1).isNotEqualTo(cuisineType2);

        cuisineType2.setId(cuisineType1.getId());
        assertThat(cuisineType1).isEqualTo(cuisineType2);

        cuisineType2 = getCuisineTypeSample2();
        assertThat(cuisineType1).isNotEqualTo(cuisineType2);
    }

    @Test
    void trucksTest() throws Exception {
        CuisineType cuisineType = getCuisineTypeRandomSampleGenerator();
        FoodTruck foodTruckBack = getFoodTruckRandomSampleGenerator();

        cuisineType.addTrucks(foodTruckBack);
        assertThat(cuisineType.getTrucks()).containsOnly(foodTruckBack);
        assertThat(foodTruckBack.getCuisineType()).isEqualTo(cuisineType);

        cuisineType.removeTrucks(foodTruckBack);
        assertThat(cuisineType.getTrucks()).doesNotContain(foodTruckBack);
        assertThat(foodTruckBack.getCuisineType()).isNull();

        cuisineType.trucks(new HashSet<>(Set.of(foodTruckBack)));
        assertThat(cuisineType.getTrucks()).containsOnly(foodTruckBack);
        assertThat(foodTruckBack.getCuisineType()).isEqualTo(cuisineType);

        cuisineType.setTrucks(new HashSet<>());
        assertThat(cuisineType.getTrucks()).doesNotContain(foodTruckBack);
        assertThat(foodTruckBack.getCuisineType()).isNull();
    }
}
