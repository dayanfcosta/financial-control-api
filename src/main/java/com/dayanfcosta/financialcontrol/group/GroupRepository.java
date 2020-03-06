package com.dayanfcosta.financialcontrol.group;

import com.dayanfcosta.financialcontrol.commons.AbstractRepository;
import com.dayanfcosta.financialcontrol.user.User;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@Repository
class GroupRepository extends AbstractRepository<Group> {

  protected GroupRepository(final MongoTemplate template) {
    super(template, Group.class);
  }

  @Cacheable(key = "'user:' + #user.id + '_pageable:' + #pageable")
  public Page<Group> findByUser(final User user, final Pageable pageable) {
    final var containsUser = Criteria.where("users").in(user);
    final var isOwner = Criteria.where("owner").is(user);
    final var query = new Query(new Criteria().orOperator(isOwner, containsUser));
    return find(query, pageable);
  }

}
