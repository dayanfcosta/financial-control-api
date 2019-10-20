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

  @PersistenceConstructor
  User(String id, String name, String email, String password, boolean enabled) {
    super(id);
    this.name = Validate.notBlank(name, "Invalid user name");
    this.email = Validate.notBlank(email, "invalid user e-mail");
    this.password = Validate.notBlank(password, "Invalid user password");
    this.enabled = enabled;
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

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;
    User user = (User) o;
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
