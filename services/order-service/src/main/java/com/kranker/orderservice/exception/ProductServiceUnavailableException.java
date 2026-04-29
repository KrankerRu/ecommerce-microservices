package com.kranker.orderservice.exception;

import org.springframework.http.HttpStatus;

public class ProductServiceUnavailableException extends BusinessException {
  public ProductServiceUnavailableException(String message) {
    super(message, HttpStatus.SERVICE_UNAVAILABLE); // 503
  }
}