package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.EventTestSamples.*;
import static com.mycompany.myapp.domain.FoodTruckTestSamples.*;
import static com.mycompany.myapp.domain.LocationTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class LocationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Location.class);
        Location location1 = getLocationSample1();
        Location location2 = new Location();
        assertThat(location1).isNotEqualTo(location2);

        location2.setId(location1.getId());
        assertThat(location1).isEqualTo(location2);

        location2 = getLocationSample2();
        assertThat(location1).isNotEqualTo(location2);
    }

    @Test
    void trucksTest() throws Exception {
        Location location = getLocationRandomSampleGenerator();
        FoodTruck foodTruckBack = getFoodTruckRandomSampleGenerator();

        location.addTrucks(foodTruckBack);
        assertThat(location.getTrucks()).containsOnly(foodTruckBack);
        assertThat(foodTruckBack.getLocation()).isEqualTo(location);

        location.removeTrucks(foodTruckBack);
        assertThat(location.getTrucks()).doesNotContain(foodTruckBack);
        assertThat(foodTruckBack.getLocation()).isNull();

        location.trucks(new HashSet<>(Set.of(foodTruckBack)));
        assertThat(location.getTrucks()).containsOnly(foodTruckBack);
        assertThat(foodTruckBack.getLocation()).isEqualTo(location);

        location.setTrucks(new HashSet<>());
        assertThat(location.getTrucks()).doesNotContain(foodTruckBack);
        assertThat(foodTruckBack.getLocation()).isNull();
    }

    @Test
    void eventsTest() throws Exception {
        Location location = getLocationRandomSampleGenerator();
        Event eventBack = getEventRandomSampleGenerator();

        location.addEvents(eventBack);
        assertThat(location.getEvents()).containsOnly(eventBack);
        assertThat(eventBack.getLocation()).isEqualTo(location);

        location.removeEvents(eventBack);
        assertThat(location.getEvents()).doesNotContain(eventBack);
        assertThat(eventBack.getLocation()).isNull();

        location.events(new HashSet<>(Set.of(eventBack)));
        assertThat(location.getEvents()).containsOnly(eventBack);
        assertThat(eventBack.getLocation()).isEqualTo(location);

        location.setEvents(new HashSet<>());
        assertThat(location.getEvents()).doesNotContain(eventBack);
        assertThat(eventBack.getLocation()).isNull();
    }
}
