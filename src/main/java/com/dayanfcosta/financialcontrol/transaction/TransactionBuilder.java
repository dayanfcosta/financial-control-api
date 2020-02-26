package com.dayanfcosta.financialcontrol.transaction;

import com.dayanfcosta.financialcontrol.user.User;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.apache.commons.lang3.Validate;

public class TransactionBuilder {

  private String id;
  private final User owner;
  private TransactionTag tag;
  private String description;
  private final LocalDate date;
  private final Currency currency;
  private final BigDecimal amount;
  private final TransactionType type;

  private TransactionBuilder(final LocalDate date, final BigDecimal amount, final Currency currency, final TransactionType type,
      final User owner) {
    this.currency = Validate.notNull(currency, "Transaction currency is invalid");
    this.date = Validate.notNull(date, "Transaction date is invalid");
    this.type = Validate.notNull(type, "Transaction type is invalid");
    Validate.notNull(amount, "Transaction amount is invalid");
    Validate.isTrue(amount.compareTo(BigDecimal.ZERO) > 0, "Transaction amount must be positive");
    this.amount = amount;
    this.owner = Validate.notNull(owner, "Transaction owner invalid");
  }

  public static TransactionBuilder create(final User owner, final LocalDate date, final Currency currency, final BigDecimal amount,
      final TransactionType type) {
    return new TransactionBuilder(date, amount, currency, type, owner);
  }

  public TransactionBuilder withTag(final TransactionTag tag) {
    this.tag = tag;
    return this;
  }

  public TransactionBuilder withDescription(final String description) {
    this.description = description;
    return this;
  }

  public TransactionBuilder withId(final String id) {
    this.id = id;
    return this;
  }

  public Transaction build() {
    return new Transaction(id, owner, date, amount, currency, type, description, tag);
  }

}
