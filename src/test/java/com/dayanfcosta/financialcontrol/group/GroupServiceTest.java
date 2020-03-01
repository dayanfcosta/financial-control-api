package com.dayanfcosta.financialcontrol.group;

import static com.dayanfcosta.financialcontrol.transaction.Currency.EUR;
import static com.dayanfcosta.financialcontrol.transaction.TransactionType.INCOME;
import static java.math.BigDecimal.ONE;
import static java.time.LocalDateTime.now;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.dayanfcosta.financialcontrol.transaction.Transaction;
import com.dayanfcosta.financialcontrol.transaction.TransactionBuilder;
import com.dayanfcosta.financialcontrol.transaction.TransactionService;
import com.dayanfcosta.financialcontrol.user.User;
import com.dayanfcosta.financialcontrol.user.UserBuilder;
import com.dayanfcosta.financialcontrol.user.UserService;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.stubbing.Answer;

class GroupServiceTest {

  private UserService userService;
  private GroupService groupService;
  private GroupRepository repository;
  private TransactionService transactionService;

  private User user;

  @BeforeEach
  void setUp() {
    userService = mock(UserService.class);
    repository = mock(GroupRepository.class);
    transactionService = mock(TransactionService.class);
    groupService = new GroupService(repository, userService, transactionService);

    user = UserBuilder.create("email@email.com")
        .withPassword("pass")
        .withName("User")
        .build();
  }

  @Test
  void testSave() {
    when(userService.findById(any())).thenReturn(user);

    final var dto = new GroupDto("", of("group"), asList("1"), now());

    groupService.save(dto, user);

    verify(userService, times(1)).findById(any());
    verify(repository, times(1)).save(any());
  }


  @Test
  void testSave_WithoutUsers() {
    final var dto = new GroupDto("", of("group"), emptyList(), now());

    groupService.save(dto, user);

    verify(repository, times(1)).save(any());
    verify(userService, never()).findById(any());
  }

  @Test
  void testSave_MissingName() {
    final var dto = new GroupDto("", empty(), emptyList(), now());

    assertThatIllegalArgumentException()
        .isThrownBy(() -> groupService.save(dto, user))
        .withMessage("Invalid group name");
    verify(repository, never()).save(any());
    verify(userService, never()).findById(any());
  }

  @Test
  void testDelete() {
    when(repository.findById(any())).thenReturn(of(GroupBuilder.create(user, "12312").build()));

    groupService.delete("1", user);

    verify(repository, times(1)).findById(any());
    verify(repository, times(1)).remove(any());
  }


  @Test
  void testDelete_UserNotOwner() {
    final var owner = UserBuilder.create("1@1.com").withPassword("1").withName("1").build();
    when(repository.findById(any())).thenReturn(of(GroupBuilder.create(owner, "12312").build()));

    assertThatIllegalArgumentException()
        .isThrownBy(() -> groupService.delete("1", user))
        .withMessage("You are not the owner from this group");
    verify(repository, times(1)).findById(any());
    verify(repository, never()).remove(any());
  }

  @Test
  void testAddUsers() {
    when(userService.findById(any())).then(user());
    when(repository.findById(any())).thenReturn(of(GroupBuilder.create(user, "12312").build()));

    groupService.addUsers("1", Set.of("4", "3"), user);

    verify(userService, times(2)).findById(any());
    verify(repository, times(1)).findById(any());
    verify(repository, times(1)).save(any());
  }

  @Test
  void testAddUsers_InvalidGroup() {
    when(repository.findById(any())).thenReturn(empty());

    assertThatNullPointerException()
        .isThrownBy(() -> groupService.addUsers("1", Set.of("1", "2"), user))
        .withMessage("Group not found");

    verify(repository, only()).findById(any());
  }

  @Test
  void testAddUsers_InvalidOwner() {
    final var owner = UserBuilder.create("1@1.com").withPassword("1").withName("1").build();
    when(repository.findById(any())).thenReturn(of(GroupBuilder.create(owner, "12312").build()));

    assertThatIllegalArgumentException()
        .isThrownBy(() -> groupService.addUsers("1", Set.of("1", "2"), user))
        .withMessage("You are not the owner from this group");

    verify(repository, only()).findById(any());
  }

  @Test
  void testAddTransactions() {
    when(transactionService.findById(any())).then(transaction());
    when(repository.findById(any())).thenReturn(of(GroupBuilder.create(user, "12312").build()));

    groupService.addTransactions("1", Set.of("1", "2", "3"), user);

    verify(transactionService, times(3)).findById(any());
    verify(repository, times(1)).findById(any());
    verify(repository, times(1)).save(any());
  }

