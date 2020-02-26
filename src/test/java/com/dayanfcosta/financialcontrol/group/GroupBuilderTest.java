package com.dayanfcosta.financialcontrol.group;

import static com.dayanfcosta.financialcontrol.transaction.Currency.EUR;
import static com.dayanfcosta.financialcontrol.transaction.TransactionType.INCOME;
import static java.math.BigDecimal.ONE;
import static java.time.LocalDate.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

import com.dayanfcosta.financialcontrol.transaction.Transaction;
import com.dayanfcosta.financialcontrol.transaction.TransactionBuilder;
import com.dayanfcosta.financialcontrol.user.User;
import com.dayanfcosta.financialcontrol.user.UserBuilder;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GroupBuilderTest {

  private User owner;
  private Transaction transaction;

  @BeforeEach
  void setUp() {
    owner = UserBuilder.create("xpto@xpto.com").withName("xpto").withPassword("xpto").build();
    transaction = TransactionBuilder.create(owner, now(), EUR, ONE, INCOME).withId("1").build();
  }

  @Test
  void testCreate() {
    final var groupName = "group 1";
    final var group = GroupBuilder.create(owner, groupName).build();

    assertThat(group.getOwner()).isEqualTo(owner);
    assertThat(group.getName()).isEqualTo(groupName);
  }

  @Test
  void testCreate_InvalidOwner() {
    assertThatNullPointerException()
        .isThrownBy(() -> GroupBuilder.create(null, "group 1"))
        .withMessage("Invalid group owner");
  }

  @Test
  void testCreate_EmptyGroupName() {
    assertThatIllegalArgumentException()
        .isThrownBy(() -> GroupBuilder.create(owner, ""))
        .withMessage("Invalid group name");
  }

  @Test
  void testCreate_GroupNameIsNull() {
    assertThatNullPointerException()
        .isThrownBy(() -> GroupBuilder.create(owner, null))
        .withMessage("Invalid group name");
  }

  @Test
  void testBuild_GroupWithId() {
    final var group = GroupBuilder.create(owner, "group 1")
        .withId("1")
        .build();

    assertThat(group.getId()).isNotNull();
  }

  @Test
  void testBuild_GroupWithTransactions() {
    final var group = GroupBuilder.create(owner, "group 1")
        .withTransaction(Set.of(transaction))
        .build();

    assertThat(group.getTransactions()).isNotNull();
    assertThat(group.getTransactions()).isNotEmpty();
  }

  @Test
  void testBuild_GroupWithUsers() {
    final var user = UserBuilder.create("12@123.com").withName("123").withPassword("123").build();

    final var group = GroupBuilder.create(owner, "group 1")
        .withUsers(Set.of(user))
        .build();

    final var users = group.getUsers();
    assertThat(users).isNotNull();
    assertThat(users).isNotEmpty();
    assertThat(users.stream().allMatch(user::equals)).isTrue();
  }

}