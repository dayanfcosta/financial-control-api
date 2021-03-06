package com.dayanfcosta.financialcontrol.config.rest;

import io.swagger.v3.oas.annotations.Operation;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class RestExceptionConfig extends ResponseEntityExceptionHandler {

  private static final Logger LOGGER = LoggerFactory.getLogger(RestExceptionConfig.class);

  @Operation(hidden = true)
  @ResponseStatus(HttpStatus.CONFLICT)
  @ExceptionHandler(DataIntegrityViolationException.class)
  public HttpErrorResponse handleConflict(final DataIntegrityViolationException ex) {
    LOGGER.error("An conflict occurred: ", ex);
    return HttpErrorResponse.of(HttpStatus.CONFLICT, ex.getMessage());
  }

  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public HttpErrorResponse handleInternalErrors(final Exception ex) {
    LOGGER.error("An error occurred: ", ex);
    return HttpErrorResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
  }

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException ex, final HttpHeaders headers,
      final HttpStatus status, final WebRequest request) {
    final var validationErrors = ex.getBindingResult().getFieldErrors()
        .stream()
        .map(ValidationError::new)
        .collect(Collectors.toSet());
    final var errorResponse = HttpErrorResponse.of(HttpStatus.BAD_REQUEST, "Validation errors", validationErrors);
    return handleExceptionInternal(ex, errorResponse, headers(), HttpStatus.BAD_REQUEST, request);
  }

  private HttpHeaders headers() {
    final HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.setAcceptCharset(Collections.singletonList(StandardCharsets.UTF_8));
    return headers;
  }
}
