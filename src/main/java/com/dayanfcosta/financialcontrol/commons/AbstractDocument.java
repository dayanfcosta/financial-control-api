package com.dayanfcosta.financialcontrol.commons;

import org.springframework.data.annotation.Id;

import java.util.Optional;

/**
 * Base document
 * @author dayanfcosta
 */
public abstract class AbstractDocument {

  @Id
  private final String id;

  public AbstractDocument(String id) {
    this.id = id;
  }

  public Optional<String> getId() {
    return Optional.ofNullable(id);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    AbstractDocument that = (AbstractDocument) o;
    return id.equals(that.id);
  }

  @Override
  public int hashCode() {
    return id.hashCode();
  }
}
