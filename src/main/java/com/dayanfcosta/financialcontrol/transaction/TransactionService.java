package com.dayanfcosta.financialcontrol.transaction;

import com.dayanfcosta.financialcontrol.user.User;
import java.time.LocalDate;
import org.apache.commons.lang3.Validate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {

  private final TransactionTagService tagService;
  private final TransactionRepository repository;

  TransactionService(final TransactionRepository repository, final TransactionTagService tagService) {
    this.repository = repository;
    this.tagService = tagService;
  }

  public Transaction findById(final String id) {
    return repository.findById(id).orElseThrow(() -> new NullPointerException("Transaction not found"));
  }

  Transaction save(final TransactionDto dto, final User owner) {
    final var transaction = createTransaction(dto, owner);
    return repository.save(transaction);
  }

  Transaction update(final String id, final TransactionDto dto, final User owner) {
    final var transaction = findById(id);
    final var updatedTransaction = updateTransaction(transaction, dto, owner);
    return repository.save(updatedTransaction);
  }

  void remove(final String id, final User owner) {
    final var transaction = findById(id);
    final var userCanRemove = transaction.getOwner().equals(owner);
    Validate.isTrue(userCanRemove, "You are not the owner of this transaction");
    repository.remove(transaction);
  }

  Page<Transaction> findAll(final User owner, final LocalDate startInterval, final LocalDate endInterval, final Pageable pageable) {
    if (startInterval != null || endInterval != null) {
      return findByInterval(owner, startInterval, endInterval, pageable);
    }
    return repository.findAll(owner, pageable);
  }

  Page<Transaction> findByDate(final User owner, final LocalDate date, final Pageable pageable) {
    return repository.findByDate(owner, date, pageable);
  }

  private Transaction createTransaction(final TransactionDto dto, final User owner) {
    final var builder = TransactionBuilder.create(owner, dto.getDate(), dto.getCurrency(), dto.getAmount(), dto.getType())
        .withDescription(dto.getDescription());
    dto.getTagId().ifPresent(tagId -> builder.withTag(tagService.findById(tagId)));
    return builder.build();
  }

  private Transaction updateTransaction(final Transaction transaction, final TransactionDto dto, final User owner) {
    final var builder = TransactionBuilder.create(owner, dto.getDate(), dto.getCurrency(), dto.getAmount(), dto.getType())
        .withDescription(dto.getDescription())
        .withId(transaction.getId());
    dto.getTagId().ifPresent(tagId -> builder.withTag(tagService.findById(tagId)));
    return builder.build();
  }

  private Page<Transaction> findByInterval(final User owner, final LocalDate startInterval, final LocalDate endInterval,
      final Pageable pageable) {
    return repository.findByDateInterval(owner, startInterval, endInterval, pageable);
  }

}
