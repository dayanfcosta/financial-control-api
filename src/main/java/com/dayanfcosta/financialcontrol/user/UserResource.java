package com.dayanfcosta.financialcontrol.user;

import com.dayanfcosta.financialcontrol.commons.ResourceUtils;
import com.dayanfcosta.financialcontrol.config.rest.HttpErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
  @Operation(summary = "Find all users paged")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "successful operation"),
      @ApiResponse(responseCode = "401", description = "not authenticated request"),
      @ApiResponse(responseCode = "500", description = "internal server error"),
  })
  public Page<UserDto> findAll(final Pageable pageable) {
    return service.findAll(pageable).map(UserDto::new);
  }

  @GetMapping("/{id}")
  @Operation(summary = "Find user by id")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "successful operation"),
      @ApiResponse(responseCode = "401", description = "not authenticated request"),
      @ApiResponse(responseCode = "500", description = "internal server error"),
  })
  public UserDto findById(@Parameter @PathVariable("id") final String id) {
    final var user = service.findById(id);
    return new UserDto(user);
  }

  @PostMapping
  @Operation(summary = "Register a new user")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "user created", content = @Content(schema = @Schema(implementation = UserDto.class))),
      @ApiResponse(responseCode = "401", description = "not authenticated request", content = @Content(schema = @Schema(implementation = HttpErrorResponse.class))),
      @ApiResponse(responseCode = "409", description = "email already in use", content = @Content(schema = @Schema(implementation = HttpErrorResponse.class))),
      @ApiResponse(responseCode = "500", description = "internal server error", content = @Content(schema = @Schema(implementation = HttpErrorResponse.class)))
  })
  public ResponseEntity<UserDto> save(@Valid @RequestBody final UserForm form) throws URISyntaxException {
    final var user = service.save(form);
    return ResponseEntity
        .created(ResourceUtils.uri("users", user))
        .body(new UserDto(user));
  }

  @PutMapping("/{id}")
  @Operation(summary = "Update an user given an id")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "user updated"),
      @ApiResponse(responseCode = "401", description = "not authenticated request", content = @Content(schema = @Schema(implementation = HttpErrorResponse.class))),
      @ApiResponse(responseCode = "500", description = "internal server error", content = @Content(schema = @Schema(implementation = HttpErrorResponse.class)))
  })
  public void update(@PathVariable("id") final String id, @RequestBody final UserDto dto) {
    service.update(id, dto);
  }

  @PutMapping("/{id}/enable")
  @Operation(summary = "Enable the user with the given ID")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "user enabled", content = @Content(schema = @Schema(implementation = UserDto.class))),
      @ApiResponse(responseCode = "401", description = "not authenticated request", content = @Content(schema = @Schema(implementation = HttpErrorResponse.class))),
      @ApiResponse(responseCode = "500", description = "internal server error", content = @Content(schema = @Schema(implementation = HttpErrorResponse.class)))
  })
  public UserDto enable(@PathVariable("id") final String id) {
    final var user = service.enable(id);
    return new UserDto(user);
  }

  @PutMapping("/{id}/disable")
  @Operation(summary = "Disable the user with the given ID")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "user disabled", content = @Content(schema = @Schema(implementation = UserDto.class))),
      @ApiResponse(responseCode = "401", description = "not authenticated request", content = @Content(schema = @Schema(implementation = HttpErrorResponse.class))),
      @ApiResponse(responseCode = "500", description = "internal server error", content = @Content(schema = @Schema(implementation = HttpErrorResponse.class)))
  })
  public UserDto disable(@PathVariable("id") final String id) {
    final User user = service.disable(id);
    return new UserDto(user);
  }

}
