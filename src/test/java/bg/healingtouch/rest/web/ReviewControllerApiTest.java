package bg.healingtouch.rest.web;

import bg.healingtouch.rest.service.ReviewService;
import bg.healingtouch.rest.web.dto.CreateReviewDto;
import bg.healingtouch.rest.web.dto.ReviewResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

@WebMvcTest(controllers = ReviewController.class)
@ActiveProfiles("test")
class ReviewControllerApiTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ReviewService reviewService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreateReview_return201() throws Exception {

        CreateReviewDto dto = new CreateReviewDto();
        dto.setBookingId(UUID.randomUUID());
        dto.setTherapistId(UUID.randomUUID());
        dto.setCustomerId(UUID.randomUUID());
        dto.setRating(5);
        dto.setComment("Amazing job!");

        ReviewResponseDto responseDto = new ReviewResponseDto();
        responseDto.setId(UUID.randomUUID());
        responseDto.setBookingId(dto.getBookingId());
        responseDto.setTherapistId(dto.getTherapistId());
        responseDto.setCustomerId(dto.getCustomerId());
        responseDto.setRating(5);
        responseDto.setComment("Amazing job!");

        Mockito.when(reviewService.createReview(any(CreateReviewDto.class)))
                .thenReturn(responseDto);

        mockMvc.perform(
                        post("/api/reviews")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.rating").value(5))
                .andExpect(jsonPath("$.comment").value("Amazing job!"));
    }

    @Test
    void testGetReviewsForTherapist_returns200() throws Exception {
        UUID therapistId = UUID.randomUUID();

        ReviewResponseDto r1 = new ReviewResponseDto();
        r1.setId(UUID.randomUUID());
        r1.setTherapistId(therapistId);
        r1.setRating(5);
        r1.setComment("Good");

        Mockito.when(reviewService.getReviewsForTherapist(therapistId))
                .thenReturn(List.of(r1));

        mockMvc.perform(get("/api/reviews/therapist/" + therapistId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].comment").value("Good"));
    }

    @Test
    void testDeleteReview_returns204() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc.perform(delete("/api/reviews/" + id))
                .andExpect(status().isNoContent());

        Mockito.verify(reviewService).deleteReview(id);
    }
}
