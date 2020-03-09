package com.dayanfcosta.financialcontrol.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.base.MoreObjects;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserForm {

  @NotBlank(message = "Invalid user name")
  private String name;
  @NotBlank(message = "Invalid user email")
  @Email(message = "Invalid user email")
  private String email;
  @NotBlank(message = "Invalid user password")
  private String password;

  private UserForm() {
  }

  public UserForm(final String name, final String email, final String password) {
    this.name = name;
    this.email = email;
    this.password = password;
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

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("name", name)
        .add("email", email)
        .toString();
  }
}
