package com.kranker.orderservice.exception;

import org.springframework.http.HttpStatus;

public class InsufficientStockException extends BusinessException {
  public InsufficientStockException(String productName, Integer available, Integer requested) {
    super(String.format("Insufficient stock for '%s': available %d, requested %d",
        productName, available, requested), HttpStatus.CONFLICT);
  }
}