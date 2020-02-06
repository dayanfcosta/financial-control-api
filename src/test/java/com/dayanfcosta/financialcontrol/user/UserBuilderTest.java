package com.dayanfcosta.financialcontrol.user;

import static com.dayanfcosta.financialcontrol.user.UserProfile.ADMINISTRATOR;
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
    final var builder = UserBuilder.create("email@email.com");

    assertThatThrownBy(() -> builder.withName(""))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Invalid user name");
  }

  @Test
  void testName_NullName() {
    final var builder = UserBuilder.create("email@email.com");

    assertThatThrownBy(() -> builder.withName(null))
        .isInstanceOf(NullPointerException.class)
        .hasMessage("Invalid user name");
  }

  @Test
  void testName_WhitespacesName() {
    final var builder = UserBuilder.create("email@email.com");

    assertThatThrownBy(() -> builder.withName("    "))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Invalid user name");
  }

  @Test
  void testPassword_WhitespacesPassword() {
    final var builder = UserBuilder.create("email@email.com");

    assertThatThrownBy(() -> builder.withPassword("    "))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Invalid user password");
  }

  @Test
  void testPassword_EmptyPassword() {
    final var builder = UserBuilder.create("email@email.com");

    assertThatThrownBy(() -> builder.withPassword(""))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Invalid user password");
  }

  @Test
  void testPassword_NullPassword() {
    final var builder = UserBuilder.create("email@email.com");

    assertThatThrownBy(() -> builder.withPassword(null))
        .isInstanceOf(NullPointerException.class)
        .hasMessage("Invalid user password");
  }

  @Test
  void testCreate_WithoutId() {
    final var user = UserBuilder.create("email@email.com")
        .withName("User name")
        .withPassword("User password")
        .build();

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

    assertThat(user.getId()).isNullOrEmpty();
    assertThat(user.isEnabled()).isFalse();
  }

  @Test
  void testCreate_WithoutProfile() {
    final var user = UserBuilder.create("email@email.com")
        .withPassword("User password")
        .withName("User name")
        .build();

    assertThat(user.getId()).isNullOrEmpty();
    assertThat(user.getProfile()).isEqualTo(UserProfile.USER);
  }

  @Test
  void testCreate_WithProfile() {
    final var user = UserBuilder.create("email@email.com")
        .withPassword("User password")
        .withProfile(ADMINISTRATOR)
        .withName("User name")
        .build();

    assertThat(user.getId()).isNullOrEmpty();
    assertThat(user.getProfile()).isEqualTo(ADMINISTRATOR);
  }

}