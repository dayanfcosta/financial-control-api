package com.dayanfcosta.financialcontrol.config.security;

import com.dayanfcosta.financialcontrol.user.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
class AuthenticationService implements UserDetailsService {

  private final UserService userService;

  AuthenticationService(final UserService userService) {
    this.userService = userService;
  }

  @Override
  public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
    final var user = userService.findByEmail(username);
    return new UserDetailsImpl(user);
  }


}