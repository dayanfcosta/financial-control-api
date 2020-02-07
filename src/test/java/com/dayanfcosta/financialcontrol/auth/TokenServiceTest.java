package com.dayanfcosta.financialcontrol.auth;

import static com.dayanfcosta.financialcontrol.user.UserProfile.ADMINISTRATOR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.dayanfcosta.financialcontrol.config.security.JwtConfig;
import com.dayanfcosta.financialcontrol.user.User;
import com.dayanfcosta.financialcontrol.user.UserBuilder;
import com.dayanfcosta.financialcontrol.user.UserService;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

class TokenServiceTest {

  private JwtConfig jwtConfig;
  private UserService userService;
  private TokenService tokenService;

  private User user;

  @BeforeEach
  void setUp() {
    user = UserBuilder.create("email@email.com")
        .withPassword("User password")
        .withProfile(ADMINISTRATOR)
        .withName("User name")
        .withId("1")
        .build();

    initMocks();
    configureMocks();
  }

  @Test
  void testGenerateToken() {
    final var authentication = new UsernamePasswordAuthenticationToken(user, null);
    final var token = tokenService.generateToken(authentication);

    assertThat(token).isNotNull();
    assertThat(token).isNotEmpty();
    assertThat(token).startsWith(jwtConfig.getPrefix());
  }

  @Test
  void testIsTokenValid() {
    final var authentication = new UsernamePasswordAuthenticationToken(user, null);
    final var token = tokenService.generateToken(authentication);

    final var isValid = tokenService.isValidToken(token);

    assertThat(isValid).isTrue();
  }

  @Test
  void testIsTokenValid_tokenIsNull() {
    final var isValid = tokenService.isValidToken(null);

    assertThat(isValid).isFalse();
  }

  @Test
  void testIsTokenValid_tokenIsBlank() {
    final var isValid = tokenService.isValidToken("");

    assertThat(isValid).isFalse();
  }

  @Test
  void testUserIdFromToken() {
    final var authentication = new UsernamePasswordAuthenticationToken(user, null);
    final var token = tokenService.generateToken(authentication);

    final var userId = tokenService.userIdFromToken(token);

    assertThat(userId).isNotEmpty();
    assertThat(userId).isEqualTo(user.getId());
  }

  private void configureMocks() {
    when(jwtConfig.getExpiration()).thenReturn(8400);
    when(jwtConfig.getPrefix()).thenReturn("Bearer");
    when(userService.findById(any())).thenReturn(user);
    when(jwtConfig.getSecret()).thenReturn("B3RbbSUOj8ff0qjdiW5W");
  }

  private void initMocks() {
    jwtConfig = mock(JwtConfig.class);
    userService = mock(UserService.class);
    final var objectMapper = new ObjectMapper()
        .setVisibility(PropertyAccessor.ALL, Visibility.ANY)
        .setVisibility(PropertyAccessor.CREATOR, Visibility.ANY);
    tokenService = new TokenService(jwtConfig, userService, objectMapper);
  }
}