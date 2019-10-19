package com.dayanfcosta.financialcontrol.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.core.MongoTemplate;
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
    return new MongoTemplate(dbFactory);
  }

  @Bean
  @Primary
  MongoTransactionManager transactionManager(MongoDbFactory dbFactory) {
    return new MongoTransactionManager(dbFactory);
  }

}
