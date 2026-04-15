package com.kranker.productservice.exception;

import org.springframework.http.HttpStatus;

public class ProductAlreadyExistsException extends BusinessException {

  public ProductAlreadyExistsException(String name) {
    super("Product with name '" + name + "' already exists", HttpStatus.CONFLICT);
  }
}
