package com.learning.Bank.service;

import com.learning.Bank.dto.AccountRequest;
import com.learning.Bank.dto.AccountResponse;
import com.learning.Bank.dto.AccountUpdateRequest;
import com.learning.Bank.dto.TransactionRequest;
import com.learning.Bank.entity.Account;
import com.learning.Bank.entity.TransactionType;
import com.learning.Bank.exception.AccountNotFoundException;
import com.learning.Bank.exception.InsufficientBalanceException;
import com.learning.Bank.mapper.AccountMapper;
import com.learning.Bank.repository.AccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final SecureRandom secureRandom = new SecureRandom();
    private final AccountMapper accountMapper;
    private final TransactionService transactionService;
    public AccountService(AccountRepository accountRepository, AccountMapper accountMapper, TransactionService transactionService){
        this.accountRepository = accountRepository;
        this.accountMapper = accountMapper;
        this.transactionService = transactionService;
    }

    public AccountResponse createAccount(AccountRequest request){
        Account account = accountMapper.toEntity(request);
        account.setBalance(BigDecimal.ZERO);
        account.setCreatedAt(LocalDateTime.now());

        long accountNumber = 1_000_000_000L + secureRandom.nextInt(900_000_000);
        account.setAccountNumber(accountNumber);

        Account savedAccount = accountRepository.save(account);

//        AccountResponse response = new AccountResponse();
//        response.setId(savedAccount.getId());
//        response.setAccountNumber(savedAccount.getAccountNumber());
//        response.setAccountHolderName(savedAccount.getAccountHolderName());
//        response.setBalance(savedAccount.getBalance());
//        response.setAccountType(savedAccount.getAccountType());
//        response.setCreatedAt(savedAccount.getCreatedAt());

        return accountMapper.toResponse(savedAccount);
    }

    public AccountResponse getAccountById(Long id){
        Optional<Account> account = accountRepository.findById(id);
        if(account.isEmpty()){
            throw new AccountNotFoundException("Account not found " + id);
        }
        return accountMapper.toResponse(account.get());
    }

    public List<AccountResponse> getAllAccounts(){
        List<Account> accounts = accountRepository.findAll();
        return accounts.stream()
                .map(accountMapper::toResponse)
                .toList();
    }

    public void deleteAccount(Long id){
        Optional<Account> account = accountRepository.findById(id);
        if(account.isEmpty()){
            throw new AccountNotFoundException("Account not found " + id);
        }
        accountRepository.deleteById(id);
    }

    public AccountResponse updateAccount(Long id, AccountUpdateRequest request){
        Optional<Account> account = accountRepository.findById(id);
        if(account.isEmpty()){
            throw new AccountNotFoundException("Account not found " + id);
        }
        Account existingAccount = account.get();
        existingAccount.setAccountHolderName(request.getAccountHolderName());
        existingAccount.setAccountType(request.getAccountType());
        Account savedAccount = accountRepository.save(existingAccount);
        return accountMapper.toResponse(savedAccount);
    }

    @Transactional
    public AccountResponse deposit(Long id, TransactionRequest request){
        Optional<Account> account = accountRepository.findById(id);
        if(account.isEmpty()){
            throw new AccountNotFoundException("Account not found with " + id);
        }
        Account existingAccount = account.get();
        BigDecimal newBalance = existingAccount.getBalance().add(request.getAmount());
        existingAccount.setBalance(newBalance);
        Account savedAccount = accountRepository.save(existingAccount);
        transactionService.createTransaction(savedAccount, TransactionType.DEPOSIT,request.getAmount(),savedAccount.getBalance());
        return accountMapper.toResponse(savedAccount);
    }

    @Transactional
    public AccountResponse withdraw(Long id, TransactionRequest request){
        Optional<Account> account = accountRepository.findById(id);
        if(account.isEmpty()){
            throw new AccountNotFoundException("Account not found with " + id);
        }
        Account existingAccount = account.get();
        BigDecimal currentBalance = existingAccount.getBalance();
        if(request.getAmount().compareTo(existingAccount.getBalance()) > 0){
            throw new InsufficientBalanceException("balance not enough u only have " + currentBalance);
        }
        BigDecimal newBalance = currentBalance.subtract(request.getAmount());
        existingAccount.setBalance(newBalance);
        Account savedAccount = accountRepository.save(existingAccount);
        transactionService.createTransaction(savedAccount,TransactionType.WITHDRAW,request.getAmount(),savedAccount.getBalance());
        return accountMapper.toResponse(savedAccount);
    }
}
