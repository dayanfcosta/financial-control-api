package com.dayanfcosta.financialcontrol.config;

import com.mongodb.WriteConcern;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.WriteConcernResolver;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * @author dayanfcosta
 */
@Configuration
@EnableMongoRepositories
public class DatabaseConfig {

  @Bean
  @Primary
  MongoTemplate mongoTemplate(MongoDbFactory dbFactory) {
    var template = new MongoTemplate(dbFactory);
    template.setWriteConcernResolver(writeConcernResolver());
    return template;
  }

  @Bean
  @Primary
  MongoTransactionManager transactionManager(MongoDbFactory dbFactory) {
    return new MongoTransactionManager(dbFactory);
  }

  private WriteConcernResolver writeConcernResolver() {
    return action -> {
      if (action.getClass().getSimpleName().contains("Audit"))
        return WriteConcern.UNACKNOWLEDGED;
      else if (action.getClass().getSimpleName().contains("Metadata"))
        return WriteConcern.JOURNALED;
      return action.getDefaultWriteConcern();
    };
  }
}
