package com.dayanfcosta.financialcontrol.config.security;

import com.dayanfcosta.financialcontrol.auth.TokenService;
import com.dayanfcosta.financialcontrol.user.UserService;
import javax.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  private final UserService userService;
  private final TokenService tokenService;
  private final AuthenticationService authenticationService;
  private final AuthenticationFailureHandler failureHandler;

  public SecurityConfig(final UserService userService, final TokenService tokenService, final AuthenticationService authenticationService,
      final AuthenticationFailureHandler failureHandler) {
    this.userService = userService;
    this.tokenService = tokenService;
    this.authenticationService = authenticationService;
    this.failureHandler = failureHandler;
  }

  @Override
  protected void configure(final HttpSecurity http) throws Exception {
    final var config = jwtConfig();
    http
        .csrf().disable()
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .exceptionHandling().authenticationEntryPoint((req, resp, ex) -> resp.sendError(HttpServletResponse.SC_UNAUTHORIZED))
        .and()
        .addFilter(new AuthenticationFilter(config, tokenService, authenticationManager(), failureHandler))
        .addFilterBefore(new JwtTokenAuthenticationFilter(config, tokenService, userService), UsernamePasswordAuthenticationFilter.class)
        .authorizeRequests()
        .antMatchers(HttpMethod.POST, config.getUri()).permitAll()
        .anyRequest().authenticated();
  }

  @Override
  protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(authenticationService).passwordEncoder(passwordEncoder());
  }

  @Bean
  BCryptPasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  JwtConfig jwtConfig() {
    return new JwtConfig();
  }

}