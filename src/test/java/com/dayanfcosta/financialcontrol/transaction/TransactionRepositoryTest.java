package com.dayanfcosta.financialcontrol.transaction;

import static com.dayanfcosta.financialcontrol.transaction.Currency.EUR;
import static com.dayanfcosta.financialcontrol.transaction.TransactionType.INCOME;
import static java.math.BigDecimal.TEN;
import static java.time.LocalDate.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.data.domain.Pageable.unpaged;

import com.dayanfcosta.financialcontrol.AbstractRepositoryTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.mongodb.core.query.Query;

class TransactionRepositoryTest extends AbstractRepositoryTest {

  private TransactionRepository repository;

  @BeforeEach
  void setUp() {
    repository = new TransactionRepository(getMongoTemplate());

    for (int i = 0; i < 10; i++) {
      final var date = now().plusDays(i);
      final var transaction = TransactionBuilder.create(date, TEN, EUR, INCOME).build();
      repository.save(transaction);
    }
  }

  @AfterEach
  void tearDown() {
    getMongoTemplate().remove(new Query(), Transaction.class);
  }

  @Test
  void testFindByDate() {
    final var page = repository.findByDate(now(), unpaged());

    assertThat(page.getTotalElements()).isEqualTo(1L);
  }

  @Test
  void testFindByDateInterval() {
    final var page = repository.findByDateInterval(now(), now().plusDays(1L), unpaged());

    assertThat(page.getTotalElements()).isEqualTo(2L);
  }
}