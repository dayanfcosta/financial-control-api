package com.dayanfcosta.financialcontrol.transaction;

import com.dayanfcosta.financialcontrol.commons.ResourceUtils;
import com.dayanfcosta.financialcontrol.user.User;
import java.net.URISyntaxException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transactions")
public class TransactionResource {

  private final TransactionTagService tagService;
  private final TransactionService transactionService;

  public TransactionResource(final TransactionTagService tagService, final TransactionService transactionService) {
    this.tagService = tagService;
    this.transactionService = transactionService;
  }

  @PostMapping
  public ResponseEntity<TransactionDto> addTransaction(final Authentication authentication, @RequestBody final TransactionDto dto)
      throws URISyntaxException {
    final var transaction = transactionService.save(dto, (User) authentication.getPrincipal());
    final var resourceUri = ResourceUtils.uri("/transactions", transaction);
    return ResponseEntity
        .created(resourceUri)
        .body(new TransactionDto(transaction));
  }

  @PutMapping("/{id}")
  public void updateTransaction(final Authentication authentication, @RequestParam("id") final String id,
      @RequestBody final TransactionDto dto) {
    transactionService.update(id, dto, (User) authentication.getPrincipal());
  }

  @GetMapping("/{id}")
  public TransactionDto transactionById(@RequestParam("id") final String id) {
    final var transaction = transactionService.findById(id);
    return new TransactionDto(transaction);
  }

}
