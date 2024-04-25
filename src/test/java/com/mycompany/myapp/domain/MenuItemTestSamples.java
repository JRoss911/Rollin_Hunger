package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class MenuItemTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static MenuItem getMenuItemSample1() {
        return new MenuItem().id(1L).name("name1").description("description1");
    }

    public static MenuItem getMenuItemSample2() {
        return new MenuItem().id(2L).name("name2").description("description2");
    }

    public static MenuItem getMenuItemRandomSampleGenerator() {
        return new MenuItem().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString()).description(UUID.randomUUID().toString());
    }
}
