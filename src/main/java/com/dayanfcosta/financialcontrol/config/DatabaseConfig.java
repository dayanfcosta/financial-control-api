package com.dayanfcosta.financialcontrol.config;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.data.mongodb.core.index.MongoPersistentEntityIndexResolver;
import org.springframework.data.mongodb.core.mapping.BasicMongoPersistentEntity;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * @author dayanfcosta
 */
@Configuration
@EnableMongoRepositories
public class DatabaseConfig {

  private final MongoTemplate mongoTemplate;
  private final MongoConverter mongoConverter;

  public DatabaseConfig(final MongoTemplate mongoTemplate, final MongoConverter mongoConverter) {
    this.mongoTemplate = mongoTemplate;
    this.mongoConverter = mongoConverter;
  }

  @EventListener(ApplicationReadyEvent.class)
  public void initIndicesAfterStartup() {
    final var mappingContext = mongoConverter.getMappingContext();
    if (mappingContext instanceof MongoMappingContext) {
      final var mongoMappingContext = (MongoMappingContext) mappingContext;
      for (final BasicMongoPersistentEntity<?> persistentEntity : mongoMappingContext.getPersistentEntities()) {
        final var clazz = persistentEntity.getType();
        if (clazz.isAnnotationPresent(Document.class)) {
          final var indexOps = mongoTemplate.indexOps(clazz);
          final var resolver = new MongoPersistentEntityIndexResolver(mongoMappingContext);
          resolver.resolveIndexFor(clazz).forEach(indexOps::ensureIndex);
        }
      }
    }
  }
}
