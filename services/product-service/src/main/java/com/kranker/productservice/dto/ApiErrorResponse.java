package com.kranker.productservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiErrorResponse {

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime timestamp;

  private Integer status;
  private String error;
  private String message;
  private String path;

  public static ApiErrorResponse of(Integer status, String error, String message, String path) {
    return ApiErrorResponse.builder()
        .timestamp(LocalDateTime.now())
        .status(status)
        .error(error)
        .message(message)
        .path(path)
        .build();
  }
}
