package com.dayanfcosta.financialcontrol.config.rest;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.Collection;
import org.springframework.http.HttpStatus;

@JsonInclude(Include.NON_NULL)
public class HttpErrorResponse {

  private int status;
  private String error;
  private String message;
  private Collection<ValidationError> validationErrors;

  private HttpErrorResponse() {
  }

  public HttpErrorResponse(final int status, final String error, final String message, final Collection<ValidationError> validationErrors) {
    this.validationErrors = validationErrors;
    this.message = message;
    this.status = status;
    this.error = error;
  }

  public int getStatus() {
    return status;
  }

  public String getError() {
    return error;
  }

  public String getMessage() {
    return message;
  }

  public Collection<ValidationError> getValidationErrors() {
    return validationErrors;
  }

  public static HttpErrorResponse of(final HttpStatus status, final String message) {
    return new HttpErrorResponse(status.value(), status.getReasonPhrase(), message, null);
  }

  public static HttpErrorResponse of(final HttpStatus status, final String message, final Collection<ValidationError> validationErrors) {
    return new HttpErrorResponse(status.value(), status.getReasonPhrase(), message, validationErrors);
  }
}