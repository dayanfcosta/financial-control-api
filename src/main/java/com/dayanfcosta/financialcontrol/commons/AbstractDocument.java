package com.dayanfcosta.financialcontrol.commons;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;

import java.util.Optional;

/**
 * Base document
 *
 * @author dayanfcosta
 */
public abstract class AbstractDocument {

  @Id
  private String id;

  @PersistenceConstructor
  public AbstractDocument(String id) {
    this.id = id;
  }

  public String getId() {
    return id;
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
