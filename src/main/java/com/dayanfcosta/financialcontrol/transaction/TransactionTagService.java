package com.dayanfcosta.financialcontrol.transaction;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
class TransactionTagService {

  private final TransactionTagRepository repository;

  TransactionTagService(final TransactionTagRepository repository) {
    this.repository = repository;
  }

  TransactionTag save(final String tagDescription) {
    final var existing = repository.findByDescriptionIgnoreCase(tagDescription);
    return existing.orElseGet(() -> repository.save(new TransactionTag(tagDescription)));
  }

  Page<TransactionTag> findAll(final Pageable pageable) {
    return repository.findAll(pageable);
  }

  TransactionTag findById(final String id) {
    return repository.findById(id).orElseThrow(() -> new NullPointerException("Tag not found"));
  }
}
