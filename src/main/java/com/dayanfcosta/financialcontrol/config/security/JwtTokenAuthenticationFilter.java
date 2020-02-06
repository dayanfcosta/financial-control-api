package com.dayanfcosta.financialcontrol.config.security;

import com.dayanfcosta.financialcontrol.auth.TokenService;
import com.dayanfcosta.financialcontrol.user.UserProfile;
import com.dayanfcosta.financialcontrol.user.UserService;
import java.io.IOException;
import java.util.Collections;
import java.util.Set;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

public class JwtTokenAuthenticationFilter extends OncePerRequestFilter {

  private final JwtConfig jwtConfig;
  private final UserService userService;
  private final TokenService tokenService;

  public JwtTokenAuthenticationFilter(final JwtConfig jwtConfig, final TokenService tokenService, final UserService userService) {
    this.jwtConfig = jwtConfig;
    this.userService = userService;
    this.tokenService = tokenService;
  }

  @Override
  protected void doFilterInternal(final HttpServletRequest response, final HttpServletResponse request, final FilterChain chain)
      throws ServletException, IOException {
    final var token = getToken(response);
    if (tokenService.isValidToken(token)) {
      authenticate(token);
    }
    chain.doFilter(response, request);
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
    final var header = request.getHeader(jwtConfig.getHeader());
    return isValidHeader(header) ? header.replace(jwtConfig.getPrefix(), "") : null;
  }

  private boolean isValidHeader(final String header) {
    return StringUtils.isNotBlank(header) && header.startsWith(jwtConfig.getPrefix());
  }
}
