package com.learning.Bank.dto;

import jakarta.validation.constraints.DecimalMin;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class TransactionRequest {
    @DecimalMin(value = "0.01")
    private BigDecimal amount;
}
