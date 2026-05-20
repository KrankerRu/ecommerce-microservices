package com.kranker.orderservice.dto;

import com.kranker.orderservice.entity.OrderStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;

public record OrderResponse(
    Long id,
    Long userId,
    BigDecimal totalAmount,
    OrderStatus status,
    List<OrderItemResponse> items,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {}