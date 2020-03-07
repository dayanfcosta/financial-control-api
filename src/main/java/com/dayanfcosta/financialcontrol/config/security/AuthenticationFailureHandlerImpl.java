package com.dayanfcosta.financialcontrol.config.security;

import com.dayanfcosta.financialcontrol.commons.HttpErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

@Component
class AuthenticationFailureHandlerImpl implements AuthenticationFailureHandler {

  private final ObjectMapper objectMapper;

  AuthenticationFailureHandlerImpl(final ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  @Override
  public void onAuthenticationFailure(final HttpServletRequest request, final HttpServletResponse response,
      final AuthenticationException exception)
      throws IOException {
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);

    if (exception instanceof BadCredentialsException) {
      objectMapper.writeValue(response.getWriter(), createErrorResponse("Bad credentials"));
    } else {
      objectMapper.writeValue(response.getWriter(), createErrorResponse("Invalid authentication"));
    }
  }

  private HttpErrorResponse createErrorResponse(final String message) {
    return HttpErrorResponse.of(HttpStatus.UNAUTHORIZED, message);
  }
}