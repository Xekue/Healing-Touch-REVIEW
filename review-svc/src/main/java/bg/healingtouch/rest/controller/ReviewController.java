package bg.healingtouch.rest.controller;

import bg.healingtouch.rest.controller.dto.CreateReviewDto;
import bg.healingtouch.rest.controller.dto.ReviewResponseDto;
import bg.healingtouch.rest.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReviewResponseDto createReview(@Valid @RequestBody CreateReviewDto dto) {
        return reviewService.createReview(dto);
    }

    @GetMapping("/therapist/{therapistId}")
    public List<ReviewResponseDto> getReviewsForTherapist(@PathVariable UUID therapistId) {
        return reviewService.getReviewsForTherapist(therapistId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteReview(@PathVariable UUID id) {
        reviewService.deleteReview(id);
    }

}
