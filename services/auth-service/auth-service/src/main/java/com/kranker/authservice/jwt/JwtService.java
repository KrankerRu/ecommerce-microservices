package com.kranker.authservice.jwt;

import com.kranker.authservice.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.function.Function;

@Service
@FieldDefaults(makeFinal = true)
public class JwtService {

  // ToDo потом вынеси в application.properties!
  private static final String SECRET_KEY = "your-very-secret-key-that-should-be-at-least-256-bits-long!";
  private static final long TOKEN_EXPIRATION = 24 * 60 * 60 * 1000;

  private final SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

  public String generateToken(User user) {
    return Jwts.builder()
        .subject(user.getEmail())
        .issuedAt(Date.from(Instant.now()))
        .expiration(Date.from(Instant.now().plusMillis(TOKEN_EXPIRATION)))
        .signWith(key)
        .compact();
  }

  private Claims extractAllClaims(String token) {
    return Jwts.parser()
        .verifyWith(key)
        .build()
        .parseSignedClaims(token)
        .getPayload();
  }

  public String extractUsername(String token) {
    return extractClaim(token, Claims::getSubject);
  }

  private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    return claimsResolver.apply(extractAllClaims(token));
  }

  private boolean isTokenExpired(String token) {
    return extractClaim(token, Claims::getExpiration)
        .before(Date.from(Instant.now()));
  }

  public boolean isTokenValid(String token, UserDetails userDetails) {
    final String username = extractUsername(token);
    return !isTokenExpired(token) && username.equals(userDetails.getUsername());
  }
}