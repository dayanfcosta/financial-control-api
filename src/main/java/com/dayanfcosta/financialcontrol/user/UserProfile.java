package com.dayanfcosta.financialcontrol.user;

public enum UserProfile {

  USER("User"),
  ADMINISTRATOR("Administrator");

  private final String description;

  UserProfile(final String description) {
    this.description = description;
  }

  public String getDescription() {
    return description;
  }

  @Override
  public String toString() {
    return description;
  }
}
