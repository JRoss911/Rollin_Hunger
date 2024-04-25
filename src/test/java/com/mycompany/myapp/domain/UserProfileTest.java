package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.FoodTruckTestSamples.*;
import static com.mycompany.myapp.domain.OrderTestSamples.*;
import static com.mycompany.myapp.domain.ReviewTestSamples.*;
import static com.mycompany.myapp.domain.UserProfileTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class UserProfileTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserProfile.class);
        UserProfile userProfile1 = getUserProfileSample1();
        UserProfile userProfile2 = new UserProfile();
        assertThat(userProfile1).isNotEqualTo(userProfile2);

        userProfile2.setId(userProfile1.getId());
        assertThat(userProfile1).isEqualTo(userProfile2);

        userProfile2 = getUserProfileSample2();
        assertThat(userProfile1).isNotEqualTo(userProfile2);
    }

    @Test
    void reviewsTest() throws Exception {
        UserProfile userProfile = getUserProfileRandomSampleGenerator();
        Review reviewBack = getReviewRandomSampleGenerator();

        userProfile.addReviews(reviewBack);
        assertThat(userProfile.getReviews()).containsOnly(reviewBack);
        assertThat(reviewBack.getUser()).isEqualTo(userProfile);

        userProfile.removeReviews(reviewBack);
        assertThat(userProfile.getReviews()).doesNotContain(reviewBack);
        assertThat(reviewBack.getUser()).isNull();

        userProfile.reviews(new HashSet<>(Set.of(reviewBack)));
        assertThat(userProfile.getReviews()).containsOnly(reviewBack);
        assertThat(reviewBack.getUser()).isEqualTo(userProfile);

        userProfile.setReviews(new HashSet<>());
        assertThat(userProfile.getReviews()).doesNotContain(reviewBack);
        assertThat(reviewBack.getUser()).isNull();
    }

    @Test
    void ordersTest() throws Exception {
        UserProfile userProfile = getUserProfileRandomSampleGenerator();
        Order orderBack = getOrderRandomSampleGenerator();

        userProfile.addOrders(orderBack);
        assertThat(userProfile.getOrders()).containsOnly(orderBack);
        assertThat(orderBack.getUser()).isEqualTo(userProfile);

        userProfile.removeOrders(orderBack);
        assertThat(userProfile.getOrders()).doesNotContain(orderBack);
        assertThat(orderBack.getUser()).isNull();

        userProfile.orders(new HashSet<>(Set.of(orderBack)));
        assertThat(userProfile.getOrders()).containsOnly(orderBack);
        assertThat(orderBack.getUser()).isEqualTo(userProfile);

        userProfile.setOrders(new HashSet<>());
        assertThat(userProfile.getOrders()).doesNotContain(orderBack);
        assertThat(orderBack.getUser()).isNull();
    }

    @Test
    void truckTest() throws Exception {
        UserProfile userProfile = getUserProfileRandomSampleGenerator();
        FoodTruck foodTruckBack = getFoodTruckRandomSampleGenerator();

        userProfile.setTruck(foodTruckBack);
        assertThat(userProfile.getTruck()).isEqualTo(foodTruckBack);
        assertThat(foodTruckBack.getOwner()).isEqualTo(userProfile);

        userProfile.truck(null);
        assertThat(userProfile.getTruck()).isNull();
        assertThat(foodTruckBack.getOwner()).isNull();
    }
}
