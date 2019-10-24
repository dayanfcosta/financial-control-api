package com.dayanfcosta.financialcontrol.user;

import com.dayanfcosta.financialcontrol.commons.ResourceUtils;
import com.dayanfcosta.financialcontrol.commons.Views;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.stream.Collectors;

/**
 * @author dayanfcosta
 */
@RestController
@RequestMapping("/users")
public class UserResource {

  private final UserService service;

  public UserResource(UserService service) {
    this.service = service;
  }

  @GetMapping
  @JsonView(Views.Listing.class)
  public Page<UserDto> findAll(Pageable pageable) {
    var page = service.findAll(pageable);
    var pageContent = page.getContent().stream().map(UserDto::new).collect(Collectors.toList());
    return new PageImpl<>(pageContent, pageable, page.getTotalElements());
  }

  @GetMapping("/{id}")
  @JsonView(Views.Form.class)
  public UserDto findById(@PathVariable("id") String id) {
    var user = service.findById(id);
    return new UserDto(user);
  }

  @PostMapping
  public ResponseEntity<UserDto> save(@RequestBody UserDto dto) throws URISyntaxException {
    var user = service.save(dto);
    return ResponseEntity
        .created(ResourceUtils.uri("users", user))
        .body(new UserDto(user));
  }

  @PutMapping("/{id}")
  public void update(@PathVariable("id") String id, @RequestBody UserDto dto) {
    service.update(id, dto);
  }

}
