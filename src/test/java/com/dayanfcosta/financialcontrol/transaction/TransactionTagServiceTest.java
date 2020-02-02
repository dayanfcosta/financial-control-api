package com.dayanfcosta.financialcontrol.transaction;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.data.domain.Pageable.unpaged;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;

class TransactionTagServiceTest {

  private TransactionTag tag;
  private TransactionTagService service;
  private TransactionTagRepository repository;

  @BeforeEach
  void setUp() {
    repository = mock(TransactionTagRepository.class);
    service = new TransactionTagService(repository);
    tag = new TransactionTag("1", "Teste");
  }

  @Test
  void testSave_tagAlreadyExists() {
    when(repository.findByDescriptionIgnoreCase(any())).thenReturn(of(tag));

    final var existing = service.save("Teste");

    assertThat(existing).isEqualTo(tag);
    verify(repository, never()).save(any());
  }

  @Test
  void testSave_newTag() {
    when(repository.findByDescriptionIgnoreCase(any())).thenReturn(empty());

    service.save("Teste");

    verify(repository, times(1)).save(any());
  }

  @Test
  void testFindAll() {
    when(repository.findAll(any())).thenReturn(Page.empty());

    service.findAll(unpaged());

    verify(repository, times(1)).findAll(unpaged());
  }
}