package com.dayanfcosta.financialcontrol.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

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
    final var builder = UserBuilder.create("email@email.com");

    // then
    assertThatThrownBy(() -> builder.withName(""))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Invalid user name");
  }

  @Test
  void testName_NullName() {
    // given
    final var builder = UserBuilder.create("email@email.com");

    // then
    assertThatThrownBy(() -> builder.withName(null))
        .isInstanceOf(NullPointerException.class)
        .hasMessage("Invalid user name");
  }

  @Test
  void testName_WhitespacesName() {
    // given
    final var builder = UserBuilder.create("email@email.com");

    // then
    assertThatThrownBy(() -> builder.withName("    "))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Invalid user name");
  }

  @Test
  void testPassword_WhitespacesPassword() {
    // given
    final var builder = UserBuilder.create("email@email.com");

    // then
    assertThatThrownBy(() -> builder.withPassword("    "))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Invalid user password");
  }

  @Test
  void testPassword_EmptyPassword() {
    // given
    final var builder = UserBuilder.create("email@email.com");

    // then
    assertThatThrownBy(() -> builder.withPassword(""))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Invalid user password");
  }

  @Test
  void testPassword_NullPassword() {
    // given
    final var builder = UserBuilder.create("email@email.com");

    // then
    assertThatThrownBy(() -> builder.withPassword(null))
        .isInstanceOf(NullPointerException.class)
        .hasMessage("Invalid user password");
  }

  @Test
  void testCreate_WithoutId() {
    // given
    final var user = UserBuilder.create("email@email.com")
        .withName("User name")
        .withPassword("User password")
        .build();

    // then
    assertThat(user.getId()).isNullOrEmpty();
    assertThat(user.isEnabled()).isTrue();
  }

  @Test
  void testCreate_DisabledUser() {
    final var user = UserBuilder.create("email@email.com")
        .withPassword("User password")
        .withName("User name")
        .withEnabledStatus(false)
        .build();

    // then
    assertThat(user.getId()).isNullOrEmpty();
    assertThat(user.isEnabled()).isFalse();
  }

}