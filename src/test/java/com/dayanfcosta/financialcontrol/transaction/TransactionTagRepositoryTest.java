package com.dayanfcosta.financialcontrol.transaction;

import static org.assertj.core.api.Assertions.assertThat;

import com.dayanfcosta.financialcontrol.AbstractRepositoryTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.mongodb.core.query.Query;

class TransactionTagRepositoryTest extends AbstractRepositoryTest {

  private TransactionTag tag;
  private TransactionTagRepository repository;

  @BeforeEach
  void setUp() {
    tag = new TransactionTag("teste");
    repository = new TransactionTagRepository(getMongoTemplate());
  }

  @AfterEach
  void tearDown() {
    getMongoTemplate().remove(new Query(), TransactionTag.class);
  }

  @Test
  void testFindByDescription() {
    final var saved = repository.save(tag);

    assertThat(saved.getId()).isNotNull();
    assertThat(saved.getDescription()).isEqualTo(tag.getDescription());
  }
}