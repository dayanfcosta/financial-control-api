package com.dayanfcosta.financialcontrol.group;

import com.dayanfcosta.financialcontrol.commons.AbstractDocument;
import com.dayanfcosta.financialcontrol.transaction.Transaction;
import com.dayanfcosta.financialcontrol.user.User;
import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableSet;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;
import org.apache.commons.lang3.Validate;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("groups")
@CompoundIndex(name = "uk_groups", def = "{'name' : 1, 'owner' : 1}", unique = true)
public class Group extends AbstractDocument {

  private final String name;
  @DBRef
  @Indexed(name = "idx_group_owner")
  private final User owner;
  @DBRef
  private final Set<User> users;
  @DBRef
  private final Set<Transaction> transactions;
  private final LocalDateTime creationDate;

  @PersistenceConstructor
  Group(final String id, final String name, final User owner, final Set<User> users, final Set<Transaction> transactions,
      final LocalDateTime creationDate) {
    super(id);
    this.name = name;
    this.owner = owner;
    this.users = users;
    this.transactions = transactions;
    this.creationDate = creationDate;
  }

  public String getName() {
    return name;
  }

  public User getOwner() {
    return owner;
  }

  public ImmutableSet<User> getUsers() {
    return ImmutableSet.copyOf(users);
  }

  public ImmutableSet<Transaction> getTransactions() {
    return ImmutableSet.copyOf(transactions);
  }

  public LocalDateTime getCreationDate() {
    return creationDate;
  }

  public final void addTransaction(final Transaction transaction) {
    Validate.notNull(transaction, "Invalid transaction");
    transactions.add(transaction);
  }

  public final void removeTransaction(final Transaction transaction){
    Validate.notNull(transaction, "Invalid transaction");
    transactions.remove(transaction);
  }

  public final void addUser(final User user) {
    Validate.notNull(user, "Invalid user");
    Validate.isTrue(!owner.equals(user), "User is the owner of the group");
    users.add(user);
  }

  public final void removeUser(final User user) {
    Validate.notNull(user, "Invalid user");
    users.remove(user);
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass() || !super.equals(o)) {
      return false;
    }
    final Group group = (Group) o;
    return name.equals(group.name) && owner.equals(group.owner);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), name, owner);
  }


  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("name", name)
        .add("owner", owner)
        .add("creationDate", creationDate)
        .add("id", getId())
        .toString();
  }
}
