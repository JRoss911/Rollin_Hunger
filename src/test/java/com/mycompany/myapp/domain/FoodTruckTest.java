package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.CuisineTypeTestSamples.*;
import static com.mycompany.myapp.domain.FoodTruckTestSamples.*;
import static com.mycompany.myapp.domain.LocationTestSamples.*;
import static com.mycompany.myapp.domain.MenuItemTestSamples.*;
import static com.mycompany.myapp.domain.OrderTestSamples.*;
import static com.mycompany.myapp.domain.UserProfileTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class FoodTruckTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FoodTruck.class);
        FoodTruck foodTruck1 = getFoodTruckSample1();
        FoodTruck foodTruck2 = new FoodTruck();
        assertThat(foodTruck1).isNotEqualTo(foodTruck2);

        foodTruck2.setId(foodTruck1.getId());
        assertThat(foodTruck1).isEqualTo(foodTruck2);

        foodTruck2 = getFoodTruckSample2();
        assertThat(foodTruck1).isNotEqualTo(foodTruck2);
    }

    @Test
    void ownerTest() throws Exception {
        FoodTruck foodTruck = getFoodTruckRandomSampleGenerator();
        UserProfile userProfileBack = getUserProfileRandomSampleGenerator();

        foodTruck.setOwner(userProfileBack);
        assertThat(foodTruck.getOwner()).isEqualTo(userProfileBack);

        foodTruck.owner(null);
        assertThat(foodTruck.getOwner()).isNull();
    }

    @Test
    void menuItemsTest() throws Exception {
        FoodTruck foodTruck = getFoodTruckRandomSampleGenerator();
        MenuItem menuItemBack = getMenuItemRandomSampleGenerator();

        foodTruck.addMenuItems(menuItemBack);
        assertThat(foodTruck.getMenuItems()).containsOnly(menuItemBack);
        assertThat(menuItemBack.getTruck()).isEqualTo(foodTruck);

        foodTruck.removeMenuItems(menuItemBack);
        assertThat(foodTruck.getMenuItems()).doesNotContain(menuItemBack);
        assertThat(menuItemBack.getTruck()).isNull();

        foodTruck.menuItems(new HashSet<>(Set.of(menuItemBack)));
        assertThat(foodTruck.getMenuItems()).containsOnly(menuItemBack);
        assertThat(menuItemBack.getTruck()).isEqualTo(foodTruck);

        foodTruck.setMenuItems(new HashSet<>());
        assertThat(foodTruck.getMenuItems()).doesNotContain(menuItemBack);
        assertThat(menuItemBack.getTruck()).isNull();
    }

    @Test
    void ordersTest() throws Exception {
        FoodTruck foodTruck = getFoodTruckRandomSampleGenerator();
        Order orderBack = getOrderRandomSampleGenerator();

        foodTruck.addOrders(orderBack);
        assertThat(foodTruck.getOrders()).containsOnly(orderBack);
        assertThat(orderBack.getFoodTruck()).isEqualTo(foodTruck);

        foodTruck.removeOrders(orderBack);
        assertThat(foodTruck.getOrders()).doesNotContain(orderBack);
        assertThat(orderBack.getFoodTruck()).isNull();

        foodTruck.orders(new HashSet<>(Set.of(orderBack)));
        assertThat(foodTruck.getOrders()).containsOnly(orderBack);
        assertThat(orderBack.getFoodTruck()).isEqualTo(foodTruck);

        foodTruck.setOrders(new HashSet<>());
        assertThat(foodTruck.getOrders()).doesNotContain(orderBack);
        assertThat(orderBack.getFoodTruck()).isNull();
    }

    @Test
    void cuisineTypeTest() throws Exception {
        FoodTruck foodTruck = getFoodTruckRandomSampleGenerator();
        CuisineType cuisineTypeBack = getCuisineTypeRandomSampleGenerator();

        foodTruck.setCuisineType(cuisineTypeBack);
        assertThat(foodTruck.getCuisineType()).isEqualTo(cuisineTypeBack);

        foodTruck.cuisineType(null);
        assertThat(foodTruck.getCuisineType()).isNull();
    }

    @Test
    void locationTest() throws Exception {
        FoodTruck foodTruck = getFoodTruckRandomSampleGenerator();
        Location locationBack = getLocationRandomSampleGenerator();

        foodTruck.setLocation(locationBack);
        assertThat(foodTruck.getLocation()).isEqualTo(locationBack);

        foodTruck.location(null);
        assertThat(foodTruck.getLocation()).isNull();
    }
}
