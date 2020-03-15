package com.dayanfcosta.financialcontrol.transaction;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TransactionDto {

  private String tagId;
  private String id;
  private String ownerId;
  private LocalDate date;
  private BigDecimal amount;
  private Currency currency;
  private String description;
  private TransactionType type;

  private TransactionDto() {
    super();
  }

  TransactionDto(final Transaction transaction) {
    id = transaction.getId();
    type = transaction.getType();
    date = transaction.getDate();
    setTagId(transaction.getTag());
    amount = transaction.getAmount();
    currency = transaction.getCurrency();
    ownerId = transaction.getOwner().getId();
    description = transaction.getDescription();
  }

  public String getId() {
    return id;
  }

  Optional<String> getTagId() {
    return Optional.ofNullable(tagId);
  }

  String getOwnerId() {
    return ownerId;
  }

  LocalDate getDate() {
    return date;
  }

  BigDecimal getAmount() {
    return amount;
  }

  Currency getCurrency() {
    return currency;
  }

  String getDescription() {
    return description;
  }

  TransactionType getType() {
    return type;
  }

  private void setTagId(final TransactionTag tag) {
    if (tag != null) {
      tagId = tag.getId();
    }
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("TransactionDto{");
    sb.append("tagId='").append(tagId).append('\'');
    sb.append(", ownerId='").append(ownerId).append('\'');
    sb.append(", date=").append(date);
    sb.append(", amount=").append(amount);
    sb.append(", currency=").append(currency);
    sb.append(", description='").append(description).append('\'');
    sb.append(", type=").append(type);
    sb.append('}');
    return sb.toString();
  }
}
