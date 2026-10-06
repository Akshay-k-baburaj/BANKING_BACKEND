package com.learning.Bank.controller;

import com.learning.Bank.dto.*;
import com.learning.Bank.service.AccountService;
import com.learning.Bank.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/accounts")
public class AccountController {
    private final AccountService accountService;
    private final TransactionService transactionService;

    public AccountController(AccountService accountService, TransactionService transactionService){
        this.accountService = accountService;
        this.transactionService = transactionService;
    }

    @PostMapping
    public AccountResponse createAccount(@Valid @RequestBody AccountRequest request){
        return accountService.createAccount(request);
    }

    @GetMapping("/{id}")
    public AccountResponse getAccountById(@PathVariable Long id){
        return accountService.getAccountById(id);
    }

    @GetMapping
    public List<AccountResponse> getAllAccounts(){
        return accountService.getAllAccounts();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long id){
        accountService.deleteAccount(id);
    }

    @PutMapping("/{id}")
    public AccountResponse updateAccountById(@Valid @PathVariable Long id, @RequestBody AccountUpdateRequest request){
        return accountService.updateAccount(id, request);
    }

    @PostMapping("/{id}/deposit")
    public AccountResponse deposit(@PathVariable Long id, @Valid @RequestBody TransactionRequest request){
        return accountService.deposit(id,request);
    }

    @PostMapping("/{id}/withdraw")
    public AccountResponse withdraw(@PathVariable Long id, @Valid @RequestBody TransactionRequest request){
        return accountService.withdraw(id,request);
    }

    @GetMapping("/{id}/transactions")
    public Page<TransactionResponse> transactionHistory(@PathVariable Long id, Pageable pageable){
        return transactionService.getTransactionByAccountId(id, pageable);
    }
}
