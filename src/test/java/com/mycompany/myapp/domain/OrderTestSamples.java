package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class OrderTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Order getOrderSample1() {
        return new Order().id(1L).quantity(1).status("status1");
    }

    public static Order getOrderSample2() {
        return new Order().id(2L).quantity(2).status("status2");
    }

    public static Order getOrderRandomSampleGenerator() {
        return new Order().id(longCount.incrementAndGet()).quantity(intCount.incrementAndGet()).status(UUID.randomUUID().toString());
    }
}
