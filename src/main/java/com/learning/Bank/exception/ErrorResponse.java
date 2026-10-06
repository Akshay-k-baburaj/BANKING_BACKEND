package com.learning.Bank.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class ErrorResponse {
    private String message;
    private int status;
    private Map<String, String> errors;
}
