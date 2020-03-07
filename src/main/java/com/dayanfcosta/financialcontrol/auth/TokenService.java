package com.dayanfcosta.financialcontrol.auth;

import com.dayanfcosta.financialcontrol.config.security.JwtConfig;
import com.dayanfcosta.financialcontrol.config.security.UserDetailsImpl;
import com.dayanfcosta.financialcontrol.user.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class TokenService {

  private final JwtConfig jwtConfig;
  private final UserService userService;
  private final ObjectMapper objectMapper;

  public TokenService(final JwtConfig jwtConfig, final UserService userService, final ObjectMapper objectMapper) {
    this.jwtConfig = jwtConfig;
    this.userService = userService;
    this.objectMapper = objectMapper;
  }

  public String generateToken(final Authentication authentication) {
    final var userId = ((UserDetailsImpl) authentication.getPrincipal()).getId();
    final var user = userService.findById(userId);
    final String token = token(new AuthenticatedUser(user));
    return String.format("%s %s", jwtConfig.getPrefix(), token);
  }

  public boolean isValidToken(final String token) {
    if (StringUtils.isNotBlank(token)) {
      getTokenBody(token);
      return true;
    }
    return false;
  }

  public String userIdFromToken(final String token) {
    try {
      final var subject = getTokenBody(token).getSubject();
      final var authenticatedUser = objectMapper.readValue(subject, AuthenticatedUser.class);
      return authenticatedUser.getId();
    } catch (final JsonProcessingException ex) {
      throw new RuntimeException("Error converting jwt token to AuthenticatedUser: ", ex);
    }
  }

  private Claims getTokenBody(final String token) {
    final var tokenWithoutPrefix = removeTokenPrefix(token);
    return Jwts.parser()
        .setSigningKey(jwtConfig.getSecret())
        .parseClaimsJws(tokenWithoutPrefix)
        .getBody();
  }

  private String token(final AuthenticatedUser authenticatedUser) {
    try {
      final var subject = objectMapper.writeValueAsString(authenticatedUser);
      return Jwts.builder()
          .claim("authorities", authenticatedUser.grantedAuthority())
          .signWith(SignatureAlgorithm.HS256, jwtConfig.getSecret())
          .setExpiration(getTokenExpirationDate())
          .setIssuer("Financial Control API")
          .setSubject(subject)
          .compact();
    } catch (final Exception ex) {
      throw new RuntimeException("Error generating jwt token", ex);
    }
  }

  private Date getTokenExpirationDate() {
    final var expirationDate = LocalDateTime.now().plusSeconds(jwtConfig.getExpiration());
    final var instant = expirationDate.atZone(ZoneId.systemDefault()).toInstant();
    return Date.from(instant);
  }

  private String removeTokenPrefix(final String token) {
    return token.replace(jwtConfig.getPrefix(), "").trim();
  }

}