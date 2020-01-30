package com.dayanfcosta.financialcontrol.user;

import org.apache.commons.lang3.Validate;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author dayanfcosta
 */
@Service
class UserService {

  private final UserRepository repository;
  private final PasswordEncoder passwordEncoder;

  UserService(final UserRepository repository, final PasswordEncoder passwordEncoder) {
    this.passwordEncoder = passwordEncoder;
    this.repository = repository;
  }

  @Transactional
  User save(final UserDto dto) {
    validateDuplicatedInsert(dto);
    final var user = UserBuilder.create(dto.getEmail())
        .name(dto.getName())
        .password(passwordEncoder.encode(dto.getPassword()))
        .build();
    return repository.save(user);
  }

  @Transactional
  void update(final String id, final UserDto dto) {
    final var user = repository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("There isn't any user with the specified id"));
    validateUpdate(user, dto);
    final var updated = updatedUser(user, dto);
    repository.save(updated);
  }

  @Transactional(readOnly = true)
  User findById(final String id) {
    return repository.findById(id).orElseThrow(() -> new NullPointerException("User not found"));
  }

  @Transactional(readOnly = true)
  Page<User> findAll(final Pageable pageable) {
    return repository.findAll(pageable);
  }

  User enable(final String id) {
    final User user = findById(id);
    Validate.isTrue(!user.isEnabled(), "User already enabled");
    user.enable();
    repository.save(user);
    return user;
  }

  User disable(final String id) {
    final User user = findById(id);
    Validate.isTrue(user.isEnabled(), "User already disabled");
    user.disable();
    repository.save(user);
    return user;
  }

  private User updatedUser(final User user, final UserDto dto) {
    return UserBuilder.create(dto.getEmail())
        .password(user.getPassword())
        .enabled(user.isEnabled())
        .id(user.getId())
        .name(dto.getName())
        .build();
  }

  private void validateDuplicatedInsert(final UserDto dto) {
    final var existing = repository.findByEmail(dto.getEmail());
    if (existing.isPresent()) {
      throw new DuplicateKeyException("User e-mail is already in use");
    }
  }

  private void validateUpdate(final User existing, final UserDto dto) {
    repository.findByEmail(dto.getEmail())
        .ifPresent(user -> {
          if (!user.equals(existing)) {
            throw new IllegalArgumentException("The specified e-mail is already in use");
          }
        });
  }
}
