package com.learning.Bank.mapper;

import com.learning.Bank.dto.AccountRequest;
import com.learning.Bank.dto.AccountResponse;
import com.learning.Bank.entity.Account;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    Account toEntity(AccountRequest request);

    AccountResponse toResponse(Account account);
}
