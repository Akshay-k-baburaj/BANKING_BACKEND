package com.learning.Bank.dto;

import com.learning.Bank.entity.AccountType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class AccountResponse {
    private Long id;
    private Long accountNumber;
    private String accountHolderName;
    private BigDecimal balance;
    AccountType accountType;
    LocalDateTime createdAt;
}
