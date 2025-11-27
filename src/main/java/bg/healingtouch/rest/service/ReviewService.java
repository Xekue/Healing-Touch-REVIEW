package bg.healingtouch.rest.service;

import bg.healingtouch.rest.web.dto.CreateReviewDto;
import bg.healingtouch.rest.web.dto.ReviewResponseDto;
import bg.healingtouch.rest.exception.ReviewNotFoundException;
import bg.healingtouch.rest.model.Review;
import bg.healingtouch.rest.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;

    private static final Logger log = LoggerFactory.getLogger(ReviewService.class);


    public ReviewResponseDto createReview(CreateReviewDto dto) {

        log.info("Creating new review for therapist {} by customer {}",
                dto.getTherapistId(), dto.getCustomerId());

        Review review = new Review();
        review.setBookingId(dto.getBookingId());
        review.setTherapistId(dto.getTherapistId());
        review.setCustomerId(dto.getCustomerId());
        review.setRating(dto.getRating());
        review.setComment(dto.getComment());

        reviewRepository.save(review);

        log.info("Successfully created review {} with rating {}", review.getId(), review.getRating());
        return mapToDto(review);
    }

    public List<ReviewResponseDto> getReviewsForTherapist(UUID therapistId) {
        log.debug("Fetching all reviews for therapist {}", therapistId);

        List<ReviewResponseDto> reviews = reviewRepository.findByTherapistId(therapistId)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());

        log.info("Found {} reviews for therapist {}", reviews.size(), therapistId);
        return reviews;
    }

    private ReviewResponseDto mapToDto(Review review) {
        ReviewResponseDto dto = new ReviewResponseDto();
        dto.setId(review.getId());
        dto.setBookingId(review.getBookingId());
        dto.setTherapistId(review.getTherapistId());
        dto.setCustomerId(review.getCustomerId());
        dto.setComment(review.getComment());
        dto.setRating(review.getRating());
        dto.setCreatedOn(review.getCreatedOn());
        dto.setReviewerName("Anonymous");
        return dto;
    }

    public void deleteReview(UUID reviewId) {
        log.warn("Attempting to delete review {}", reviewId);

        if (!reviewRepository.existsById(reviewId)) {
            log.error("Review deletion failed: Review {} not found", reviewId);
            throw new ReviewNotFoundException("Review not found with ID: " + reviewId);
        }
        reviewRepository.deleteById(reviewId);
        log.info("Review {} successfully deleted", reviewId);
    }

}
