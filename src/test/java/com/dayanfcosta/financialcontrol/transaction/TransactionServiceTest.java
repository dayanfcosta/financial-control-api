package com.dayanfcosta.financialcontrol.transaction;

import static com.dayanfcosta.financialcontrol.transaction.Currency.EUR;
import static com.dayanfcosta.financialcontrol.transaction.TransactionType.INCOME;
import static java.math.BigDecimal.ONE;
import static java.time.LocalDate.now;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.data.domain.Pageable.unpaged;

import com.dayanfcosta.financialcontrol.user.User;
import com.dayanfcosta.financialcontrol.user.UserBuilder;
import com.dayanfcosta.financialcontrol.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;

class TransactionServiceTest {

  private UserService userService;
  private TransactionRepository repository;
  private TransactionTagService tagService;
  private TransactionService transactionService;

  private User user;
  private Transaction transaction;

  @BeforeEach
  void setUp() {
    userService = mock(UserService.class);
    repository = mock(TransactionRepository.class);
    tagService = mock(TransactionTagService.class);
    transactionService = new TransactionService(repository, userService, tagService);

    user = UserBuilder.create("email@email.com")
        .withPassword("pass")
        .withName("User")
        .build();

    transaction = TransactionBuilder
        .create(user, now(), EUR, ONE, INCOME)
        .build();
  }

  @Test
  void testSave_withoutTag() {
    when(userService.findById(any())).thenReturn(user);
    when(tagService.findById(any())).thenReturn(null);

    transactionService.save(new TransactionDto(transaction));

    verify(repository, times(1)).save(any());
    verify(tagService, times(0)).findById(any());
    verify(userService, times(1)).findById(any());
  }

  @Test
  void testSave_withTag() {
    when(userService.findById(any())).thenReturn(user);
    when(tagService.findById(any())).thenReturn(new TransactionTag("1", "xpto"));

    transaction = TransactionBuilder
        .create(user, now(), EUR, ONE, INCOME)
        .withTag(new TransactionTag("1", "xpto"))
        .build();
    transactionService.save(new TransactionDto(transaction));

    verify(repository, times(1)).save(any());
    verify(tagService, times(1)).findById(any());
    verify(userService, times(1)).findById(any());
  }

  @Test
  void testUpdate() {
    when(userService.findById(any())).thenReturn(user);
    when(repository.findById(any())).thenReturn(of(transaction));

    transactionService.update("1", new TransactionDto(transaction));

    verify(repository, times(1)).save(any());
    verify(tagService, times(0)).findById(any());
    verify(userService, times(1)).findById(any());
  }

  @Test
  void testRemove() {
    when(repository.findById(any())).thenReturn(of(transaction));

    transactionService.remove("1");

    verify(repository, times(1)).remove(any());
    verify(repository, times(1)).findById(any());
  }

  @Test
  void testFindAll_byFromDate() {
    when(repository.findByDateInterval(any(), any(), any())).thenReturn(Page.empty());

    transactionService.findAll(now(), null, unpaged());

    verify(repository, times(1)).findByDateInterval(now(), null, unpaged());
  }

  @Test
  void testFindAll_byUntilDate() {
    when(repository.findByDateInterval(any(), any(), any())).thenReturn(Page.empty());

    transactionService.findAll(null, now(), unpaged());

    verify(repository, times(1)).findByDateInterval(null, now(), unpaged());
  }

  @Test
  void testFindAll_byDateInterval() {
    when(repository.findByDateInterval(any(), any(), any())).thenReturn(Page.empty());

    transactionService.findAll(now(), now(), unpaged());

    verify(repository, times(1)).findByDateInterval(now(), now(), unpaged());
  }

  @Test
  void testFindAll() {
    when(repository.findAll(any())).thenReturn(Page.empty());

    transactionService.findAll(null, null, unpaged());

    verify(repository, times(1)).findAll(unpaged());
  }

  @Test
  void testFindById_validId() {
    when(repository.findById(any())).thenReturn(of(transaction));

    final var existent = transactionService.findById("1");

    assertThat(existent).isNotNull();
  }

  @Test
  void testFindById_invalidId() {
    when(repository.findById(any())).thenReturn(empty());

    assertThatThrownBy(() -> transactionService.findById("1"))
        .isInstanceOf(NullPointerException.class)
        .hasMessage("Transaction not found");
  }

  @Test
  void testFindByDate() {
    when(repository.findByDate(any(), any())).thenReturn(Page.empty());

    final var page = transactionService.findByDate(now(), unpaged());

    verify(repository, times(1)).findByDate(now(), unpaged());
    assertThat(page.isEmpty()).isTrue();
  }
}