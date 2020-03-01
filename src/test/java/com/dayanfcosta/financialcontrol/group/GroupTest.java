package com.dayanfcosta.financialcontrol.group;

import static com.dayanfcosta.financialcontrol.transaction.Currency.EUR;
import static com.dayanfcosta.financialcontrol.transaction.TransactionType.INCOME;
import static java.math.BigDecimal.ONE;
import static java.time.LocalDate.now;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

import com.dayanfcosta.financialcontrol.transaction.Transaction;
import com.dayanfcosta.financialcontrol.transaction.TransactionBuilder;
import com.dayanfcosta.financialcontrol.user.User;
import com.dayanfcosta.financialcontrol.user.UserBuilder;
import java.util.HashSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GroupTest {

  private User user;
  private User owner;
  private Group group;
  private Transaction transaction;

  @BeforeEach
  void setUp() {
    owner = UserBuilder.create("xpto@xpto.com").withName("xpto").withPassword("xpto").build();
    user = UserBuilder.create("xptoa@xpto.com").withName("xptoa").withPassword("xptoa").build();
    transaction = TransactionBuilder.create(owner, now(), EUR, ONE, INCOME).withId("1").build();
    group = GroupBuilder.create(owner, "group 1")
        .withTransactions(new HashSet<>(asList(transaction)))
        .withUsers(new HashSet<>(asList(user)))
        .build();
  }

  @Test
  void testAddTransaction() {
    final var transaction = TransactionBuilder.create(owner, now(), EUR, ONE, INCOME).withId("2").build();
    final var initialSize = group.getTransactions().size();

    group.addTransaction(transaction);

    assertThat(group.getTransactions().size()).isEqualTo(initialSize + 1);
  }

  @Test
  void testAddUser() {
    final var initialSize = group.getUsers().size();
    final var user = UserBuilder.create("xptoaa@xpto.com").withName("xptaoa").withPassword("xpatoa").build();

    group.addUser(user);

    assertThat(group.getUsers().size()).isEqualTo(initialSize + 1);
  }

  @Test
  void testAddUser_AddOwner() {
    assertThatIllegalArgumentException()
        .isThrownBy(() -> group.addUser(owner))
        .withMessage("User is the owner of the group");
  }

  @Test
  void testRemoveUser() {
    final var initialSize = group.getUsers().size();
    group.removeUser(user);

    assertThat(group.getUsers().size()).isEqualTo(initialSize - 1);
  }

  @Test
  void testRemoveUser_UserInvalid() {
    assertThatNullPointerException()
        .isThrownBy(() -> group.removeUser(null))
        .withMessage("Invalid user");
  }

  @Test
  void testRemoveTransaction() {
    final var initialSize = group.getTransactions().size();

    group.removeTransaction(transaction);

    assertThat(group.getTransactions().size()).isEqualTo(initialSize - 1);
  }

  @Test
  void testRemoveTransaction_TransactionInvalid() {
    assertThatNullPointerException()
        .isThrownBy(() -> group.removeTransaction(null))
        .withMessage("Invalid transaction");
  }
}