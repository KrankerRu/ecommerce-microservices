package com.kranker.orderservice.exception;

import org.springframework.http.HttpStatus;

public class OrderNotFoundException extends BusinessException {
  public OrderNotFoundException(Long id) {
    super("Order not found with id: " + id, HttpStatus.NOT_FOUND);
  }
}