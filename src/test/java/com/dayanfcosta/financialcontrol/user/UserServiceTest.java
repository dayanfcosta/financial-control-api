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
        .withPassword("pass")
        .withName("User")
        .build();
  }

  @Test
  void testSave() {
    when(passwordEncoder.encode(any())).thenReturn(new BCryptPasswordEncoder().encode(user.getPassword()));
    when(repository.findByEmail(any())).thenReturn(empty());

    service.save(new UserDto(user));

    verify(repository, times(1)).save(any());
    verify(repository, times(1)).findByEmail(user.getEmail());
    verify(passwordEncoder, times(1)).encode(user.getPassword());
  }

  @Test
  void testSave_Duplicated() {
    when(repository.findByEmail(any())).thenReturn(of(user));

    assertThatThrownBy(() -> service.save(new UserDto(user)))
        .isInstanceOf(DuplicateKeyException.class)
        .hasMessage("User e-mail is already in use");
  }

  @Test
  void testUpdate_InvalidId() {
    when(repository.findById(any())).thenReturn(empty());

    assertThatThrownBy(() -> service.update("1", new UserDto(user)))
        .isInstanceOf(NullPointerException.class)
        .hasMessage("User not found");
  }

  @Test
  void testUpdate_UserNotExists() {
    when(repository.findById(any())).thenReturn(empty());

    assertThatThrownBy(() -> service.update("1", new UserDto(user)))
        .isInstanceOf(NullPointerException.class)
        .hasMessage("User not found");
  }

  @Test
  void testUpdate_EmailInUse() {
    final var newUser = UserBuilder.create("xpto@email.com").withName("Xpto").withPassword("pass").build();
    when(repository.findById(any())).thenReturn(of(newUser));
    when(repository.findByEmail(any())).thenReturn(of(user));

    assertThatThrownBy(() -> service.update("1", new UserDto(newUser)))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("The specified e-mail is already in use");
  }

  @Test
  void testUpdate() {
    final var userWithId = UserBuilder.create(user.getEmail()).withId("1").withName(user.getName()).withPassword(user.getPassword())
        .build();
    when(repository.findById(any())).thenReturn(of(userWithId));
    when(repository.findByEmail(any())).thenReturn(of(userWithId));

    final var newUser = UserBuilder.create("email@email.com").withPassword("pass").withName("User Updated").withId("1").build();
    service.update("1", new UserDto(newUser));

    verify(repository, times(1)).save(any());
  }

  @Test
  void testFindById_NotFound() {
    when(repository.findById(any())).thenReturn(empty());

    assertThatThrownBy(() -> service.findById("1"))
        .isInstanceOf(NullPointerException.class)
        .hasMessage("User not found");
  }

  @Test
  void testFindById() {
    when(repository.findById(any())).thenReturn(of(user));

    final var withId = service.findById("1");

    assertThat(withId).isNotNull();
  }

  @Test
  void testFindAll() {
    when(repository.findAll(any())).thenReturn(new PageImpl<>(List.of(user), unpaged(), 1));

    final var page = service.findAll(unpaged());

    assertThat(page).isNotNull();
  }

  @Test
  void testEnable_userAlreadyEnabled() {
    when(repository.findById(any())).thenReturn(of(user));

    assertThatThrownBy(() -> service.enable("1"))
        .isInstanceOf(RuntimeException.class)
        .hasMessage("User already enabled");
  }

  @Test
  void testEnable_userEnabledWithSuccess() {
    user.disable();
    when(repository.findById(any())).thenReturn(of(user));

    final User enabledUser = service.enable(user.getId());

    verify(repository, times(1)).save(any());
    assertThat(enabledUser.isEnabled()).isTrue();
  }

  @Test
  void testDisable_userAlreadyDisabled() {
    user.disable();
    when(repository.findById(any())).thenReturn(of(user));

    assertThatThrownBy(() -> service.disable("1"))
        .isInstanceOf(RuntimeException.class)
        .hasMessage("User already disabled");
  }

  @Test
  void testDisable_userDisabledWithSuccess() {
    when(repository.findById(any())).thenReturn(of(user));

    final User enabledUser = service.disable(user.getId());

    verify(repository, times(1)).save(any());
    assertThat(enabledUser.isEnabled()).isFalse();
  }

}