package com.dayanfcosta.financialcontrol.transaction;

import com.dayanfcosta.financialcontrol.commons.ResourceUtils;
import com.dayanfcosta.financialcontrol.config.openapi.CreateApiResponse;
import com.dayanfcosta.financialcontrol.config.openapi.PageableParameter;
import com.dayanfcosta.financialcontrol.config.openapi.QueryApiResponse;
import com.dayanfcosta.financialcontrol.config.openapi.UpdateApiResponse;
import com.dayanfcosta.financialcontrol.user.User;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.net.URISyntaxException;
import java.time.LocalDate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transactions")
@Tag(name = "Transactions Resources")
public class TransactionResource {

  private final TransactionTagService tagService;
  private final TransactionService transactionService;

  public TransactionResource(final TransactionTagService tagService, final TransactionService transactionService) {
    this.tagService = tagService;
    this.transactionService = transactionService;
  }

  @PostMapping
  @CreateApiResponse(summary = "Register a new transaction")
  public ResponseEntity<TransactionDto> addTransaction(@RequestBody final TransactionForm form,
      @Parameter(hidden = true) final Authentication authentication) throws URISyntaxException {
    final var transaction = transactionService.save(form, currentUser(authentication));
    final var resourceUri = ResourceUtils.uri("/transactions", transaction);
    return ResponseEntity
        .created(resourceUri)
        .body(new TransactionDto(transaction));
  }

  @PutMapping("/{id}")
  @UpdateApiResponse(summary = "Update the transaction with the given ID")
  public void updateTransaction(@PathVariable("id") final String id, @RequestBody final TransactionForm form,
      @Parameter(hidden = true) final Authentication authentication) {
    transactionService.update(id, form, currentUser(authentication));
  }

  @GetMapping("/{id}")
  @QueryApiResponse(summary = "Find a transaction with the given ID")
  public TransactionDto transactionById(@PathVariable("id") final String id) {
    final var transaction = transactionService.findById(id);
    return new TransactionDto(transaction);
  }

  @GetMapping("/date")
  @PageableParameter(summary = "Find all transactions from the current user with the given date")
  public Page<TransactionDto> transactionsByDate(@RequestParam("date") final LocalDate date,
      @Parameter(hidden = true) final Pageable pageable, @Parameter(hidden = true) final Authentication authentication) {
    return transactionService.findByDate(date, currentUser(authentication), pageable).map(TransactionDto::new);
  }

  @GetMapping
  @PageableParameter(summary = "Find all transactions from the current user with the given date interval")
  public Page<TransactionDto> transactionsByInterval(@RequestParam("startDate") final LocalDate startDate,
      @RequestParam("endDate") final LocalDate endDate, @Parameter(hidden = true) final Pageable pageable,
      @Parameter(hidden = true) final Authentication authentication) {
    return transactionService.findAll(startDate, endDate, currentUser(authentication), pageable).map(TransactionDto::new);
  }

  private User currentUser(final Authentication authentication) {
    return (User) authentication.getPrincipal();
  }
}
