package com.dayanfcosta.financialcontrol.commons;

import org.springframework.http.HttpStatus;

public class HttpErrorResponse {

  private int status;
  private String error;
  private String message;

  private HttpErrorResponse() {
  }

  public HttpErrorResponse(final int status, final String error, final String message) {
    this.status = status;
    this.error = error;
    this.message = message;
  }

  public static HttpErrorResponse of(final HttpStatus status, final String message) {
    return new HttpErrorResponse(status.value(), status.getReasonPhrase(), message);
  }
}