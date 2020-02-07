package com.dayanfcosta.financialcontrol.config.security;

import org.springframework.beans.factory.annotation.Value;

public class JwtConfig {

  @Value("${security.jwt.uri:/auth/**}")
  private String uri;
  @Value("${security.jwt.header:Authorization}")
  private String header;
  @Value("${security.jwt.expiration:8400}")
  private int expiration;
  @Value("${security.jwt.secret:B3RbbSUOj8ff0qjdiW5W}")
  private String secret;
  @Value("${security.jwt.prefix:Bearer}")
  private String prefix;

  public String getUri() {
    return uri;
  }

  public String getHeader() {
    return header;
  }

  public int getExpiration() {
    return expiration;
  }

  public String getSecret() {
    return secret;
  }

  public String getPrefix() {
    return prefix;
  }
}