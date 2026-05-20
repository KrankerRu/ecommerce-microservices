package com.kranker.orderservice.dto;

import java.math.BigDecimal;

public record ProductDto(
    Long id,
    String name,
    BigDecimal price,
    Integer stock
) {}