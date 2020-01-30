package com.dayanfcosta.financialcontrol.transaction;

public enum TransactionType {

  INCOME("Income", "+"),
  EXPENSE("Expense", "-");

  private final String name;
  private final String symbol;

  TransactionType(final String name, final String symbol) {
    this.name = name;
    this.symbol = symbol;
  }

  public String getName() {
    return name;
  }

  public String getSymbol() {
    return symbol;
  }

  @Override
  public String toString() {
    return getName();
  }
}
