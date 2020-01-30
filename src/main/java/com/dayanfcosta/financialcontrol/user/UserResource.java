package com.dayanfcosta.financialcontrol.user;

import com.dayanfcosta.financialcontrol.commons.ResourceUtils;
import com.dayanfcosta.financialcontrol.commons.Views;
import com.fasterxml.jackson.annotation.JsonView;
import java.net.URISyntaxException;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
public class UserResource {

  private final UserService service;

  public UserResource(final UserService service) {
    this.service = service;
  }

  @GetMapping
  @JsonView(Views.Listing.class)
  public Page<UserDto> findAll(final Pageable pageable) {
    final var page = service.findAll(pageable);
    final var pageContent = page.getContent().stream().map(UserDto::new).collect(Collectors.toList());
    return new PageImpl<>(pageContent, pageable, page.getTotalElements());
  }

  @GetMapping("/{id}")
  @JsonView(Views.Form.class)
  public UserDto findById(@PathVariable("id") final String id) {
    final var user = service.findById(id);
    return new UserDto(user);
  }

  @PostMapping
  @JsonView(Views.Form.class)
  public ResponseEntity<UserDto> save(@RequestBody final UserDto dto) throws URISyntaxException {
    final var user = service.save(dto);
    return ResponseEntity
        .created(ResourceUtils.uri("users", user))
        .body(new UserDto(user));
  }

  @PutMapping("/{id}")
  public void update(@PathVariable("id") final String id, @RequestBody final UserDto dto) {
    service.update(id, dto);
  }

  @PutMapping("/{id}/enable")
  @JsonView(Views.Form.class)
  public UserDto enable(@PathVariable("id") final String id) {
    final var user = service.enable(id);
    return new UserDto(user);
  }

  @PutMapping("/{id}/disable")
  @JsonView(Views.Form.class)
  public UserDto disable(@PathVariable("id") final String id) {
    final User user = service.disable(id);
    return new UserDto(user);
  }

}
