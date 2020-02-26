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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;

class TransactionServiceTest {

  private TransactionRepository repository;
  private TransactionTagService tagService;
  private TransactionService transactionService;

  private User user;
  private Transaction transaction;

  @BeforeEach
  void setUp() {
    repository = mock(TransactionRepository.class);
    tagService = mock(TransactionTagService.class);
    transactionService = new TransactionService(repository, tagService);

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
    when(tagService.findById(any())).thenReturn(null);

    transactionService.save(new TransactionDto(transaction), user);

    verify(repository, times(1)).save(any());
    verify(tagService, times(0)).findById(any());
  }

  @Test
  void testSave_withTag() {
    when(tagService.findById(any())).thenReturn(new TransactionTag("1", "xpto"));

    transaction = TransactionBuilder
        .create(user, now(), EUR, ONE, INCOME)
        .withTag(new TransactionTag("1", "xpto"))
        .build();
    transactionService.save(new TransactionDto(transaction), user);

    verify(repository, times(1)).save(any());
    verify(tagService, times(1)).findById(any());
  }

  @Test
  void testUpdate() {
    when(repository.findById(any())).thenReturn(of(transaction));

    transactionService.update("1", new TransactionDto(transaction), user);

    verify(repository, times(1)).save(any());
    verify(tagService, times(0)).findById(any());
  }

  @Test
  void testRemove() {
    when(repository.findById(any())).thenReturn(of(transaction));

    transactionService.remove("1", user);

    verify(repository, times(1)).remove(any());
    verify(repository, times(1)).findById(any());
  }

  @Test
  void testRemove_differentOwner() {
    when(repository.findById(any())).thenReturn(of(transaction));

    final var newUser = UserBuilder.create("user@user.com")
        .withPassword("pass")
        .withName("User")
        .build();

    assertThatThrownBy(() -> transactionService.remove("1", newUser))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("You are not the owner of this transaction");
  }

  @Test
  void testFindAll_byFromDate() {
    when(repository.findByDateInterval(any(), any(), any(), any())).thenReturn(Page.empty());

    transactionService.findAll(user, now(), null, unpaged());

    verify(repository, times(1)).findByDateInterval(user, now(), null, unpaged());
  }

  @Test
  void testFindAll_byUntilDate() {
    when(repository.findByDateInterval(any(), any(), any(), any())).thenReturn(Page.empty());

    transactionService.findAll(user, null, now(), unpaged());

    verify(repository, times(1)).findByDateInterval(user, null, now(), unpaged());
  }

  @Test
  void testFindAll_byDateInterval() {
    when(repository.findByDateInterval(any(), any(), any(), any())).thenReturn(Page.empty());

    transactionService.findAll(user, now(), now(), unpaged());

    verify(repository, times(1)).findByDateInterval(user, now(), now(), unpaged());
  }

  @Test
  void testFindAll() {
    when(repository.findAll(any(), any())).thenReturn(Page.empty());

    transactionService.findAll(user, null, null, unpaged());

    verify(repository, times(1)).findAll(user, unpaged());
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
    when(repository.findByDate(any(), any(), any())).thenReturn(Page.empty());

    final var page = transactionService.findByDate(user, now(), unpaged());

    verify(repository, times(1)).findByDate(user, now(), unpaged());
    assertThat(page.isEmpty()).isTrue();
  }
}