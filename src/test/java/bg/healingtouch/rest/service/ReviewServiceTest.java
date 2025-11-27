package bg.healingtouch.rest.service;

import bg.healingtouch.rest.web.dto.CreateReviewDto;
import bg.healingtouch.rest.web.dto.ReviewResponseDto;
import bg.healingtouch.rest.model.Review;
import bg.healingtouch.rest.repository.ReviewRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ReviewServiceTest {

    private ReviewService reviewService;
    private ReviewRepository reviewRepository;

    @BeforeEach
    void setUp() {
        reviewRepository = mock(ReviewRepository.class);
        reviewService = new ReviewService(reviewRepository);
    }

    @Test
    void testCreateReview_success() {

        UUID bookingId = UUID.randomUUID();
        UUID therapistId = UUID.randomUUID();
        UUID customerId = UUID.randomUUID();

        CreateReviewDto dto = new CreateReviewDto();
        dto.setBookingId(bookingId);
        dto.setTherapistId(therapistId);
        dto.setCustomerId(customerId);
        dto.setRating(5);
        dto.setComment("Amazing experience!");

        // Mock repo save to return the passed entity
        when(reviewRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        ReviewResponseDto response = reviewService.createReview(dto);

        // Capture the saved Review entity
        ArgumentCaptor<Review> captor = ArgumentCaptor.forClass(Review.class);
        verify(reviewRepository).save(captor.capture());
        Review saved = captor.getValue();

        // Validate saved entity
        assertThat(saved.getBookingId()).isEqualTo(bookingId);
        assertThat(saved.getTherapistId()).isEqualTo(therapistId);
        assertThat(saved.getCustomerId()).isEqualTo(customerId);
        assertThat(saved.getRating()).isEqualTo(5);
        assertThat(saved.getComment()).isEqualTo("Amazing experience!");

        // Validate returned DTO
        assertThat(response.getComment()).isEqualTo("Amazing experience!");
        assertThat(response.getRating()).isEqualTo(5);
    }

    @Test
    void testGetReviewsForTherapist_returnList() {
        UUID therapistId = UUID.randomUUID();

        Review review = new Review();
        review.setId(UUID.randomUUID());
        review.setTherapistId(therapistId);
        review.setCustomerId(UUID.randomUUID());
        review.setBookingId(UUID.randomUUID());
        review.setRating(4);
        review.setComment("Good");

        when(reviewRepository.findByTherapistId(therapistId))
                .thenReturn(List.of(review));

        List<ReviewResponseDto> result = reviewService.getReviewsForTherapist(therapistId);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getRating()).isEqualTo(4);
        assertThat(result.get(0).getComment()).isEqualTo("Good");
    }

    @Test
    void testDeleteReview_success() {
        UUID id = UUID.randomUUID();

        when(reviewRepository.existsById(id)).thenReturn(true);

        reviewService.deleteReview(id);

        verify(reviewRepository).deleteById(id);
    }
}
