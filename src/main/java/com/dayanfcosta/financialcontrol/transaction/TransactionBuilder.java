package com.dayanfcosta.financialcontrol.transaction;

import java.math.BigDecimal;
import java.time.LocalDate;
import org.apache.commons.lang3.Validate;

class TransactionBuilder {

  private String id;
  private TransactionTag tag;
  private String description;
  private final LocalDate date;
  private final Currency currency;
  private final BigDecimal amount;
  private final TransactionType type;

  private TransactionBuilder(final LocalDate date, final BigDecimal amount, final Currency currency, final TransactionType type) {
    this.currency = Validate.notNull(currency, "Transaction currency is invalid");
    this.date = Validate.notNull(date, "Transaction date is invalid");
    this.type = Validate.notNull(type, "Transaction type is invalid");
    Validate.notNull(amount, "Transaction amount is invalid");
    Validate.isTrue(amount.compareTo(BigDecimal.ZERO) > 0, "Transaction amount must be positive");
    this.amount = amount;
  }

  static TransactionBuilder create(final LocalDate date, final BigDecimal amount, final Currency currency,
      final TransactionType type) {
    return new TransactionBuilder(date, amount, currency, type);
  }

  TransactionBuilder withTag(final TransactionTag tag) {
    this.tag = tag;
    return this;
  }

  TransactionBuilder withDescription(final String description) {
    this.description = description;
    return this;
  }

  TransactionBuilder withId(final String id) {
    this.id = id;
    return this;
  }

  Transaction build() {
    return new Transaction(id, description, currency, amount, tag, type, date);
  }

}
