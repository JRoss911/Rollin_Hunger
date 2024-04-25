package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class FoodTruckTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static FoodTruck getFoodTruckSample1() {
        return new FoodTruck().id(1L).name("name1").description("description1").profilePicture("profilePicture1");
    }

    public static FoodTruck getFoodTruckSample2() {
        return new FoodTruck().id(2L).name("name2").description("description2").profilePicture("profilePicture2");
    }

    public static FoodTruck getFoodTruckRandomSampleGenerator() {
        return new FoodTruck()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .profilePicture(UUID.randomUUID().toString());
    }
}
