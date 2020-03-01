package com.dayanfcosta.financialcontrol.transaction;

import com.dayanfcosta.financialcontrol.commons.AbstractDocument;
import com.google.common.base.MoreObjects;
import java.util.Objects;
import org.apache.commons.lang3.Validate;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(value = "transaction_tags")
public class TransactionTag extends AbstractDocument {

  @Indexed(unique = true, name = "uk_transaction_tag")
  private final String description;

  @PersistenceConstructor
  TransactionTag(final String id, final String description) {
    super(id);
    this.description = Validate.notEmpty(description, "Invalid tag description");
  }

  TransactionTag(final String description) {
    this(null, description);
  }

  String getDescription() {
    return description;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass() || !super.equals(o)) {
      return false;
    }
    final TransactionTag that = (TransactionTag) o;
    return description.equals(that.description);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), description);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("description", description)
        .add("id", getId())
        .toString();
  }
}
