package com.dayanfcosta.financialcontrol.user;

import java.util.Optional;
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
  private Optional<UserProfile> profile = Optional.empty();

  private UserBuilder(final String email) {
    this.email = Validate.notBlank(email, "Invalid user e-mail");
  }

  public static UserBuilder create(final String email) {
    return new UserBuilder(email);
  }

  public UserBuilder withId(final String id) {
    this.id = Validate.notBlank(id, "Invalid user id");
    return this;
  }

  public UserBuilder withName(final String name) {
    this.name = Validate.notBlank(name, "Invalid user name");
    return this;
  }

  public UserBuilder withPassword(final String password) {
    this.password = Validate.notBlank(password, "Invalid user password");
    return this;
  }

  UserBuilder withEnabledStatus(final boolean enabled) {
    this.enabled = enabled;
    return this;
  }

  public UserBuilder withProfile(final UserProfile profile) {
    this.profile = Optional.ofNullable(profile);
    return this;
  }

  public User build() {
    Validate.notBlank(name, "Invalid user name");
    Validate.notBlank(password, "Invalid user password");
    return new User(id, name, email, password, enabled, profile.orElse(UserProfile.USER));
  }
}
