package com.kranker.authservice.service;

import com.kranker.authservice.dto.AuthResponse;
import com.kranker.authservice.dto.LoginRequest;
import com.kranker.authservice.dto.RegisterRequest;
import com.kranker.authservice.entity.User;
import com.kranker.authservice.exception.InvalidCredentialsException;
import com.kranker.authservice.exception.UserAlreadyExistsException;
import com.kranker.authservice.exception.UserNotFoundException;
import com.kranker.authservice.jwt.JwtService;
import com.kranker.authservice.repository.UserRepository;
import io.micrometer.core.instrument.MeterRegistry;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
public class AuthService {

  private UserRepository userRepository;
  private PasswordEncoder passwordEncoder;
  private JwtService jwtService;
  private final MeterRegistry meterRegistry;

  public void register(RegisterRequest request) {
    var user = userRepository.findByEmail(request.getEmail());
    if (user.isPresent()) {
      throw new UserAlreadyExistsException(request.getEmail());

    }
    User newUser = User.builder()
        .email(request.getEmail())
        .firstName(request.getFirstName())
        .lastName(request.getLastName())
        .password(passwordEncoder.encode(request.getPassword()))
        .createdAt(LocalDateTime.now())
        .role("USER")
        .build();

    userRepository.save(newUser);
    meterRegistry.counter("registrations.success.total").increment();
  }
  public AuthResponse login(LoginRequest request) {
    User user = userRepository.findByEmail(request.getEmail())
        .orElseThrow(() -> new UserNotFoundException(request.getEmail()));

    if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
      throw new InvalidCredentialsException();
    }

    String token = jwtService.generateToken(user);

    return AuthResponse.builder()
        .token(token)
        .email(user.getEmail())
        .build();
  }

}
