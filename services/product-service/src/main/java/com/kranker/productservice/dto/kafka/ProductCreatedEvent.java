package com.kranker.productservice.dto.kafka;

import java.time.LocalDateTime;

public record ProductCreatedEvent(
    Long productId,
    String productName,
    LocalDateTime createdAt,
    Long createdByUserId
) {}