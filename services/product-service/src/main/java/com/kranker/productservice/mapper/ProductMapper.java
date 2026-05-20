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
    return new ProductResponse(product.getId(), product.getName(), product.getDescription(), product.getPrice(),
        product.getStock(), product.getCreatedAt(), product.getUpdatedAt(), product.getCreatedByUserId());
  }

  public Product toEntity(ProductRequest request) {
    if (request == null) {
      return null;
    }
    return Product.builder()
        .name(request.name())
        .description(request.description())
        .price(request.price())
        .stock(request.stock())
        .build();
  }
}
