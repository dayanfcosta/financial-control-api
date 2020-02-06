package com.dayanfcosta.financialcontrol.commons;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class HttpErrorResponse {

  private final int status;
  private final String message;
  private final HttpErrorCode error;

  @JsonCreator
  private HttpErrorResponse(@JsonProperty final int status, @JsonProperty final HttpErrorCode error, @JsonProperty final String message) {
    this.message = message;
    this.status = status;
    this.error = error;
  }

  public int getStatus() {
    return status;
  }

  public HttpErrorCode getError() {
    return error;
  }

  public String getMessage() {
    return message;
  }

  public static HttpErrorResponse of(final int status, final HttpErrorCode error, final String message) {
    return new HttpErrorResponse(status, error, message);
  }
}