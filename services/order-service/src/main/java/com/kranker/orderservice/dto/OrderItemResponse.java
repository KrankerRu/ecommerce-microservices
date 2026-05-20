package com.kranker.orderservice.dto;

import java.math.BigDecimal;

public record OrderItemResponse(
    Long productId,
    String productName,
    Integer quantity,
    BigDecimal priceAtOrder,
    BigDecimal subtotal
) {}