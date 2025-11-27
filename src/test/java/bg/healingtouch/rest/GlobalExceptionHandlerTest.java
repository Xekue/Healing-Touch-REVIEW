package bg.healingtouch.rest;

import bg.healingtouch.rest.exception.GlobalExceptionHandler;
import bg.healingtouch.rest.exception.ReviewNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

public class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void testHandleEntityNotFound() {
        EntityNotFoundException ex = new EntityNotFoundException("entity missing");

        ResponseEntity<String> response = handler.handleEntityNotFound(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isEqualTo("entity missing");
    }

    @Test
    void testHandleReviewNotFound() {
        ReviewNotFoundException ex = new ReviewNotFoundException("review missing");

        ResponseEntity<String> response = handler.handleReviewNotFound(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isEqualTo("review missing");
    }
}
