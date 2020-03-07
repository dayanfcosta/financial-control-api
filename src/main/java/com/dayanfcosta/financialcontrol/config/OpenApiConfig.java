package com.dayanfcosta.financialcontrol.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

  @Bean
  public OpenAPI api() {
    return new OpenAPI()
        .components(new Components())
        .info(metaData());
  }

  private Info metaData() {
    return new Info()
        .title("Financial Control REST API")
        .description("\"API for Financial Control Application\"")
        .version("1.0.0")
        .license(license());
  }

  private License license() {
    return new License()
        .name("Apache License Version 2.0")
        .url("https://www.apache.org/licenses/LICENSE-2.0");
  }

}
