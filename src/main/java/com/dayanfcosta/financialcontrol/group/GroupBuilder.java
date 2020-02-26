package com.dayanfcosta.financialcontrol.group;

import com.dayanfcosta.financialcontrol.transaction.Transaction;
import com.dayanfcosta.financialcontrol.user.User;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.lang3.Validate;

public class GroupBuilder {

  private String id;
  private final User owner;
  private final String name;
  private Set<User> users = new HashSet<>();
  private Set<Transaction> transactions = new HashSet<>();

  private GroupBuilder(final User owner, final String name) {
    this.owner = Validate.notNull(owner, "Invalid group owner");
    this.name = Validate.notBlank(name, "Invalid group name");
  }

  public static GroupBuilder create(final User owner, final String name) {
    return new GroupBuilder(owner, name);
  }

  GroupBuilder withId(final String id) {
    this.id = id;
    return this;
  }

  GroupBuilder withUsers(final Set<User> users) {
    this.users = users;
    return this;
  }

  GroupBuilder withTransaction(final Set<Transaction> transactions) {
    this.transactions = transactions;
    return this;
  }

  Group build() {
    return new Group(id, name, owner, users, transactions, LocalDateTime.now());
  }
}
