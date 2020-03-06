package com.dayanfcosta.financialcontrol.transaction;

import com.dayanfcosta.financialcontrol.commons.AbstractRepository;
import com.dayanfcosta.financialcontrol.user.User;
import java.time.LocalDate;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@Repository
class TransactionRepository extends AbstractRepository<Transaction> {

  private static final String DATE_FIELD = "date";
  public static final String OWNER_FIELD = "owner";

  TransactionRepository(final MongoTemplate template) {
    super(template, Transaction.class);
  }

  @Caching(
      cacheable = {
          @Cacheable(key = "'owner:' + #owner.id + '_pageable:' + #pageable"),
          @Cacheable(key = "'owner:' + #owner.id + '_date:' + #date + '_pageable:' + #pageable")
      }
  )
  public Page<Transaction> findByDate(final User owner, final LocalDate date, final Pageable pageable) {
    final var criteria = Criteria.where(DATE_FIELD).is(date).and(OWNER_FIELD).is(owner);
    final var query = new Query(criteria).with(pageable);
    return find(query, pageable);
  }

  @Caching(
      cacheable = {
          @Cacheable(key = "'owner:' + #owner.id + '_date:' + #end + '_pageable:' + #pageable"),
          @Cacheable(key = "'owner:' + #owner.id + '_date:' + #start + '_pageable:' + #pageable"),
          @Cacheable(key = "'owner:' + #owner.id + '_start:' + #start + '_end:' + end + '_pageable:' + #pageable"),
      },
      put = {
          @CachePut(key = "'owner:' + #owner.id + '_date': #result.date"),
          @CachePut(key = "'owner:' + #owner.id + '_date': #result.date")
      }
  )
  public Page<Transaction> findByDateInterval(final User owner, final LocalDate start, final LocalDate end, final Pageable pageable) {
    final Criteria criteria = createIntervalCriteria(owner, start, end);
    final var query = new Query(criteria).with(pageable);
    return find(query, pageable);
  }

  @Caching(
      cacheable = {@Cacheable(key = "'owner:' + #owner.id + '_pageable:' + #pageable")},
      put = {@CachePut(key = "'owner:' + #owner.id + '_date:' + #result.date + '_pageable:' + #pageable")}
  )
  public Page<Transaction> findAll(final User owner, final Pageable pageable) {
    final var query = new Query(Criteria.where(OWNER_FIELD).is(owner));
    return find(query, pageable);
  }

  private Criteria createIntervalCriteria(final User owner, final LocalDate start, final LocalDate end) {
    final var criteria = Criteria.where(DATE_FIELD);
    if (start != null) {
      criteria.gte(start);
    }
    if (end != null) {
      criteria.lte(end);
    }
    return criteria.and(OWNER_FIELD).is(owner);
  }
}
