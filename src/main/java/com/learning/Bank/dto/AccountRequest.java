package com.learning.Bank.dto;

import com.learning.Bank.entity.AccountType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AccountRequest {

    @NotBlank
    private String accountHolderName;

    @NotNull
    private AccountType accountType;

}
