package service_generator.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record ProductAttributeChangedEvent(

        @JsonProperty("event_id")
        UUID eventId,

        @JsonProperty("product_id")
        UUID productId,

        @JsonProperty("price")
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        BigDecimal price,

        @JsonProperty("available_stock")
        Integer availableStock,

        @JsonProperty("occurred_at")
        Instant occurredAt
) {}