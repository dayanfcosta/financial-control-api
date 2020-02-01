package com.dayanfcosta.financialcontrol;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@DataMongoTest
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
public class AbstractRepositoryTest {

  @Autowired
  private MongoTemplate mongoTemplate;

  protected MongoTemplate getMongoTemplate() {
    return mongoTemplate;
  }
}
