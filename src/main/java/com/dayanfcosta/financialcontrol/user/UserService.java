package com.dayanfcosta.financialcontrol.user;

import com.dayanfcosta.financialcontrol.commons.DuplicateKeyException;
import org.apache.commons.lang3.Validate;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * @author dayanfcosta
 */
@Service
public class UserService {

  private final UserRepository repository;
  private final PasswordEncoder passwordEncoder;

  UserService(final UserRepository repository, final PasswordEncoder passwordEncoder) {
    this.passwordEncoder = passwordEncoder;
    this.repository = repository;
  }

  public User findById(final String id) {
    return repository.findById(id).orElseThrow(() -> new NullPointerException("User not found"));
  }

  public User findByEmail(final String email) {
    return repository.findByEmail(email).orElseThrow(() -> new NullPointerException("User not found"));
  }

  @DuplicateKeyException("User e-mail is already in use")
  User save(final UserDto dto) {
    final var encodedPassword = passwordEncoder.encode(dto.getPassword());
    final var user = UserBuilder.create(dto.getEmail())
        .withPassword(encodedPassword)
        .withName(dto.getName())
        .build();
    return repository.save(user);
  }

  @Caching(evict = {@CacheEvict(key = "#result.id"), @CacheEvict(key = "#result.email")})
  void update(final String id, final UserDto dto) {
    final var user = findById(id);
    validateUpdate(user, dto);
    final var updated = updatedUser(user, dto);
    repository.save(updated);
  }

  @Caching(put = {@CachePut(key = "#result.id"), @CachePut(key = "#result.email")})
  Page<User> findAll(final Pageable pageable) {
    return repository.findAll(pageable);
  }

  @Caching(evict = {@CacheEvict(key = "#result.id"), @CacheEvict(key = "#result.email")})
  User enable(final String id) {
    final User user = findById(id);
    Validate.isTrue(!user.isEnabled(), "User already enabled");
    user.enable();
    repository.save(user);
    return user;
  }

  @Caching(evict = {@CacheEvict(key = "#result.id"), @CacheEvict(key = "#result.email")})
  User disable(final String id) {
    final User user = findById(id);
    Validate.isTrue(user.isEnabled(), "User already disabled");
    user.disable();
    repository.save(user);
    return user;
  }

  private User updatedUser(final User user, final UserDto dto) {
    return UserBuilder.create(dto.getEmail())
        .withPassword(user.getPassword())
        .withEnabledStatus(user.isEnabled())
        .withId(user.getId())
        .withName(dto.getName())
        .build();
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
