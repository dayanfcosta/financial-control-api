package com.dayanfcosta.financialcontrol.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;

import static com.dayanfcosta.financialcontrol.commons.Views.*;

/**
 * @author dayanfcosta
 */
class UserDto {

  @JsonView({Listing.class, Form.class, Select.class})
  private final String id;
  @JsonView({Listing.class, Form.class, Select.class})
  private final String name;
  @JsonView({Listing.class, Form.class})
  private final String email;
  private final String password;

  @JsonCreator
  public UserDto(@JsonProperty("id") String id,
                 @JsonProperty("name") String name,
                 @JsonProperty("email") String email,
                 @JsonProperty("password") String password) {
    this.id = id;
    this.name = name;
    this.email = email;
    this.password = password;
  }

  public UserDto(User user) {
    this(user.getId(), user.getName(), user.getEmail(), user.getPassword());
  }

  public String getId() {
    return id;
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
}
