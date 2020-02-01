package com.dayanfcosta.financialcontrol.transaction;

import com.dayanfcosta.financialcontrol.commons.AbstractRepository;
import java.util.Optional;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@Repository
final class TransactionTagRepository extends AbstractRepository<TransactionTag> {

  protected TransactionTagRepository(final MongoTemplate template) {
    super(template, TransactionTag.class);
  }

  Optional<TransactionTag> findByDescription(final String description) {
    final var query = new Query(Criteria.where("description").is(description));
    return Optional.of(findOne(query));
  }
}
