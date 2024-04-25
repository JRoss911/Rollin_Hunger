package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.FoodTruckTestSamples.*;
import static com.mycompany.myapp.domain.MenuItemTestSamples.*;
import static com.mycompany.myapp.domain.OrderTestSamples.*;
import static com.mycompany.myapp.domain.UserProfileTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class OrderTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Order.class);
        Order order1 = getOrderSample1();
        Order order2 = new Order();
        assertThat(order1).isNotEqualTo(order2);

        order2.setId(order1.getId());
        assertThat(order1).isEqualTo(order2);

        order2 = getOrderSample2();
        assertThat(order1).isNotEqualTo(order2);
    }

    @Test
    void menuItemTest() throws Exception {
        Order order = getOrderRandomSampleGenerator();
        MenuItem menuItemBack = getMenuItemRandomSampleGenerator();

        order.addMenuItem(menuItemBack);
        assertThat(order.getMenuItems()).containsOnly(menuItemBack);

        order.removeMenuItem(menuItemBack);
        assertThat(order.getMenuItems()).doesNotContain(menuItemBack);

        order.menuItems(new HashSet<>(Set.of(menuItemBack)));
        assertThat(order.getMenuItems()).containsOnly(menuItemBack);

        order.setMenuItems(new HashSet<>());
        assertThat(order.getMenuItems()).doesNotContain(menuItemBack);
    }

    @Test
    void userTest() throws Exception {
        Order order = getOrderRandomSampleGenerator();
        UserProfile userProfileBack = getUserProfileRandomSampleGenerator();

        order.setUser(userProfileBack);
        assertThat(order.getUser()).isEqualTo(userProfileBack);

        order.user(null);
        assertThat(order.getUser()).isNull();
    }

    @Test
    void foodTruckTest() throws Exception {
        Order order = getOrderRandomSampleGenerator();
        FoodTruck foodTruckBack = getFoodTruckRandomSampleGenerator();

        order.setFoodTruck(foodTruckBack);
        assertThat(order.getFoodTruck()).isEqualTo(foodTruckBack);

        order.foodTruck(null);
        assertThat(order.getFoodTruck()).isNull();
    }
}
