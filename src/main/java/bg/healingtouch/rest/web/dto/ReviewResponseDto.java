package bg.healingtouch.rest.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewResponseDto {

    private UUID id;
    private UUID bookingId;
    private UUID therapistId;
    private UUID customerId;
    private String comment;
    private int rating;
    private LocalDateTime createdOn;

    private String reviewerName;

    private LocalDateTime reviewDate;

}
