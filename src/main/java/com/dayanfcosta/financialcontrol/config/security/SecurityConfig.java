package com.dayanfcosta.financialcontrol.config.security;

import com.dayanfcosta.financialcontrol.auth.TokenService;
import com.dayanfcosta.financialcontrol.user.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired
  private JwtConfig jwtConfig;
  @Autowired
  private UserService userService;
  @Autowired
  private ObjectMapper objectMapper;
  @Autowired
  private TokenService tokenService;
  @Autowired
  private PasswordEncoder passwordEncoder;
  @Autowired
  private AuthenticationService authenticationService;
  @Autowired
  private AuthenticationFailureHandler failureHandler;

  @Override
  protected void configure(final HttpSecurity http) throws Exception {
    final var config = jwtConfig;
    http
        .csrf().disable()
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .exceptionHandling().authenticationEntryPoint((req, resp, ex) -> resp.sendError(HttpServletResponse.SC_UNAUTHORIZED))
        .and()
        .addFilter(new AuthenticationFilter(config, tokenService, authenticationManager(), failureHandler))
        .addFilterBefore(new JwtTokenAuthenticationFilter(config, tokenService, userService, objectMapper),
            UsernamePasswordAuthenticationFilter.class)
        .authorizeRequests()
        .antMatchers(HttpMethod.POST, config.getUri()).permitAll()
        .antMatchers("/", "/v3/api-docs", "/v3/api-docs/**", "/configuration/**", "/swagger*/**", "/webjars/**").permitAll()
        .anyRequest().authenticated();
  }

  @Override
  protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(authenticationService).passwordEncoder(passwordEncoder);
  }

}