package com.kranker.productservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequest {

  @NotBlank(message = "Product name is required")
  private String name;

  private String description;

  @Positive(message = "Price must be positive")
  private BigDecimal price;

  @Positive(message = "Stock must be positive")
  private Integer stock;
}