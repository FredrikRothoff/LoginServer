package com.example.loginserver.adapters.api.rest.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class UpdatePasswordDto {

    String email;
    String oldPassword;
    String newPassword;
}
