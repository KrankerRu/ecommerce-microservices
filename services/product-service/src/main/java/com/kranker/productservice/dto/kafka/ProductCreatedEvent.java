package com.kranker.productservice.dto.kafka;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductCreatedEvent {

  private Long productId;
  private String productName;
  private LocalDateTime createdAt;
  private Long createdByUserId;

}
