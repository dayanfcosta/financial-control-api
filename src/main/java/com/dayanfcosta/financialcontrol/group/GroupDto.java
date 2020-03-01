package com.dayanfcosta.financialcontrol.group;

import com.dayanfcosta.financialcontrol.user.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GroupDto {

  private final String id;
  private final List<String> users;
  private final Optional<String> name;
  private final LocalDateTime creationDate;

  public GroupDto(@JsonProperty final String id, @JsonProperty final Optional<String> name, @JsonProperty final List<String> users,
      @JsonProperty final LocalDateTime creationDate) {
    this.id = id;
    this.name = name;
    this.users = users;
    this.creationDate = creationDate;
  }

  GroupDto(final Group group) {
    this(group.getId(), Optional.of(group.getName()), group.getUsers().stream().map(User::getId).collect(Collectors.toList()),
        group.getCreationDate());
  }

  public String getId() {
    return id;
  }

  public Optional<String> getName() {
    return name;
  }

  public List<String> getUsers() {
    return users;
  }

  public LocalDateTime getCreationDate() {
    return creationDate;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("name", name)
        .add("users", users)
        .toString();
  }
}
