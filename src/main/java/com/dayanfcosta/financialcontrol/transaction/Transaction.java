package com.dayanfcosta.financialcontrol.transaction;

import com.dayanfcosta.financialcontrol.commons.AbstractDocument;
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

  @PersistenceConstructor
  Transaction(final String id, final String description, final Currency currency, final BigDecimal amount, final TransactionTag tag,
      final TransactionType type, final LocalDate date) {
    super(id);
    this.description = description;
    this.currency = currency;
    this.amount = amount;
    this.date = date;
    this.type = type;
    this.tag = tag;
  }

  public LocalDate getDate() {
    return date;
  }

  public String getDescription() {
    return description;
  }

  public Currency getCurrency() {
    return currency;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public TransactionTag getTag() {
    return tag;
  }

  public TransactionType getType() {
    return type;
  }
}
