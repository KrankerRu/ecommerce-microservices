package com.kranker.authservice.exception;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends BusinessException {

  public UserNotFoundException(String email) {
    super("User with email '" + email + "' not found", HttpStatus.NOT_FOUND);
  }
}
