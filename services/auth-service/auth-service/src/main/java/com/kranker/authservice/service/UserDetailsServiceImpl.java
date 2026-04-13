package com.kranker.authservice.service;

import com.kranker.authservice.mappers.UserDetailsMapper;
import com.kranker.authservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
@SuppressWarnings("NullMarked")
public class UserDetailsServiceImpl implements UserDetailsService {

  private final UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    return userRepository.findByEmail(email)
        .map(UserDetailsMapper::map) // 👈 см. следующий шаг
        .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
  }
}