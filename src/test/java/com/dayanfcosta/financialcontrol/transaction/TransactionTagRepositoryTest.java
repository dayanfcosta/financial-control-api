package com.dayanfcosta.financialcontrol.transaction;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.dayanfcosta.financialcontrol.AbstractRepositoryTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Pageable;

class TransactionTagRepositoryTest extends AbstractRepositoryTest {

  private TransactionTag tag;
  private TransactionTagRepository repository;

  public TransactionTagRepositoryTest() {
    addDocumentsToClear(TransactionTag.class);
  }

  @Override
  @BeforeEach
  public void setUp() {
    tag = new TransactionTag("teste");
    repository = new TransactionTagRepository(getMongoTemplate());
  }

  @Test
  void testSave() {
    final var saved = repository.save(tag);

    assertThat(saved.getId()).isNotNull();
    assertThat(saved.getDescription()).isEqualTo(tag.getDescription());
  }

  @Test
  void testSave_Duplicated() {
    final var duplicatedTag = new TransactionTag("teste");
    repository.save(tag);

    assertThatThrownBy(() -> repository.save(duplicatedTag))
        .isInstanceOf(DuplicateKeyException.class);
  }

  @Test
  void testFindById() {
    repository.save(tag);

    final var existing = repository.findById(tag.getId());

    assertThat(existing).isNotEmpty();
    assertThat(existing.get()).isEqualTo(tag);
  }

  @Test
  void testFindAll() {
    final var initialSize = repository.findAll(Pageable.unpaged()).getSize();
    repository.save(tag);

    final var users = repository.findAll(Pageable.unpaged());

    assertThat(users).isNotEmpty();
    assertThat(users).hasSize(initialSize + 1);
  }

  @Test
  void testFindByDescription() {
    repository.save(tag);

    final var existent = repository.findByDescriptionIgnoreCase("teste");

    assertThat(existent).isNotNull();
    assertThat(existent).isNotEmpty();
    assertThat(existent.get()).isEqualTo(tag);
  }

  @Test
  void testFindByDescription_ignoringCase() {
    repository.save(tag);

    final var existent = repository.findByDescriptionIgnoreCase("TESTE");

    assertThat(existent).isNotNull();
    assertThat(existent).isNotEmpty();
    assertThat(existent.get()).isEqualTo(tag);
  }
}