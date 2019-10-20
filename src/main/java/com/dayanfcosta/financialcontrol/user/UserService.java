package com.dayanfcosta.financialcontrol.user;

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
public class UserService {

  private final UserRepository repository;
  private final PasswordEncoder passwordEncoder;

  public UserService(UserRepository repository, PasswordEncoder passwordEncoder) {
    this.passwordEncoder = passwordEncoder;
    this.repository = repository;
  }

  @Transactional
  public User save(UserDto dto) {
    validateDuplicatedInsert(dto);
    var user = UserBuilder.create(dto.getEmail())
        .name(dto.getName())
        .password(passwordEncoder.encode(dto.getPassword()))
        .build();
    return repository.save(user);
  }

  @Transactional
  public void update(String id, UserDto dto) {
    var user = repository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Doesn't exist a user with the specified id"));
    validateUpdate(user, dto);
    var updated = updatedUser(user, dto);
    repository.save(updated);
  }

  @Transactional(readOnly = true)
  public User findById(String id) {
    return repository.findById(id).orElseThrow(() -> new NullPointerException("User doesn't exist"));
  }

  @Transactional(readOnly = true)
  public Page<User> findAll(Pageable pageable) {
    return repository.findAll(pageable);
  }

  private User updatedUser(User user, UserDto dto) {
    return UserBuilder.create(dto.getEmail())
        .password(user.getPassword())
        .enabled(user.isEnabled())
        .id(user.getId())
        .name(dto.getName())
        .build();
  }

  private void validateDuplicatedInsert(UserDto dto) {
    var existing = repository.findByEmail(dto.getEmail());
    if (existing.isPresent()) {
      throw new DuplicateKeyException("User e-mail is already in use");
    }
  }

  private void validateUpdate(User existing, UserDto dto) {
    repository.findByEmail(dto.getEmail())
        .ifPresent(user -> {
          if (!user.equals(existing)) {
            throw new IllegalArgumentException("The specified e-mail is already in use");
          }
        });
  }
}
