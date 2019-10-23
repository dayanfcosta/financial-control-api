package com.dayanfcosta.financialcontrol.user;

import com.dayanfcosta.financialcontrol.commons.AbstractRepository;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author dayanfcosta
 */
@Repository
class UserRepository extends AbstractRepository<User> {

  UserRepository(MongoTemplate template) {
    super(template, User.class);
  }

  Optional<User> findByEmail(String email) {
    var query = new Query(Criteria.where("email").is(email));
    return Optional.ofNullable(findOne(query));
  }
}
