package com.dayanfcosta.financialcontrol.transaction;

import com.dayanfcosta.financialcontrol.commons.AbstractRepository;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

@Repository
class TransactionRepository extends AbstractRepository<Transaction> {

  protected TransactionRepository(final MongoTemplate template) {
    super(template, Transaction.class);
  }
}
