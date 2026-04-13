package com.kranker.authservice.exception;

import org.springframework.http.HttpStatus;

public class InvalidCredentialsException extends BusinessException {

  public InvalidCredentialsException() {
    super("Invalid credentials", HttpStatus.UNAUTHORIZED);
  }
}
