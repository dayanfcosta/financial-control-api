package com.dayanfcosta.financialcontrol.transaction;

import com.dayanfcosta.financialcontrol.user.User;
import com.dayanfcosta.financialcontrol.user.UserService;
import java.time.LocalDate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
class TransactionService {

  private final UserService userService;
  private final TransactionTagService tagService;
  private final TransactionRepository repository;

  TransactionService(final TransactionRepository repository, final UserService userService, final TransactionTagService tagService) {
    this.repository = repository;
    this.tagService = tagService;
    this.userService = userService;
  }

  Transaction save(final TransactionDto dto) {
    final var owner = userService.findById(dto.getOwnerId());
    final var transaction = createTransaction(dto, owner);
    return repository.save(transaction);
  }

  void update(final String id, final TransactionDto dto) {
    final var transaction = findById(id);
    final var owner = userService.findById(dto.getOwnerId());
    final var updatedTransaction = updateTransaction(transaction, dto, owner);
    repository.save(updatedTransaction);
  }

  void remove(final String id) {
    final var transaction = findById(id);
    repository.remove(transaction);
  }

  Page<Transaction> findAll(final LocalDate startInterval, final LocalDate endInterval, final Pageable pageable) {
    if (startInterval != null || endInterval != null) {
      return findByInterval(startInterval, endInterval, pageable);
    }
    return repository.findAll(pageable);
  }

  Page<Transaction> findByDate(final LocalDate date, final Pageable pageable) {
    return repository.findByDate(date, pageable);
  }

  Transaction findById(final String id) {
    return repository.findById(id).orElseThrow(() -> new NullPointerException("Transaction not found"));
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

  private Page<Transaction> findByInterval(final LocalDate startInterval, final LocalDate endInterval, final Pageable pageable) {
    return repository.findByDateInterval(startInterval, endInterval, pageable);
  }

}
