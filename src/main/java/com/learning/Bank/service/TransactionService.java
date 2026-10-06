package com.learning.Bank.service;

import com.learning.Bank.dto.TransactionResponse;
import com.learning.Bank.entity.Account;
import com.learning.Bank.entity.Transaction;
import com.learning.Bank.entity.TransactionType;
import com.learning.Bank.exception.AccountNotFoundException;
import com.learning.Bank.mapper.TransactionMapper;
import com.learning.Bank.repository.AccountRepository;
import com.learning.Bank.repository.TransactionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;
    private final AccountRepository accountRepository;

    public TransactionService(TransactionRepository transactionRepository, TransactionMapper transactionMapper,AccountRepository accountRepository){
        this.transactionRepository = transactionRepository;
        this.transactionMapper = transactionMapper;
        this.accountRepository = accountRepository;
    }

    public Transaction createTransaction(Account account, TransactionType transactionType, BigDecimal amount, BigDecimal balanceAfter){
        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setTransactionType(transactionType);
        transaction.setAmount(amount);
        transaction.setBalanceAfter(balanceAfter);
        transaction.setCreatedAt(LocalDateTime.now());
        return transactionRepository.save(transaction);
    }

    public Page<TransactionResponse> getTransactionByAccountId(Long accountId, Pageable pageable){
        Optional<Account> account = accountRepository.findById(accountId);
        if(account.isEmpty()){
            throw new AccountNotFoundException("Account Not Found with "+ accountId);
        }
        Page<Transaction> transactions = transactionRepository.findByAccountId(accountId, pageable);
        return transactions.map(transactionMapper::toResponse);
    }


}
