package com.learning.Bank.dto;

import com.learning.Bank.entity.UserType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RegisterResponse {
    private Long id;
    private String email;
    private UserType userType;
}
