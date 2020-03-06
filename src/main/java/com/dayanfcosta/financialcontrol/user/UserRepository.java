package com.dayanfcosta.financialcontrol.user;

import com.dayanfcosta.financialcontrol.commons.AbstractRepository;
import java.util.Optional;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

/**
 * @author dayanfcosta
 */
@Repository
class UserRepository extends AbstractRepository<User> {

  UserRepository(final MongoTemplate template) {
    super(template, User.class);
  }

  @Cacheable(key = "#email")
  public Optional<User> findByEmail(final String email) {
    final var query = new Query(Criteria.where("email").is(email));
    return Optional.ofNullable(findOne(query));
  }
}
