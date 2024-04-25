package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CuisineTypeTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static CuisineType getCuisineTypeSample1() {
        return new CuisineType().id(1L).name("name1");
    }

    public static CuisineType getCuisineTypeSample2() {
        return new CuisineType().id(2L).name("name2");
    }

    public static CuisineType getCuisineTypeRandomSampleGenerator() {
        return new CuisineType().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString());
    }
}
