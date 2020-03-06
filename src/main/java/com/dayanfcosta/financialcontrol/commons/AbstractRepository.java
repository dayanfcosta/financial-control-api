package com.dayanfcosta.financialcontrol.commons;

import static com.dayanfcosta.financialcontrol.config.redis.RedisConfig.DEFAULT_CACHE_RESOLVER;

import java.util.Collections;
import java.util.Optional;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.repository.support.PageableExecutionUtils;

/**
 * @author dayanfcosta
 */
@CacheConfig(cacheResolver = DEFAULT_CACHE_RESOLVER)
public abstract class AbstractRepository<D extends AbstractDocument> {

  private final Class<D> clazz;
  private final MongoTemplate mongoTemplate;

  protected AbstractRepository(final MongoTemplate mongoTemplate, final Class<D> clazz) {
    this.mongoTemplate = mongoTemplate;
    this.clazz = clazz;
  }

  @Caching(put = @CachePut(key = "#return.id"), evict = @CacheEvict(key = "#return.id"))
  public D save(final D document) {
    return mongoTemplate.save(document);
  }

  @Cacheable(key = "#id")
  public Optional<D> findById(final String id) {
    final var document = mongoTemplate.findById(id, clazz);
    return Optional.ofNullable(document);
  }

  public Page<D> findAll(final Pageable pageable) {
    final var query = new Query().with(pageable);
    final var page = Collections.unmodifiableList(mongoTemplate.find(query, clazz));
    return PageableExecutionUtils.getPage(page, pageable, () -> mongoTemplate.count(query, clazz));
  }

  @CacheEvict(allEntries = true)
  public boolean remove(final D document) {
    return mongoTemplate.remove(document).getDeletedCount() > 0;
  }

  protected Page<D> find(final Query query, final Pageable pageable) {
    final var page = Collections.unmodifiableList(mongoTemplate.find(query.with(pageable), clazz));
    return PageableExecutionUtils.getPage(page, pageable, () -> mongoTemplate.count(query.with(pageable), clazz));
  }

  protected D findOne(final Query query) {
    return mongoTemplate.findOne(query, clazz);
  }

}
