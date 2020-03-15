package com.dayanfcosta.financialcontrol.group;

import com.dayanfcosta.financialcontrol.commons.ResourceUtils;
import com.dayanfcosta.financialcontrol.config.openapi.CreateApiResponse;
import com.dayanfcosta.financialcontrol.config.openapi.PageableParameter;
import com.dayanfcosta.financialcontrol.config.openapi.QueryApiResponse;
import com.dayanfcosta.financialcontrol.config.openapi.UpdateApiResponse;
import com.dayanfcosta.financialcontrol.config.rest.HttpErrorResponse;
import com.dayanfcosta.financialcontrol.user.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.net.URISyntaxException;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/groups")
@Tag(name = "Group Resources")
public class GroupResource {

  private final GroupService service;

  public GroupResource(final GroupService service) {
    this.service = service;
  }

  @GetMapping
  @PageableParameter(summary = "Find all groups from the current user")
  public Page<GroupDto> fromUser(@Parameter(hidden = true) final Authentication authentication,
      @Parameter(hidden = true) final Pageable pageable) {
    return service.fromUser(currentUser(authentication), pageable).map(GroupDto::new);
  }

  @GetMapping("/{id}")
  @QueryApiResponse(summary = "Find a group with the given ID")
  public GroupDto findById(@RequestParam("id") final String id) {
    final var group = service.findById(id);
    return new GroupDto(group);
  }

  @PostMapping
  @CreateApiResponse(summary = "Register a new group")
  public ResponseEntity<GroupDto> save(@RequestBody final GroupDto dto, @Parameter(hidden = true) final Authentication authentication)
      throws URISyntaxException {
    final var group = service.save(dto, currentUser(authentication));
    return ResponseEntity
        .created(ResourceUtils.uri("/groups", group))
        .body(new GroupDto(group));
  }

  @DeleteMapping
  @Operation(summary = "Delete a group")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "user deleted"),
      @ApiResponse(responseCode = "401", description = "not authenticated request", content = @Content(schema = @Schema(implementation = HttpErrorResponse.class))),
      @ApiResponse(responseCode = "500", description = "internal server error", content = @Content(schema = @Schema(implementation = HttpErrorResponse.class)))
  })
  public ResponseEntity<Void> delete(@PathVariable("id") final String id, @Parameter(hidden = true) final Authentication authentication) {
    service.delete(id, currentUser(authentication));
    return ResponseEntity.noContent().build();
  }

  @PutMapping("/{id}/add-users")
  @UpdateApiResponse(summary = "Add the users with given IDs to the group with the given ID")
  public void addUsers(@PathVariable("id") final String id, @RequestBody final Set<String> userIds,
      @Parameter(hidden = true) final Authentication authentication) {
    service.addUsers(id, userIds, currentUser(authentication));
  }

  @PutMapping("/{id}/remove-users")
  @UpdateApiResponse(summary = "Remove the users with given IDs from the group with the given ID")
  public void removeUsers(@PathVariable("id") final String id, @RequestBody final Set<String> userIds,
      @Parameter(hidden = true) final Authentication authentication) {
    service.removeUsers(id, userIds, currentUser(authentication));
  }

  @PutMapping("/{id}/add-transactions")
  @UpdateApiResponse(summary = "Add the transactions with the given IDs to the group with the given ID")
  public void addTransactions(@PathVariable("id") final String id, @RequestBody final Set<String> transactionIds,
      @Parameter(hidden = true) final Authentication authentication) {
    service.addTransactions(id, transactionIds, currentUser(authentication));
  }

  @PutMapping("/{id}/remove-transactions")
  @UpdateApiResponse(summary = "Remove the transactions with the given IDs to the group with the given ID")
  public void removeTransactions(@PathVariable("id") final String id, @RequestBody final Set<String> transactionIds,
      @Parameter(hidden = true) final Authentication authentication) {
    service.removeTransactions(id, transactionIds, currentUser(authentication));
  }

  @PutMapping("/{id}/leave")
  @UpdateApiResponse(summary = "Removes the current user from the group with the given ID")
  public void leaveGroup(@PathVariable("id") final String id, @Parameter(hidden = true) final Authentication authentication) {
    service.leave(id, currentUser(authentication));
  }

  private User currentUser(final Authentication authentication) {
    return (User) authentication.getPrincipal();
  }

}

