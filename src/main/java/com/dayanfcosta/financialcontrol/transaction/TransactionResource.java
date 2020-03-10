package com.dayanfcosta.financialcontrol.transaction;

import com.dayanfcosta.financialcontrol.commons.ResourceUtils;
import com.dayanfcosta.financialcontrol.config.rest.HttpErrorResponse;
import com.dayanfcosta.financialcontrol.user.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

@Tag(name = "Transactions Resources")
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
  @Operation(summary = "Register a new transaction")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "transaction created", content = @Content(schema = @Schema(implementation = TransactionDto.class))),
      @ApiResponse(responseCode = "401", description = "not authenticated request", content = @Content(schema = @Schema(implementation = HttpErrorResponse.class))),
      @ApiResponse(responseCode = "500", description = "internal server error", content = @Content(schema = @Schema(implementation = HttpErrorResponse.class)))
  })
  public ResponseEntity<TransactionDto> addTransaction(@RequestBody final TransactionDto dto, final Authentication authentication)
      throws URISyntaxException {
    final var transaction = transactionService.save(dto, currentUser(authentication));
    final var resourceUri = ResourceUtils.uri("/transactions", transaction);
    return ResponseEntity
        .created(resourceUri)
        .body(new TransactionDto(transaction));
  }

  @PutMapping("/{id}")
  @Operation(summary = "Update the transaction with the given ID")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "transaction updated"),
      @ApiResponse(responseCode = "401", description = "not authenticated request", content = @Content(schema = @Schema(implementation = HttpErrorResponse.class))),
      @ApiResponse(responseCode = "500", description = "internal server error", content = @Content(schema = @Schema(implementation = HttpErrorResponse.class)))
  })
  public void updateTransaction(@PathVariable("id") final String id, @RequestBody final TransactionDto dto,
      final Authentication authentication) {
    transactionService.update(id, dto, currentUser(authentication));
  }

  @GetMapping("/{id}")
  @Operation(summary = "Find a transaction with the given ID")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "transaction found", content = @Content(schema = @Schema(implementation = TransactionDto.class))),
      @ApiResponse(responseCode = "401", description = "not authenticated request", content = @Content(schema = @Schema(implementation = HttpErrorResponse.class))),
      @ApiResponse(responseCode = "500", description = "internal server error", content = @Content(schema = @Schema(implementation = HttpErrorResponse.class)))
  })
  public TransactionDto transactionById(@PathVariable("id") final String id) {
    final var transaction = transactionService.findById(id);
    return new TransactionDto(transaction);
  }

  @GetMapping("/date")
  public Page<TransactionDto> transactionsByDate(@RequestParam("date") final LocalDate date, final Pageable pageable,
      final Authentication authentication) {
    return transactionService.findByDate(date, currentUser(authentication), pageable).map(TransactionDto::new);
  }

  @GetMapping
  public Page<TransactionDto> transactionsByInterval(@RequestParam("startDate") final LocalDate startDate,
      @RequestParam("endDate") final LocalDate endDate, final Pageable pageable, final Authentication authentication) {
    return transactionService.findAll(startDate, endDate, currentUser(authentication), pageable).map(TransactionDto::new);
  }

  private User currentUser(final Authentication authentication) {
    return (User) authentication.getPrincipal();
  }
}
