package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.ReviewTestSamples.*;
import static com.mycompany.myapp.domain.UserProfileTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ReviewTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Review.class);
        Review review1 = getReviewSample1();
        Review review2 = new Review();
        assertThat(review1).isNotEqualTo(review2);

        review2.setId(review1.getId());
        assertThat(review1).isEqualTo(review2);

        review2 = getReviewSample2();
        assertThat(review1).isNotEqualTo(review2);
    }

    @Test
    void userTest() throws Exception {
        Review review = getReviewRandomSampleGenerator();
        UserProfile userProfileBack = getUserProfileRandomSampleGenerator();

        review.setUser(userProfileBack);
        assertThat(review.getUser()).isEqualTo(userProfileBack);

        review.user(null);
        assertThat(review.getUser()).isNull();
    }
}
