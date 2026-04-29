package com.kranker.orderservice.exception;

import org.springframework.http.HttpStatus;

public class ProductNotFoundException extends BusinessException {
  public ProductNotFoundException(Long id) {
    super("Product not found with id: " + id, HttpStatus.NOT_FOUND);
  }
}