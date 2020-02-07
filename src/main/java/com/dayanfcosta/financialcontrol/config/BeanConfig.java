package com.dayanfcosta.financialcontrol.config;

import com.dayanfcosta.financialcontrol.config.security.JwtConfig;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class BeanConfig {

  @Bean
  public ObjectMapper objectMapper() {
    return new ObjectMapper()
        .setVisibility(PropertyAccessor.ALL, Visibility.ANY)
        .setVisibility(PropertyAccessor.CREATOR, Visibility.ANY);
  }

  @Bean
  public BCryptPasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public JwtConfig jwtConfig() {
    return new JwtConfig();
  }

}
