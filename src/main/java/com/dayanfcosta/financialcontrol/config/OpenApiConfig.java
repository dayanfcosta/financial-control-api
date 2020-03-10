package com.dayanfcosta.financialcontrol.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.headers.Header;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.security.OAuthFlow;
import io.swagger.v3.oas.models.security.OAuthFlows;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityScheme.In;
import io.swagger.v3.oas.models.security.SecurityScheme.Type;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

  private static final String AUTHORIZATION_HEADER = "Authorization";

  @Bean
  public OpenAPI api() {
    return new OpenAPI()
        .components(
            new Components().addSecuritySchemes("bearer", securityScheme())
                .addParameters(AUTHORIZATION_HEADER, authorizationHeaderScheme())
                .addHeaders(AUTHORIZATION_HEADER, authorizationHeader())
        )
        .info(metaData());
  }

  private Parameter authorizationHeaderScheme() {
    return new Parameter().in("header").schema(new StringSchema()).name(AUTHORIZATION_HEADER);
  }

  private Header authorizationHeader() {
    return new Header().description(AUTHORIZATION_HEADER + " header").schema(new StringSchema());
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

  private SecurityScheme securityScheme() {
    return new SecurityScheme()
        .name("jwt-authentication")
        .flows(new OAuthFlows().implicit(new OAuthFlow().authorizationUrl("/login")))
        .bearerFormat("jwt")
        .scheme("bearer")
        .type(Type.HTTP)
        .in(In.HEADER);
  }

}
