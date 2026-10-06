package com.learning.Bank.mapper;

import com.learning.Bank.dto.TransactionResponse;
import com.learning.Bank.entity.Transaction;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TransactionMapper {
    TransactionResponse toResponse(Transaction transaction);
    List<TransactionResponse> toResponse (List<Transaction> transactions);
}
