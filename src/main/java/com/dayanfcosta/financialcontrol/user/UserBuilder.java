package com.dayanfcosta.financialcontrol.user;

import org.apache.commons.lang3.Validate;

/**
 * @author dayanfcosta
 */
final class UserBuilder {

  private String id;
  private String name;
  private String password;
  private final String email;
  private boolean enabled = true;

  private UserBuilder(final String email) {
    this.email = Validate.notBlank(email, "Invalid user e-mail");
  }

  static UserBuilder create(final String email) {
    return new UserBuilder(email);
  }

  UserBuilder withId(final String id) {
    this.id = Validate.notBlank(id, "Invalid user id");
    return this;
  }

  UserBuilder withName(final String name) {
    this.name = Validate.notBlank(name, "Invalid user name");
    return this;
  }

  UserBuilder withPassword(final String password) {
    this.password = Validate.notBlank(password, "Invalid user password");
    return this;
  }

  UserBuilder withEnabledStatus(final boolean enabled) {
    this.enabled = enabled;
    return this;
  }

  User build() {
    Validate.notBlank(name, "Invalid user name");
    Validate.notBlank(password, "Invalid user password");
    return new User(id, name, email, password, enabled);
  }
}
