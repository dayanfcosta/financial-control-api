package com.dayanfcosta.financialcontrol.transaction;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

public class TransactionForm {

  private String tagId;
  private String ownerId;
  private LocalDate date;
  private BigDecimal amount;
  private Currency currency;
  private String description;
  private TransactionType type;

  private TransactionForm() {
  }

  public TransactionForm(final Transaction transaction) {
    type = transaction.getType();
    date = transaction.getDate();
    setTagId(transaction.getTag());
    amount = transaction.getAmount();
    currency = transaction.getCurrency();
    ownerId = transaction.getOwner().getId();
    description = transaction.getDescription();
  }

  public void setTagId(final TransactionTag tag) {
    if (tag != null) {
      tagId = tag.getId();
    }
  }

  public Optional<String> getTagId() {
    return Optional.ofNullable(tagId);
  }

  public String getOwnerId() {
    return ownerId;
  }

  public LocalDate getDate() {
    return date;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public Currency getCurrency() {
    return currency;
  }

  public String getDescription() {
    return description;
  }

  public TransactionType getType() {
    return type;
  }
}
