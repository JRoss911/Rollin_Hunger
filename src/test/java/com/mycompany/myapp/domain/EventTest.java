package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.EventTestSamples.*;
import static com.mycompany.myapp.domain.LocationTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EventTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Event.class);
        Event event1 = getEventSample1();
        Event event2 = new Event();
        assertThat(event1).isNotEqualTo(event2);

        event2.setId(event1.getId());
        assertThat(event1).isEqualTo(event2);

        event2 = getEventSample2();
        assertThat(event1).isNotEqualTo(event2);
    }

    @Test
    void locationTest() throws Exception {
        Event event = getEventRandomSampleGenerator();
        Location locationBack = getLocationRandomSampleGenerator();

        event.setLocation(locationBack);
        assertThat(event.getLocation()).isEqualTo(locationBack);

        event.location(null);
        assertThat(event.getLocation()).isNull();
    }
}
