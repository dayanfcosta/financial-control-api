package com.dayanfcosta.financialcontrol.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.base.MoreObjects;

/**
 * @author dayanfcosta
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDto {

  private String id;
  private String name;
  private String email;
  @JsonIgnore
  private String password;

  private UserDto() {
  }

  UserDto(final User user) {
    id = user.getId();
    name = user.getName();
    email = user.getEmail();
    password = user.getPassword();
  }

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  String getEmail() {
    return email;
  }

  String getPassword() {
    return password;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("id", id)
        .add("name", name)
        .add("email", email)
        .toString();
  }
}
