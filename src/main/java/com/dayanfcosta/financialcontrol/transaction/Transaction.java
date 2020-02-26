package com.dayanfcosta.financialcontrol.transaction;

import com.dayanfcosta.financialcontrol.commons.AbstractDocument;
import com.dayanfcosta.financialcontrol.user.User;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(value = "transactions")
public class Transaction extends AbstractDocument {

  private final LocalDate date;
  private final String description;
  private final Currency currency;
  private final BigDecimal amount;
  @DBRef
  private final TransactionTag tag;
  private final TransactionType type;
  @DBRef
  private final User owner;

  @PersistenceConstructor
  Transaction(final String id, final User owner, final LocalDate date, final BigDecimal amount, final Currency currency,
      final TransactionType type, final String description, final TransactionTag tag) {
    super(id);
    this.description = description;
    this.currency = currency;
    this.amount = amount;
    this.owner = owner;
    this.date = date;
    this.type = type;
    this.tag = tag;
  }

  public LocalDate getDate() {
    return date;
  }

  String getDescription() {
    return description;
  }

  public Currency getCurrency() {
    return currency;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  TransactionTag getTag() {
    return tag;
  }

  public TransactionType getType() {
    return type;
  }

  public User getOwner() {
    return owner;
  }
}
