package com.dayanfcosta.financialcontrol.transaction;

import com.dayanfcosta.financialcontrol.commons.AbstractRepository;
import java.time.LocalDate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@Repository
class TransactionRepository extends AbstractRepository<Transaction> {

  private static final String DATE_FIELD = "date";

  TransactionRepository(final MongoTemplate template) {
    super(template, Transaction.class);
  }

  Page<Transaction> findByDate(final LocalDate date, final Pageable pageable) {
    final var query = new Query(Criteria.where(DATE_FIELD).is(date)).with(pageable);
    return find(query, pageable);
  }

  Page<Transaction> findByDateInterval(final LocalDate start, final LocalDate end, final Pageable pageable) {
    final var criteria = Criteria.where(DATE_FIELD).gte(start).lte(end);
    final var query = new Query(criteria).with(pageable);
    return find(query, pageable);
  }
}
