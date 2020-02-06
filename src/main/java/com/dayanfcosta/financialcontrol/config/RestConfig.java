package com.dayanfcosta.financialcontrol.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RestConfig {

  @Bean
  public ObjectMapper objectMapper() {
    return new ObjectMapper()
        .setVisibility(PropertyAccessor.ALL, Visibility.ANY)
        .setVisibility(PropertyAccessor.CREATOR, Visibility.ANY);
  }

}
