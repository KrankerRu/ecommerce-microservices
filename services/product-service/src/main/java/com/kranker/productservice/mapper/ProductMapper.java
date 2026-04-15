package com.kranker.productservice.mapper;

import com.kranker.productservice.dto.ProductRequest;
import com.kranker.productservice.dto.ProductResponse;
import com.kranker.productservice.entity.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {
  public ProductResponse toResponse(Product product) {
    if (product == null) {
      return null;
    }
    return ProductResponse.builder()
        .id(product.getId())
        .name(product.getName())
        .description(product.getDescription())
        .price(product.getPrice())
        .stock(product.getStock())
        .createdAt(product.getCreatedAt())
        .updatedAt(product.getUpdatedAt())
        .createdByUserId(product.getCreatedByUserId())
        .build();
  }

  public Product toEntity(ProductRequest request) {
    if (request == null) {
      return null;
    }
    return Product.builder()
        .name(request.getName())
        .description(request.getDescription())
        .price(request.getPrice())
        .stock(request.getStock())
        .build();
  }
}
