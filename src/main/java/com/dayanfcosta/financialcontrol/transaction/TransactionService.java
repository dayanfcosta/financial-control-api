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

  Transaction save(final TransactionForm form, final User owner) {
    final var transaction = createTransaction(form, owner);
    return repository.save(transaction);
  }

  Transaction update(final String id, final TransactionForm form, final User owner) {
    final var transaction = findById(id);
    final var updatedTransaction = updateTransaction(transaction, form, owner);
    return repository.save(updatedTransaction);
  }

  void remove(final String id, final User owner) {
    final var transaction = findById(id);
    final var userCanRemove = transaction.getOwner().equals(owner);
    Validate.isTrue(userCanRemove, "You are not the owner of this transaction");
    repository.remove(transaction);
  }

  Page<Transaction> findAll(final LocalDate startInterval, final LocalDate endInterval, final User owner, final Pageable pageable) {
    if (startInterval != null || endInterval != null) {
      return findByInterval(owner, startInterval, endInterval, pageable);
    }
    return repository.findAll(owner, pageable);
  }

  Page<Transaction> findByDate(final LocalDate date, final User owner, final Pageable pageable) {
    return repository.findByDate(owner, date, pageable);
  }

  private Transaction createTransaction(final TransactionForm form, final User owner) {
    final var builder = TransactionBuilder.create(owner, form.getDate(), form.getCurrency(), form.getAmount(), form.getType())
        .withDescription(form.getDescription());
    form.getTagId().ifPresent(tagId -> builder.withTag(tagService.findById(tagId)));
    return builder.build();
  }

  private Transaction updateTransaction(final Transaction transaction, final TransactionForm form, final User owner) {
    final var builder = TransactionBuilder.create(owner, form.getDate(), form.getCurrency(), form.getAmount(), form.getType())
        .withDescription(form.getDescription())
        .withId(transaction.getId());
    form.getTagId().ifPresent(tagId -> builder.withTag(tagService.findById(tagId)));
    return builder.build();
  }

  private Page<Transaction> findByInterval(final User owner, final LocalDate startInterval, final LocalDate endInterval,
      final Pageable pageable) {
    return repository.findByDateInterval(owner, startInterval, endInterval, pageable);
  }

}
