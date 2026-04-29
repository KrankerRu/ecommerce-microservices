package com.kranker.orderservice.exception;

import com.kranker.orderservice.dto.ApiErrorResponse;
import com.kranker.orderservice.exception.BusinessException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

  @ExceptionHandler(BusinessException.class)
  public ResponseEntity<ApiErrorResponse> handleBusinessException(
      BusinessException ex, HttpServletRequest request) {

    log.warn("Business exception: {}", ex.getMessage());

    ApiErrorResponse response = ApiErrorResponse.builder()
        .timestamp(LocalDateTime.now())
        .status(ex.getStatus().value())
        .error(ex.getStatus().getReasonPhrase())
        .message(ex.getMessage())
        .path(request.getRequestURI())
        .build();

    return ResponseEntity.status(ex.getStatus()).body(response);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiErrorResponse> handleValidationException(
      MethodArgumentNotValidException ex, HttpServletRequest request) {

    String message = ex.getBindingResult()
        .getFieldErrors()
        .stream()
        .map(err -> err.getField() + ": " + err.getDefaultMessage())
        .findFirst()
        .orElse("Validation failed");

    log.warn("Validation error: {}", message);

    ApiErrorResponse response = ApiErrorResponse.builder()
        .timestamp(LocalDateTime.now())
        .status(400)
        .error("Bad Request")
        .message(message)
        .path(request.getRequestURI())
        .build();

    return ResponseEntity.badRequest().body(response);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiErrorResponse> handleGenericException(
      Exception ex, HttpServletRequest request) {

    log.error("Unexpected error at {}", request.getRequestURI(), ex);

    ApiErrorResponse response = ApiErrorResponse.builder()
        .timestamp(LocalDateTime.now())
        .status(500)
        .error("Internal Server Error")
        .message("An unexpected error occurred")
        .path(request.getRequestURI())
        .build();

    return ResponseEntity.internalServerError().body(response);
  }
}