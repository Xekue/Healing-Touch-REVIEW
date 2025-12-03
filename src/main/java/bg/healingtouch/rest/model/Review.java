package bg.healingtouch.rest.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name ="reviews")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID bookingId;

    @Column(nullable = false)
    private UUID therapistId;

    @Column(nullable = false)
    private UUID customerId;

    @Column(nullable = false)
    private int rating;

    @Column(length = 2000)
    private String comment;

    private LocalDateTime createdOn = LocalDateTime.now();

}
