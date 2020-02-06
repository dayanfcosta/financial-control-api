package com.dayanfcosta.financialcontrol.user;

import com.dayanfcosta.financialcontrol.commons.AbstractDocument;
import org.apache.commons.lang3.Validate;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author dayanfcosta
 */
@Document(value = "users")
public class User extends AbstractDocument {

  private final String name;
  private boolean enabled;
  @Indexed(unique = true, name = "uk_user")
  private final String email;
  private final String password;
  private final UserProfile profile;

  @PersistenceConstructor
  User(final String id, final String name, final String email, final String password, final boolean enabled, final UserProfile profile) {
    super(id);
    this.name = Validate.notBlank(name, "Invalid user name");
    this.email = Validate.notBlank(email, "invalid user e-mail");
    this.password = Validate.notBlank(password, "Invalid user password");
    this.enabled = enabled;
    this.profile = Validate.notNull(profile, "Invalid user profile");
  }

  public String getName() {
    return name;
  }

  public String getEmail() {
    return email;
  }

  public String getPassword() {
    return password;
  }

  public boolean isEnabled() {
    return enabled;
  }

  public void enable() {
    enabled = true;
  }

  public void disable() {
    enabled = false;
  }

  public UserProfile getProfile() {
    return profile;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    final User user = (User) o;
    return email.equals(user.email);
  }

  @Override
  public int hashCode() {
    return email.hashCode();
  }

  @Override
  public String toString() {
    final StringBuilder builder = new StringBuilder("User{");
    builder.append("name='").append(name).append('\'');
    builder.append(", enabled=").append(enabled);
    builder.append(", email='").append(email).append('\'');
    builder.append('}');
    return builder.toString();
  }

}
