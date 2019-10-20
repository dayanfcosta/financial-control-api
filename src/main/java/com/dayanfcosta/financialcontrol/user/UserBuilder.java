package com.dayanfcosta.financialcontrol.user;

import org.apache.commons.lang3.Validate;

/**
 * @author dayanfcosta
 */
public final class UserBuilder {

  private String id;
  private String name;
  private String password;
  private final String email;
  private boolean enabled = true;

  private UserBuilder(String email) {
    this.email = Validate.notBlank(email, "Invalid user e-mail");
  }

  public static UserBuilder create(String email) {
    return new UserBuilder(email);
  }

  public UserBuilder id(String id) {
    this.id = Validate.notBlank(id, "Invalid user id");
    return this;
  }

  public UserBuilder name(String name) {
    this.name = Validate.notBlank(name, "Invalid user name");
    return this;
  }

  public UserBuilder password(String password) {
    this.password = Validate.notBlank(password, "Invalid user password");
    return this;
  }

  public UserBuilder enabled(boolean enabled) {
    this.enabled = enabled;
    return this;
  }

  public User build() {
    Validate.notBlank(name, "Invalid user name");
    Validate.notBlank(password, "Invalid user password");
    return new User(id, name, email, password, enabled);
  }
}
