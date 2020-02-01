package com.dayanfcosta.financialcontrol.transaction;

import static com.dayanfcosta.financialcontrol.transaction.Currency.EUR;
import static com.dayanfcosta.financialcontrol.transaction.TransactionType.INCOME;
import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.valueOf;
import static java.time.LocalDate.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.dayanfcosta.financialcontrol.user.User;
import com.dayanfcosta.financialcontrol.user.UserBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TransactionBuilderTest {

  private User user;

  @BeforeEach
  public void setUp() {
    user = UserBuilder.create("xpto@xpto.com")
        .withPassword("xpto")
        .withName("xpto")
        .withId("1")
        .build();
  }

  @Test
  void testCreate_DateIsNull() {
    assertThatThrownBy(() -> TransactionBuilder.create(user, null, EUR, ONE, INCOME))
        .isInstanceOf(NullPointerException.class)
        .hasMessage("Transaction date is invalid");
  }

  @Test
  void testCrete_AmountIsNegative() {
    assertThatThrownBy(() -> TransactionBuilder.create(user, now(), EUR, valueOf(-1), INCOME))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Transaction amount must be positive");
  }

  @Test
  void testCrete_AmountIsNull() {
    assertThatThrownBy(() -> TransactionBuilder.create(user, now(), EUR, null, INCOME))
        .isInstanceOf(NullPointerException.class)
        .hasMessage("Transaction amount is invalid");
  }

  @Test
  void testCreate_CurrencyIsNull() {
    assertThatThrownBy(() -> TransactionBuilder.create(user, now(), null, ONE, INCOME))
        .isInstanceOf(NullPointerException.class)
        .hasMessage("Transaction currency is invalid");
  }

  @Test
  void testCreate_TypeIsNull() {
    assertThatThrownBy(() -> TransactionBuilder.create(user, now(), EUR, ONE, null))
        .isInstanceOf(NullPointerException.class)
        .hasMessage("Transaction type is invalid");
  }

  @Test
  void testWithDescription() {
    final Transaction transaction = TransactionBuilder.create(user, now(), EUR, ONE, INCOME)
        .withDescription("xpto")
        .build();

    assertThat(transaction.getDescription()).isNotNull();
    assertThat(transaction.getDescription()).isNotEmpty();
  }

  @Test
  void testWithTag() {
    final TransactionTag tag = new TransactionTag("1", "Teste");
    final Transaction transaction = TransactionBuilder.create(user, now(), EUR, ONE, INCOME)
        .withTag(tag)
        .build();

    assertThat(transaction.getTag()).isNotNull();
    assertThat(transaction.getTag()).isEqualTo(tag);
  }

  @Test
  void testWithId() {
    final Transaction transaction = TransactionBuilder.create(user, now(), EUR, ONE, INCOME)
        .withId("1")
        .build();

    assertThat(transaction.getId()).isNotNull();
    assertThat(transaction.getId()).isNotEmpty();
    assertThat(transaction.getId()).isEqualTo("1");
  }
}