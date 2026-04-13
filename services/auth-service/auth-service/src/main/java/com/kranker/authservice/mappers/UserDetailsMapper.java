package com.kranker.authservice.mappers;

import com.kranker.authservice.entity.User;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collections;

public class UserDetailsMapper {

  public static UserDetails map(User user) {
    return new org.springframework.security.core.userdetails.User(
        user.getEmail(),
        user.getPassword(),
        // Пока одна роль, позже можно распарсить из БД
        Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole()))
    );
  }
}