package com.dayanfcosta.financialcontrol.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.dayanfcosta.financialcontrol.AbstractRepositoryTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Pageable;

/**
 * @author dayanfcosta
 */
class UserRepositoryTest extends AbstractRepositoryTest {

  private User user;
  private UserRepository repository;

  public UserRepositoryTest() {
    addDocumentsToClear(User.class);
  }

  @Override
  @BeforeEach
  public void setUp() {
    super.setUp();
    repository = new UserRepository(getMongoTemplate());
    user = UserBuilder.create("email@email.com")
        .withPassword("1234")
        .withEnabledStatus(true)
        .withName("User")
        .build();
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