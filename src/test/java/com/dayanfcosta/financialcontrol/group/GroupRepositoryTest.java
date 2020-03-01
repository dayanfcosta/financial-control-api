package com.dayanfcosta.financialcontrol.group;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.dayanfcosta.financialcontrol.AbstractRepositoryTest;
import com.dayanfcosta.financialcontrol.user.User;
import com.dayanfcosta.financialcontrol.user.UserBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Pageable;

class GroupRepositoryTest extends AbstractRepositoryTest {

  private User user;
  private User owner;
  private Group group;

  private GroupRepository repository;

  public GroupRepositoryTest() {
    addDocumentsToClear(User.class, Group.class);
  }

  @BeforeEach
  void setUpGroups() {
    repository = new GroupRepository(getMongoTemplate());

    user = UserBuilder.create("user@user.com")
        .withPassword("1234")
        .withName("User")
        .build();

    owner = UserBuilder.create("owner@owner.com")
        .withPassword("1234")
        .withName("Owner")
        .build();

    group = GroupBuilder.create(owner, "group1").build();

    getMongoTemplate().save(user);
    getMongoTemplate().save(owner);
    repository.save(group);
  }

  @Test
  void testFindByUser_GroupsFromOwner() {
    final var page = repository.findByUser(owner, Pageable.unpaged());

    assertThat(page.getContent()).hasSize(1);
  }

  @Test
  void testFindByUser_GroupsFromUser() {
    group.addUser(user);
    repository.save(group);

    final var page = repository.findByUser(user, Pageable.unpaged());

    assertThat(page.getContent()).hasSize(1);
  }

  @Test
  void testSave_Duplicated() {
    final var newGroup = GroupBuilder.create(owner, "group1").build();

    assertThatThrownBy(() -> repository.save(newGroup))
        .isInstanceOf(DuplicateKeyException.class);
  }
}