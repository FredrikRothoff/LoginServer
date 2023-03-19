package com.example.loginserver.adapters.api.rest.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class UserLoginRequestDto {

    String email;
    String password;

}
