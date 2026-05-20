package com.kranker.productservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public record ProductRequest (

  @NotBlank(message = "Product name is required")
  String name,

  String description,

  @Positive(message = "Price must be positive")
  BigDecimal price,

  @Positive(message = "Stock must be positive")
  Integer stock
) {}