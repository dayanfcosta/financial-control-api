package com.dayanfcosta.financialcontrol.group;

import com.dayanfcosta.financialcontrol.commons.DuplicateKeyException;
import com.dayanfcosta.financialcontrol.transaction.TransactionService;
import com.dayanfcosta.financialcontrol.user.User;
import com.dayanfcosta.financialcontrol.user.UserService;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import org.apache.commons.lang3.Validate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class GroupService {

  private final UserService userService;
  private final GroupRepository repository;
  private final TransactionService transactionService;

  public GroupService(final GroupRepository repository, final UserService userService, final TransactionService transactionService) {
    this.userService = userService;
    this.repository = repository;
    this.transactionService = transactionService;
  }

  public Group findById(final String id) {
    return repository.findById(id).orElseThrow(() -> new NullPointerException("Group not found"));
  }

  @DuplicateKeyException("You are already owner from a group with the same name")
  Group save(final GroupDto dto, final User owner) {
    Validate.isTrue(dto.getName().isPresent(), "Invalid group name");
    final var users = dto.getUsers().stream().map(userService::findById).collect(Collectors.toSet());
    final var group = GroupBuilder.create(owner, dto.getName().get())
        .withUsers(users)
        .build();
    return repository.save(group);
  }

  void delete(final String groupId, final User user) {
    final var group = findById(groupId);
    Validate.isTrue(group.isOwner(user), "You are not the owner from this group");
    repository.remove(group);
  }

  void addUsers(final String groupId, final Set<String> userIds, final User user) {
    final var group = findById(groupId);
    Validate.isTrue(group.isOwner(user), "You are not the owner from this group");
    final var users = userIds.stream().map(userService::findById).collect(Collectors.toUnmodifiableSet());
    users.forEach(group::addUser);
    repository.save(group);
  }

  void addTransactions(final String groupId, final Set<String> transactionIds, final User user) {
    final var group = findById(groupId);
    validateUserIsMember(user, group);
    final var transactions = transactionIds.stream().map(transactionService::findById).collect(Collectors.toUnmodifiableSet());
    transactions.forEach(group::addTransaction);
    repository.save(group);
  }

  Page<Group> fromUser(final User user, final Pageable pageable) {
    return repository.findByUser(user, pageable);
  }

  void leave(final String groupId, final User user) {
    final var group = findById(groupId);
    validateUserIsMember(user, group);
    if (group.isOwner(user)) {
      final var newOwner = group.getUsers().stream().findFirst();
      newOwner.ifPresentOrElse(changeGroupOwner(group), () -> repository.remove(group));
    } else {
      group.removeUser(user);
      repository.save(group);
    }
  }

  void removeUsers(final String groupId, final Set<String> userIds, final User currentUser) {
    final var group = findById(groupId);
    Validate.isTrue(group.isOwner(currentUser), "You are not the owner from this group");
    final var users = userIds.stream().map(userService::findById).collect(Collectors.toSet());
    users.forEach(group::removeUser);
    repository.save(group);
  }

  void removeTransactions(final String groupId, final Set<String> transactionIds, final User user) {
    final var group = findById(groupId);
    validateUserIsMember(user, group);
    final var users = transactionIds.stream().map(transactionService::findById).collect(Collectors.toSet());
    users.forEach(group::removeTransaction);
    repository.save(group);
  }

  private Consumer<User> changeGroupOwner(final Group group) {
    return newOwner -> {
      group.removeUser(newOwner);
      final var newGroup = GroupBuilder.create(newOwner, group.getName())
          .withTransactions(group.getTransactions())
          .withUsers(group.getUsers())
          .withId(group.getId())
          .build();
      repository.save(newGroup);
    };
  }

  private void validateUserIsMember(final User user, final Group group) {
    final var isMember = group.getUsers().stream().anyMatch(member -> member.equals(user));
    final var isOwner = group.getOwner().equals(user);
    Validate.isTrue(isOwner || isMember, "You are not member from this group");
  }
}
