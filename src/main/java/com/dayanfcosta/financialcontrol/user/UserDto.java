package com.dayanfcosta.financialcontrol.user;

import static com.dayanfcosta.financialcontrol.commons.Views.Form;
import static com.dayanfcosta.financialcontrol.commons.Views.Listing;
import static com.dayanfcosta.financialcontrol.commons.Views.Select;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;

/**
 * @author dayanfcosta
 */
public class UserDto {

  @JsonView({Listing.class, Form.class, Select.class})
  private final String id;
  @JsonView({Listing.class, Form.class, Select.class})
  private final String name;
  @JsonView({Listing.class, Form.class})
  private final String email;
  private final String password;

  @JsonCreator
  private UserDto(@JsonProperty("id") final String id,
      @JsonProperty("name") final String name,
      @JsonProperty("email") final String email,
      @JsonProperty("password") final String password) {
    this.id = id;
    this.name = name;
    this.email = email;
    this.password = password;
  }

  UserDto(final User user) {
    this(user.getId(), user.getName(), user.getEmail(), user.getPassword());
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
}
