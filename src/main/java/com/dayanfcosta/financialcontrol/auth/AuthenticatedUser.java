package com.dayanfcosta.financialcontrol.auth;

import com.dayanfcosta.financialcontrol.user.User;
import java.util.Collections;
import java.util.Set;
import org.springframework.security.core.GrantedAuthority;

class AuthenticatedUser {

  private String id;
  private String name;
  private String profile;
  private boolean enabled;

  AuthenticatedUser() {
  }

  AuthenticatedUser(final User user) {
    id = user.getId();
    name = user.getName();
    enabled = user.isEnabled();
    profile = user.getProfile().name();
  }

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public Set<GrantedAuthority> grantedAuthority() {
    return Collections.singleton(this::getProfile);
  }

  public String getProfile() {
    return profile;
  }

  public boolean isEnabled() {
    return enabled;
  }
}