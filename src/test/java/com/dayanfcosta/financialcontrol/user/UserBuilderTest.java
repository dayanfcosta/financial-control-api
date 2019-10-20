package com.dayanfcosta.financialcontrol.user;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

/**
 * @author dayanfcosta
 */
class UserBuilderTest {

  @Test
  void testCreate_NullEmail() {
    assertThatThrownBy(() -> UserBuilder.create(null))
        .isInstanceOf(NullPointerException.class)
        .hasMessage("Invalid user e-mail");
  }

  @Test
  void testCreate_EmptyEmail() {
    assertThatThrownBy(() -> UserBuilder.create(""))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Invalid user e-mail");
  }

  @Test
  void testCreate_WhitespacesEmail() {
    assertThatThrownBy(() -> UserBuilder.create("    "))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Invalid user e-mail");
  }

  @Test
  void testName_PasswordName() {
    // given
    var builder = UserBuilder.create("email@email.com");

    // then
    assertThatThrownBy(() -> builder.name(""))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Invalid user name");
  }

  @Test
  void testName_NullName() {
    // given
    var builder = UserBuilder.create("email@email.com");

    // then
    assertThatThrownBy(() -> builder.name(null))
        .isInstanceOf(NullPointerException.class)
        .hasMessage("Invalid user name");
  }

  @Test
  void testName_WhitespacesName() {
    // given
    var builder = UserBuilder.create("email@email.com");

    // then
    assertThatThrownBy(() -> builder.name("    "))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Invalid user name");
  }

  @Test
  void testPassword_WhitespacesPassword() {
    // given
    final var builder = UserBuilder.create("email@email.com");

    // then
    assertThatThrownBy(() -> builder.password("    "))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Invalid user password");
  }

  @Test
  void testPassword_EmptyPassword() {
    // given
    var builder = UserBuilder.create("email@email.com");

    // then
    assertThatThrownBy(() -> builder.password(""))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Invalid user password");
  }

  @Test
  void testPassword_NullPassword() {
    // given
    var builder = UserBuilder.create("email@email.com");

    // then
    assertThatThrownBy(() -> builder.password(null))
        .isInstanceOf(NullPointerException.class)
        .hasMessage("Invalid user password");
  }

  @Test
  void testCreate_WithoutId() {
    // given
    var user = UserBuilder.create("email@email.com")
        .name("User name")
        .password("User password")
        .build();

    // then
    assertThat(user.getId()).isEmpty();
    assertThat(user.isEnabled()).isTrue();
  }

  @Test
  void testCreate_DisabledUser() {
    var user = UserBuilder.create("email@email.com")
        .password("User password")
        .name("User name")
        .enabled(false)
        .build();

    // then
    assertThat(user.getId()).isEmpty();
    assertThat(user.isEnabled()).isFalse();
  }

}