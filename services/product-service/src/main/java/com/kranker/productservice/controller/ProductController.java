package com.kranker.productservice.controller;

import com.kranker.productservice.dto.ProductRequest;
import com.kranker.productservice.dto.ProductResponse;
import com.kranker.productservice.service.ProductService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
@Tag(name = "Product Controller", description = "API для управления товарами")
public class ProductController {

  private ProductService productService;

  @PostMapping
  public ResponseEntity<ProductResponse> createProduct(@RequestBody @Valid ProductRequest request) {
    return ResponseEntity.ok(productService.createProduct(request));
  }

  @GetMapping
  public ResponseEntity<List<ProductResponse>> getAllProducts() {
    return ResponseEntity.ok(productService.getAllProducts());
  }

  @GetMapping("/{id}")
  public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) {
    return ResponseEntity.ok(productService.getProductById(id));
  }
}
