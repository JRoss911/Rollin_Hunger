package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.FoodTruckTestSamples.*;
import static com.mycompany.myapp.domain.MenuItemTestSamples.*;
import static com.mycompany.myapp.domain.OrderTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class MenuItemTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MenuItem.class);
        MenuItem menuItem1 = getMenuItemSample1();
        MenuItem menuItem2 = new MenuItem();
        assertThat(menuItem1).isNotEqualTo(menuItem2);

        menuItem2.setId(menuItem1.getId());
        assertThat(menuItem1).isEqualTo(menuItem2);

        menuItem2 = getMenuItemSample2();
        assertThat(menuItem1).isNotEqualTo(menuItem2);
    }

    @Test
    void truckTest() throws Exception {
        MenuItem menuItem = getMenuItemRandomSampleGenerator();
        FoodTruck foodTruckBack = getFoodTruckRandomSampleGenerator();

        menuItem.setTruck(foodTruckBack);
        assertThat(menuItem.getTruck()).isEqualTo(foodTruckBack);

        menuItem.truck(null);
        assertThat(menuItem.getTruck()).isNull();
    }

    @Test
    void ordersTest() throws Exception {
        MenuItem menuItem = getMenuItemRandomSampleGenerator();
        Order orderBack = getOrderRandomSampleGenerator();

        menuItem.addOrders(orderBack);
        assertThat(menuItem.getOrders()).containsOnly(orderBack);
        assertThat(orderBack.getMenuItems()).containsOnly(menuItem);

        menuItem.removeOrders(orderBack);
        assertThat(menuItem.getOrders()).doesNotContain(orderBack);
        assertThat(orderBack.getMenuItems()).doesNotContain(menuItem);

        menuItem.orders(new HashSet<>(Set.of(orderBack)));
        assertThat(menuItem.getOrders()).containsOnly(orderBack);
        assertThat(orderBack.getMenuItems()).containsOnly(menuItem);

        menuItem.setOrders(new HashSet<>());
        assertThat(menuItem.getOrders()).doesNotContain(orderBack);
        assertThat(orderBack.getMenuItems()).doesNotContain(menuItem);
    }
}
