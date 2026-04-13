package com.kranker.authservice.handler;

import com.kranker.authservice.dto.ApiErrorResponse;
import com.kranker.authservice.exception.BusinessException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(BusinessException.class)
  public ResponseEntity<ApiErrorResponse> handleBusinessException(
      BusinessException ex,
      HttpServletRequest request) {

    ApiErrorResponse response = ApiErrorResponse.of(
        ex.getStatus().value(),
        ex.getStatus().getReasonPhrase(),
        ex.getMessage(),
        request.getRequestURI()
    );
    return ResponseEntity.status(ex.getStatus()).body(response);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiErrorResponse> handleValidationException(
      MethodArgumentNotValidException ex,
      HttpServletRequest request) {

    String message = ex.getBindingResult()
        .getFieldErrors()
        .stream()
        .map(err -> err.getField() + ": " + err.getDefaultMessage())
        .findFirst()
        .orElse("Validation failed");

    ApiErrorResponse response = ApiErrorResponse.of(
        400,
        "Bad Request",
        message,
        request.getRequestURI()
    );
    return ResponseEntity.badRequest().body(response);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiErrorResponse> handleGenericException(
      Exception ex,
      HttpServletRequest request) {

    // log.error("Unexpected error", ex);

    ApiErrorResponse response = ApiErrorResponse.of(
        500,
        "Internal Server Error",
        "An unexpected error occurred",
        request.getRequestURI()
    );
    return ResponseEntity.internalServerError().body(response);
  }
}