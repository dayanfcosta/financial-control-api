package com.dayanfcosta.financialcontrol.config.rest;

import org.springframework.validation.FieldError;

public class ValidationError {

  private String field;
  private String error;

  private ValidationError() {
  }

  public ValidationError(final FieldError error) {
    field = error.getField();
    this.error = error.getDefaultMessage();
  }

  public String getField() {
    return field;
  }

  public String getError() {
    return error;
  }

}