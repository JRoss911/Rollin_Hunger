package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class UserProfileTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static UserProfile getUserProfileSample1() {
        return new UserProfile().id(1L).email("email1").points(1);
    }

    public static UserProfile getUserProfileSample2() {
        return new UserProfile().id(2L).email("email2").points(2);
    }

    public static UserProfile getUserProfileRandomSampleGenerator() {
        return new UserProfile().id(longCount.incrementAndGet()).email(UUID.randomUUID().toString()).points(intCount.incrementAndGet());
    }
}
