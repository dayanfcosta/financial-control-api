package com.dayanfcosta.financialcontrol.group;

import com.dayanfcosta.financialcontrol.commons.ResourceUtils;
import com.dayanfcosta.financialcontrol.user.User;
import java.net.URISyntaxException;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/groups")
public class GroupResource {

  private final GroupService service;

  public GroupResource(final GroupService service) {
    this.service = service;
  }

  @GetMapping
  public Page<GroupDto> fromUser(final Authentication authentication, final Pageable pageable) {
    final var page = service.fromUser(currentUser(authentication), pageable);
    final var pageContent = page.getContent().stream().map(GroupDto::new).collect(Collectors.toList());
    return new PageImpl<>(pageContent, pageable, page.getTotalElements());
  }

  @GetMapping("/{id}")
  public GroupDto findById(@RequestParam("id") final String id) {
    final var group = service.findById(id);
    return new GroupDto(group);
  }

  @PostMapping
  public ResponseEntity<GroupDto> save(@RequestBody final GroupDto dto, final Authentication authentication) throws URISyntaxException {
    final var group = service.save(dto, currentUser(authentication));
    return ResponseEntity
        .created(ResourceUtils.uri("/groups", group))
        .body(new GroupDto(group));
  }

  @DeleteMapping
  public void delete(@RequestParam("id") final String id, final Authentication authentication) {
    service.delete(id, currentUser(authentication));
  }

  @PutMapping("/{id}/add-users")
  public void addUsers(@RequestParam("id") final String id, @RequestBody final Set<String> userIds, final Authentication authentication) {
    service.addUsers(id, userIds, currentUser(authentication));
  }

  @PutMapping("/{id}/remove-users")
  public void removeUsers(@RequestParam("id") final String id, @RequestBody final Set<String> userIds,
      final Authentication authentication) {
    service.removeUsers(id, userIds, currentUser(authentication));
  }

  @PutMapping("/{id}/add-transactions")
  public void addTransactions(@RequestParam("id") final String id, @RequestBody final Set<String> transactionIds,
      final Authentication authentication) {
    service.addTransactions(id, transactionIds, currentUser(authentication));
  }

  @PutMapping("/{id}/remove-transactions")
  public void removeTransactions(@RequestParam("id") final String id, @RequestBody final Set<String> transactionIds,
      final Authentication authentication) {
    service.removeTransactions(id, transactionIds, currentUser(authentication));
  }

  @PutMapping("/{id}/leave")
  public void leaveGroup(@RequestParam("id") final String id, final Authentication authentication) {
    service.leave(id, currentUser(authentication));
  }

  private User currentUser(final Authentication authentication) {
    return (User) authentication.getPrincipal();
  }

}

