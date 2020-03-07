package com.dayanfcosta.financialcontrol.config.security;

import com.dayanfcosta.financialcontrol.auth.TokenService;
import com.dayanfcosta.financialcontrol.commons.HttpErrorResponse;
import com.dayanfcosta.financialcontrol.user.UserProfile;
import com.dayanfcosta.financialcontrol.user.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import java.io.IOException;
import java.util.Collections;
import java.util.Set;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

public class JwtTokenAuthenticationFilter extends OncePerRequestFilter {

  private final JwtConfig jwtConfig;
  private final UserService userService;
  private final TokenService tokenService;
  private final ObjectMapper objectMapper;

  public JwtTokenAuthenticationFilter(final JwtConfig jwtConfig, final TokenService tokenService, final UserService userService,
      final ObjectMapper objectMapper) {
    this.jwtConfig = jwtConfig;
    this.userService = userService;
    this.tokenService = tokenService;
    this.objectMapper = objectMapper;
  }

  @Override
  protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response, final FilterChain chain)
      throws ServletException, IOException {
    try {
      final var token = getToken(request);
      if (tokenService.isValidToken(token)) {
        authenticate(token);
      }
      chain.doFilter(request, response);
    } catch (final ExpiredJwtException ex) {
      createUnauthorizedResponse(response, "Expired token");
      throw ex;
    } catch (final Exception ex) {
      createUnauthorizedResponse(response, "Invalid token");
      throw ex;
    }
  }

  private void authenticate(final String token) {
    final var userId = tokenService.userIdFromToken(token);
    final var user = userService.findById(userId);
    final var authorities = profileToGrantedAuthority(user.getProfile());
    final var authentication = new UsernamePasswordAuthenticationToken(user, null, authorities);
    SecurityContextHolder.getContext().setAuthentication(authentication);
  }

  private Set<GrantedAuthority> profileToGrantedAuthority(final UserProfile profile) {
    final GrantedAuthority authority = profile::name;
    return Collections.singleton(authority);
  }

  private String getToken(final HttpServletRequest request) {
    return request.getHeader(jwtConfig.getHeader());
  }

  private void createUnauthorizedResponse(final HttpServletResponse response, final String message) throws IOException {
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    objectMapper.writeValue(response.getWriter(), HttpErrorResponse.of(HttpStatus.UNAUTHORIZED, message));
  }

}
