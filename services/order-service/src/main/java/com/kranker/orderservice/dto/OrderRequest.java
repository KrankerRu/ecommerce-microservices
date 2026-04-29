package com.kranker.orderservice.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderRequest {

  @NotNull(message = "UserId is required")
  private Long userId;

  @NotNull(message = "ProductId is required")
  private Long productId;

  @Positive(message = "Quantity must be positive")
  private Integer quantity;




}
