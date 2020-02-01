package com.dayanfcosta.financialcontrol;

import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@DataMongoTest
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
public class AbstractRepositoryTest {

  @Autowired
  private MongoTemplate mongoTemplate;

  private final List<Class> documents = new ArrayList<>();

  @BeforeEach
  public void setUp() {
    documents.forEach(documentClass -> mongoTemplate.remove(new Query(), documentClass));
  }

  @AfterEach
  public void tearDown() {
    documents.forEach(documentClass -> mongoTemplate.remove(new Query(), documentClass));
  }

  protected MongoTemplate getMongoTemplate() {
    return mongoTemplate;
  }

  protected void addDocumentsToClear(final Class... classes) {
    documents.addAll(asList(classes));
  }
}
