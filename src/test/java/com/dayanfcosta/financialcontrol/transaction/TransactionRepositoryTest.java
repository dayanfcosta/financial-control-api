package com.dayanfcosta.financialcontrol.transaction;

import static com.dayanfcosta.financialcontrol.transaction.Currency.EUR;
import static com.dayanfcosta.financialcontrol.transaction.TransactionType.INCOME;
import static java.math.BigDecimal.TEN;
import static java.time.LocalDate.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.data.domain.Pageable.unpaged;

import com.dayanfcosta.financialcontrol.AbstractRepositoryTest;
import com.dayanfcosta.financialcontrol.user.User;
import com.dayanfcosta.financialcontrol.user.UserBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TransactionRepositoryTest extends AbstractRepositoryTest {

  private User user;
  private TransactionRepository transactionRepository;

  public TransactionRepositoryTest() {
    addDocumentsToClear(User.class, Transaction.class);
  }

  @BeforeEach
  public void setUpTransactions() {
    user = UserBuilder.create("xpto@xpto.com")
        .withPassword("zpto")
        .withName("xpto")
        .build();
    getMongoTemplate().save(user);
    transactionRepository = new TransactionRepository(getMongoTemplate());

    for (int i = 0; i < 10; i++) {
      final var date = now().plusDays(i);
      final var transaction = TransactionBuilder.create(user, date, EUR, TEN, INCOME).build();
      transactionRepository.save(transaction);
    }
  }

  @Test
  void testFindByDate() {
    final var page = transactionRepository.findByDate(user, now(), unpaged());

    assertThat(page.getTotalElements()).isEqualTo(1L);
  }

  @Test
  void testFindByDateInterval() {
    final var page = transactionRepository.findByDateInterval(user, now(), now().plusDays(1L), unpaged());

    assertThat(page.getTotalElements()).isEqualTo(2L);
  }
}