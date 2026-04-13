package com.kranker.authservice.controller;

import com.kranker.authservice.dto.AuthResponse;
import com.kranker.authservice.dto.LoginRequest;
import com.kranker.authservice.dto.RegisterRequest;
import com.kranker.authservice.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;

  @GetMapping("/test")
  public String test() {
    return "Auth service is working!";
  }

  @PostMapping("/api/auth/register")
  public ResponseEntity<String> register(@RequestBody @Valid RegisterRequest request) {
    authService.register(request);
    return ResponseEntity.ok("User registered");
  }

  @PostMapping("/api/auth/login")
  public ResponseEntity<AuthResponse> login(@RequestBody @Valid LoginRequest request) {
    AuthResponse response = authService.login(request);
    return ResponseEntity.ok(response);
  }
}