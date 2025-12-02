package bg.healingtouch.rest.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class BookingEventConsumer {

    @KafkaListener(topics = "booking-events", groupId = "review-service-group")
    public void listen(String message) {
        log.info("Received Kafka event in Review Service: {}", message);
    }
}
