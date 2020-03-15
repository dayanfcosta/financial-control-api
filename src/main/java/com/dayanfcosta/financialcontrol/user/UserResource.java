package com.dayanfcosta.financialcontrol.user;

import com.dayanfcosta.financialcontrol.commons.ResourceUtils;
import com.dayanfcosta.financialcontrol.config.openapi.CreateApiResponse;
import com.dayanfcosta.financialcontrol.config.openapi.PageableParameter;
import com.dayanfcosta.financialcontrol.config.openapi.QueryApiResponse;
import com.dayanfcosta.financialcontrol.config.openapi.UpdateApiResponse;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.net.URISyntaxException;
import javax.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author dayanfcosta
 */
@RestController
@RequestMapping("/users")
@Tag(name = "User Resources")
public class UserResource {

  private final UserService service;

  public UserResource(final UserService service) {
    this.service = service;
  }

  @GetMapping
  @PageableParameter(summary = "Find all users paged")
  public Page<UserDto> findAll(@Parameter(hidden = true) final Pageable pageable) {
    return service.findAll(pageable).map(UserDto::new);
  }

  @GetMapping("/{id}")
  @QueryApiResponse(summary = "Find user by id")
  public UserDto findById(@Parameter @PathVariable("id") final String id) {
    final var user = service.findById(id);
    return new UserDto(user);
  }

  @PostMapping
  @CreateApiResponse(summary = "Register a new user")
  public ResponseEntity<UserDto> save(@Valid @RequestBody final UserForm form) throws URISyntaxException {
    final var user = service.save(form);
    return ResponseEntity
        .created(ResourceUtils.uri("users", user))
        .body(new UserDto(user));
  }

  @PutMapping("/{id}")
  @UpdateApiResponse(summary = "Update an user given an id")
  public void update(@PathVariable("id") final String id, @RequestBody final UserDto dto) {
    service.update(id, dto);
  }

  @PutMapping("/{id}/enable")
  @UpdateApiResponse(summary = "Enable the user with the given ID")
  public UserDto enable(@PathVariable("id") final String id) {
    final var user = service.enable(id);
    return new UserDto(user);
  }

  @PutMapping("/{id}/disable")
  @UpdateApiResponse(summary = "Disable the user with the given ID")
  public UserDto disable(@PathVariable("id") final String id) {
    final User user = service.disable(id);
    return new UserDto(user);
  }

}
