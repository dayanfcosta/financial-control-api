package com.dayanfcosta.financialcontrol.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * @author dayanfcosta
 */
@DataMongoTest
@ExtendWith(SpringExtension.class)
class UserRepositoryTest {

  @Autowired
  private MongoTemplate mongoTemplate;

  private UserRepository repository;

  private User user;

  @BeforeEach
  void setUp() {
    repository = new UserRepository(mongoTemplate);
    user = UserBuilder.create("email@email.com")
        .withPassword("1234")
        .withEnabledStatus(true)
        .withName("User")
        .build();
  }

  @AfterEach
  void tearDown() {
    mongoTemplate.remove(new Query(), User.class);
  }

  @Test
  void testSave() {
    // when
    final var saved = repository.save(user);

    // then
    assertThat(saved.getId()).isNotNull();
  }

  @Test
  void testSave_Duplicated() {
    // given
    final var user1 = UserBuilder.create(user.getEmail()).withPassword("pass").withName("User").build();
    repository.save(user);

    // when - then
    assertThatThrownBy(() -> repository.save(user1))
        .isInstanceOf(DuplicateKeyException.class);
  }

  @Test
  void testFindById() {
    // given
    repository.save(user);

    // when
    final var existing = repository.findById(user.getId());

    // then
    assertThat(existing).isNotEmpty();
    assertThat(existing.get()).isEqualTo(user);
  }

  @Test
  void testFindAll() {
    // given
    final var initialSize = repository.findAll(Pageable.unpaged()).getSize();
    repository.save(user);

    // when
    final var users = repository.findAll(Pageable.unpaged());

    // then
    assertThat(users).isNotEmpty();
    assertThat(users).hasSize(initialSize + 1);
  }
}