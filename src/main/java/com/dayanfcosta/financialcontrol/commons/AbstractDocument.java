package com.dayanfcosta.financialcontrol.commons;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;

/**
 * Base document
 *
 * @author dayanfcosta
 */
public abstract class AbstractDocument {

  @Id
  private String id;

  protected AbstractDocument() {
  }

  @PersistenceConstructor
  public AbstractDocument(final String id) {
    this.id = id;
  }

  public String getId() {
    return id;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    final AbstractDocument that = (AbstractDocument) o;
    return id.equals(that.id);
  }

  @Override
  public int hashCode() {
    return id.hashCode();
  }
}
