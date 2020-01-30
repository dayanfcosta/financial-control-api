package com.dayanfcosta.financialcontrol.transaction;

import com.dayanfcosta.financialcontrol.commons.AbstractDocument;
import java.util.Objects;
import org.apache.commons.lang3.Validate;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(value = "transaction_tags")
public class TransactionTag extends AbstractDocument {

  private final String description;

  public TransactionTag(final String id, final String description) {
    super(id);
    this.description = Validate.notEmpty(description, "Invalid tag description");
  }

  public String getDescription() {
    return description;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    if (!super.equals(o)) {
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
    final StringBuilder sb = new StringBuilder("TransactionTag{");
    sb.append("description='").append(description).append('\'');
    sb.append('}');
    return sb.toString();
  }
}