  @Test
  void testAddTransactions_UserNotGroupMember() {
    when(transactionService.findById(any())).then(transaction());
    when(repository.findById(any())).thenReturn(of(GroupBuilder.create(user, "12312").build()));

    final var notMember = UserBuilder.create("id").withPassword("id").withName("id").build();
    assertThatIllegalArgumentException()
        .isThrownBy(() -> groupService.addTransactions("1", Set.of("1", "2", "3"), notMember))
        .withMessage("You are not member from this group");

    verify(repository, only()).findById(any());
  }

  @Test
  void testLeave_OwnerWithoutMembers() {
    when(repository.findById(any())).thenReturn(of(GroupBuilder.create(user, "g").build()));

    groupService.leave("1", user);

    verify(repository, times(1)).findById(any());
    verify(repository, times(1)).remove(any());
  }

  @Test
  void testLeave_OwnerWithMembers() {
    final var member = UserBuilder.create("1@1.com").withPassword("1").withName("1").build();
    final var users = new HashSet<>(singletonList(member));
    when(repository.findById(any())).thenReturn(of(GroupBuilder.create(user, "g").withUsers(users).build()));

    groupService.leave("1", user);

    final var captor = ArgumentCaptor.forClass(Group.class);
    verify(repository, times(1)).save(captor.capture());
    assertThat(captor.getValue().getUsers()).isNullOrEmpty();
    verify(repository, times(1)).findById(any());
    verify(repository, never()).remove(any());
  }

  @Test
  void testLeave_UserMember() {
    final var member = UserBuilder.create("1@1.com").withPassword("1").withName("1").build();
    final var users = new HashSet<>(singletonList(member));
    when(repository.findById(any())).thenReturn(of(GroupBuilder.create(user, "g").withUsers(users).build()));

    groupService.leave("1", member);

    final var captor = ArgumentCaptor.forClass(Group.class);
    verify(repository, times(1)).save(captor.capture());
    assertThat(captor.getValue().getUsers()).isNullOrEmpty();
    verify(repository, times(1)).findById(any());
    verify(repository, never()).remove(any());
  }

  @Test
  void testRemoveUsers() {
    final var member = UserBuilder.create("1").withPassword("1").withName("1").build();
    final var users = new HashSet<>(singletonList(member));
    when(userService.findById(any())).then(user());
    when(repository.findById(any())).thenReturn(of(GroupBuilder.create(user, "g").withUsers(users).build()));

    groupService.removeUsers("1", Set.of("1"), user);

    final var captor = ArgumentCaptor.forClass(Group.class);
    verify(repository, times(1)).save(captor.capture());
    assertThat(captor.getValue().getUsers()).isNullOrEmpty();
    verify(repository, times(1)).findById(any());
    verify(userService, times(1)).findById(any());
  }

  @Test
  void testRemoveUsers_UserNotOwner() {
    final var member = UserBuilder.create("1").withPassword("1").withName("1").build();
    when(repository.findById(any())).thenReturn(of(GroupBuilder.create(user, "g").build()));
    when(userService.findById(any())).thenReturn(member);

    assertThatIllegalArgumentException()
        .isThrownBy(() -> groupService.removeUsers("1", Set.of("1"), member))
        .withMessage("You are not the owner from this group");

    verify(repository, times(1)).findById(any());
    verify(userService, never()).findById(any());
    verify(repository, never()).save(any());
  }

  @Test
  void testRemoveTransactions() {
    when(transactionService.findById(any())).then(transaction());
    when(repository.findById(any())).thenReturn(of(GroupBuilder.create(user, "12312").build()));

    groupService.removeTransactions("1", Set.of("1", "2", "3"), user);

    verify(transactionService, times(3)).findById(any());
    verify(repository, times(1)).findById(any());
    verify(repository, times(1)).save(any());
  }

  @Test
  void testRemoveTransactions_UserNotGroupMember() {
    when(transactionService.findById(any())).then(transaction());
    when(repository.findById(any())).thenReturn(of(GroupBuilder.create(user, "12312").build()));

    final var notMember = UserBuilder.create("id").withPassword("id").withName("id").build();
    assertThatIllegalArgumentException()
        .isThrownBy(() -> groupService.addTransactions("1", Set.of("1", "2", "3"), notMember))
        .withMessage("You are not member from this group");

    verify(repository, only()).findById(any());
  }

  private Answer<Transaction> transaction() {
    return invocation -> {
      final String id = invocation.getArgument(0);
      return TransactionBuilder.create(user, LocalDate.now(), EUR, ONE, INCOME)
          .withId(id)
          .build();
    };
  }

  private Answer<User> user() {
    return invocation -> {
      final String id = invocation.getArgument(0);
      return UserBuilder.create(id).withPassword(id).withName(id).build();
    };
  }

}