package com.dayanfcosta.financialcontrol.commons;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.repository.support.PageableExecutionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @author dayanfcosta
 */
public abstract class AbstractRepository<D extends AbstractDocument> {

  private final MongoTemplate template;
  private final Class<D> clazz;

  protected AbstractRepository(MongoTemplate template, Class<D> clazz) {
    this.template = template;
    this.clazz = clazz;
  }

  public D save(D document) {
    return template.save(document);
  }

  public Optional<D> findById(final String id) {
    return Optional.ofNullable(template.findById(id, clazz));
  }

  public Page<D> findAll(final Pageable pageable) {
    var query = new Query().with(pageable);
    var page = Collections.unmodifiableList(template.find(query, clazz));
    return PageableExecutionUtils.getPage(page, pageable, () -> template.count(query, clazz));
  }

  protected Page<D> find(final Query query, final Pageable pageable) {
    var page = Collections.unmodifiableList(template.find(query.with(pageable), clazz));
    return PageableExecutionUtils
        .getPage(page, pageable, () -> template.count(query.with(pageable), clazz));
  }

  protected D findOne(Query query) {
    return template.findOne(query, clazz);
  }

  protected MongoTemplate template() {
    return template;
  }

}
