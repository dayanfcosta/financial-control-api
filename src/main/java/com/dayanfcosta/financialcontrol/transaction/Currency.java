package com.dayanfcosta.financialcontrol.transaction;

public enum Currency {

  BRL("Brazilian Real", "R$"),
  USD("United States Dollar", "$"),
  EUR("Euro", "€");

  private final String name;
  private final String symbol;

  Currency(final String name, final String symbol) {
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
