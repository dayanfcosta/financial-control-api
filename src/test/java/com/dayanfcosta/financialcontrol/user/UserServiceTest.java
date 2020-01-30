package com.dayanfcosta.financialcontrol.user;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.data.domain.Pageable.unpaged;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author dayanfcosta
 */
class UserServiceTest {

  private User user;
  private UserService service;
  private UserRepository repository;
  private PasswordEncoder passwordEncoder;

  @BeforeEach
  void setUp() {
    repository = mock(UserRepository.class);
    passwordEncoder = mock(PasswordEncoder.class);
    service = new UserService(repository, passwordEncoder);

    user = UserBuilder.create("email@email.com")
        .password("pass")
        .name("User")
        .build();
  }

  @Test
  void testSave() {
    // given
    when(passwordEncoder.encode(any())).thenReturn(new BCryptPasswordEncoder().encode(user.getPassword()));
    when(repository.findByEmail(any())).thenReturn(empty());

    // when
    service.save(new UserDto(user));

    // then
    verify(repository, times(1)).save(any());
    verify(repository, times(1)).findByEmail(user.getEmail());
    verify(passwordEncoder, times(1)).encode(user.getPassword());
  }

  @Test
  void testSave_Duplicated() {
    // given
    when(repository.findByEmail(any())).thenReturn(of(user));

    // when - then
    assertThatThrownBy(() -> service.save(new UserDto(user)))
        .isInstanceOf(DuplicateKeyException.class)
        .hasMessage("User e-mail is already in use");
  }

  @Test
  void testUpdate_InvalidID() {
    // given
    when(repository.findById(any())).thenReturn(empty());

    // when -> then
    assertThatThrownBy(() -> service.update("1", new UserDto(user)))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("There isn't any user with the specified id");
  }

  @Test
  void testUpdate_UserNotExists() {
    // given
    when(repository.findById(any())).thenReturn(empty());

    // when - then
    assertThatThrownBy(() -> service.update("1", new UserDto(user)))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("There isn't any user with the specified id");
  }

  @Test
  void testUpdate_EmailInUse() {
    // given
    final var newUser = UserBuilder.create("xpto@email.com").name("Xpto").password("pass").build();
    when(repository.findById(any())).thenReturn(of(newUser));
    when(repository.findByEmail(any())).thenReturn(of(user));

    // when - then
    assertThatThrownBy(() -> service.update("1", new UserDto(newUser)))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("The specified e-mail is already in use");
  }

  @Test
  void testUpdate() {
    // given
    final var userWithId = UserBuilder.create(user.getEmail()).id("1").name(user.getName()).password(user.getPassword()).build();
    when(repository.findById(any())).thenReturn(of(userWithId));
    when(repository.findByEmail(any())).thenReturn(of(userWithId));

    // when
    final var newUser = UserBuilder.create("email@email.com").password("pass").name("User Updated").id("1").build();
    service.update("1", new UserDto(newUser));

    // then
    verify(repository, times(1)).save(any());
  }

  @Test
  void testFindById_NotFound() {
    // given
    when(repository.findById(any())).thenReturn(empty());

    // when -> then
    assertThatThrownBy(() -> service.findById("1"))
        .isInstanceOf(NullPointerException.class)
        .hasMessage("User not found");
  }

  @Test
  void testFindById() {
    // given
    when(repository.findById(any())).thenReturn(of(user));

    // when
    final var withId = service.findById("1");

    // then
    assertThat(withId).isNotNull();
  }

  @Test
  void testFindAll() {
    // given
    when(repository.findAll(any())).thenReturn(new PageImpl<>(List.of(user), unpaged(), 1));

    // when
    final var page = service.findAll(unpaged());

    // then
    assertThat(page).isNotNull();
  }

  @Test
  void testEnable_userAlreadyEnabled() {
    // given
    when(repository.findById(any())).thenReturn(of(user));

    // when
    assertThatThrownBy(() -> service.enable("1"))
        .isInstanceOf(RuntimeException.class)
        .hasMessage("User already enabled");
  }

  @Test
  void testEnable_userEnabledWithSuccess() {
    // given
    user.disable();
    when(repository.findById(any())).thenReturn(of(user));

    // when
    final User enabledUser = service.enable(user.getId());

    // then
    assertThat(enabledUser.isEnabled()).isTrue();
  }

  @Test
  void testDisable_userAlreadyDisabled() {
    // given
    user.disable();
    when(repository.findById(any())).thenReturn(of(user));

    // when
    assertThatThrownBy(() -> service.disable("1"))
        .isInstanceOf(RuntimeException.class)
        .hasMessage("User already disabled");
  }

  @Test
  void testDisable_userDisabledWithSuccess() {
    // given
    when(repository.findById(any())).thenReturn(of(user));

    // when
    final User enabledUser = service.disable(user.getId());

    // then
    assertThat(enabledUser.isEnabled()).isFalse();
  }

}