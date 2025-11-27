package bg.healingtouch.rest.integration;

import bg.healingtouch.rest.exception.ReviewNotFoundException;
import bg.healingtouch.rest.model.Review;
import bg.healingtouch.rest.repository.ReviewRepository;
import bg.healingtouch.rest.service.ReviewService;
import bg.healingtouch.rest.web.dto.CreateReviewDto;
import bg.healingtouch.rest.web.dto.ReviewResponseDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase
@EntityScan("bg.healingtouch.rest.model")
@EnableJpaRepositories("bg.healingtouch.rest.repository")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ReviewServiceIntegrationTest {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private ReviewRepository reviewRepository;

    @Test
    void testCreateReview_persistsToDatabase() {

        CreateReviewDto dto = new CreateReviewDto();
        dto.setBookingId(UUID.randomUUID());
        dto.setTherapistId(UUID.randomUUID());
        dto.setCustomerId(UUID.randomUUID());
        dto.setRating(5);
        dto.setComment("Amazing!");

        ReviewResponseDto response = reviewService.createReview(dto);

        // Validate response and DB state
        assertThat(response.getId()).isNotNull();
        assertThat(reviewRepository.count()).isEqualTo(1);

        Review saved = reviewRepository.findAll().get(0);

        assertThat(saved.getComment()).isEqualTo("Amazing!");
        assertThat(saved.getRating()).isEqualTo(5);
        assertThat(saved.getTherapistId()).isEqualTo(dto.getTherapistId());
        assertThat(saved.getCustomerId()).isEqualTo(dto.getCustomerId());
        assertThat(saved.getBookingId()).isEqualTo(dto.getBookingId());
    }

    @Test
    void testGetReviewsForTherapist_returnsCorrectResults() {

        UUID therapistId = UUID.randomUUID();

        // Insert 2 reviews for the same therapist
        for (int i = 1; i <= 2; i++) {
            Review review = new Review();
            review.setBookingId(UUID.randomUUID());
            review.setTherapistId(therapistId);
            review.setCustomerId(UUID.randomUUID());
            review.setRating(4);
            review.setComment("Good" + i);
            reviewRepository.save(review);
        }

        List<ReviewResponseDto> results = reviewService.getReviewsForTherapist(therapistId);

        assertThat(results).hasSize(2);
        assertThat(results)
                .extracting(ReviewResponseDto::getComment)
                .containsExactlyInAnyOrder("Good1", "Good2");
    }

    @Test
    void testDeleteReview_successfulDeletion() {

        Review review = new Review();
        review.setBookingId(UUID.randomUUID());
        review.setTherapistId(UUID.randomUUID());
        review.setCustomerId(UUID.randomUUID());
        review.setRating(5);
        review.setComment("To delete");
        reviewRepository.save(review);

        UUID id = review.getId();

        reviewService.deleteReview(id);

        assertThat(reviewRepository.existsById(id)).isFalse();
    }

    @Test
    void testDeleteReview_throwsWhenNotFound() {
        UUID id = UUID.randomUUID();

        assertThatThrownBy(() -> reviewService.deleteReview(id))
                .isInstanceOf(ReviewNotFoundException.class)
                .hasMessageContaining("not found");
    }
}
