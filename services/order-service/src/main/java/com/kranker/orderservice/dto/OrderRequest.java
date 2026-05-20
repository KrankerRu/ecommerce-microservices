package com.kranker.orderservice.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record OrderRequest(
    @NotNull(message = "UserId is required")
    Long userId,

    @NotNull(message = "ProductId is required")
    Long productId,

    @Positive(message = "Quantity must be positive")
    Integer quantity
) {}