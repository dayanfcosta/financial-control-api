package com.dayanfcosta.financialcontrol.commons;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum HttpErrorCode {

  AUTHENTICATION("Authentication"),
  EXPIRED_TOKEN("Expired Session");

  private final String description;

  HttpErrorCode(final String description) {
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