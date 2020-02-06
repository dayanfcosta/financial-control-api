package com.dayanfcosta.financialcontrol.config.security;

import com.dayanfcosta.financialcontrol.auth.TokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Collections;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

  private final JwtConfig jwtConfig;
  private final TokenService tokenService;
  private final AuthenticationFailureHandler failureHandler;
  private final AuthenticationManager authenticationManager;

  AuthenticationFilter(final JwtConfig jwtConfig, final TokenService tokenService,
      final AuthenticationManager authenticationManager,
      final AuthenticationFailureHandler failureHandler) {
    this.jwtConfig = jwtConfig;
    this.tokenService = tokenService;
    this.failureHandler = failureHandler;
    this.authenticationManager = authenticationManager;
    setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher(jwtConfig.getUri(), "POST"));
  }

  @Override
  public Authentication attemptAuthentication(final HttpServletRequest request, final HttpServletResponse response)
      throws AuthenticationException {
    try {
      final var credentials = new ObjectMapper().readValue(request.getInputStream(), Credentials.class);
      final var authenticationToken = createAuthenticationToken(credentials);
      return authenticationManager.authenticate(authenticationToken);
    } catch (final IOException ex) {
      throw new RuntimeException("An error occur while authentication, try again: ", ex);
    }
  }

  @Override
  protected void successfulAuthentication(final HttpServletRequest request, final HttpServletResponse response,
      final FilterChain chain, final Authentication authResult) {
    final var token = tokenService.generateToken(authResult);
    response.addHeader(jwtConfig.getHeader(), token);
  }

  @Override
  protected void unsuccessfulAuthentication(final HttpServletRequest request, final HttpServletResponse response,
      final AuthenticationException failed) throws IOException, ServletException {
    SecurityContextHolder.clearContext();
    failureHandler.onAuthenticationFailure(request, response, failed);
  }

  private UsernamePasswordAuthenticationToken createAuthenticationToken(final Credentials credentials) {
    return new UsernamePasswordAuthenticationToken(credentials.getUsername(), credentials.getPassword(), Collections.emptyList());
  }

  private class Credentials {

    private String username;
    private String password;

    public String getUsername() {
      return username;
    }

    public String getPassword() {
      return password;
    }

  }
}