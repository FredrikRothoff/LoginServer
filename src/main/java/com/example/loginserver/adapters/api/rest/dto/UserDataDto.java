package com.example.loginserver.adapters.api.rest.dto;

import com.example.loginserver.core.domain.UserData;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class UserDataDto {

    String first_name;
    String last_name;
    String email;
    String phone;
    String password;
    String address;
    String city;
    String zip;
    String country;

    public static UserDataDto from(UserData userData) {
        return UserDataDto.builder()
                .first_name(userData.getFirst_name())
                .last_name(userData.getLast_name())
                .email(userData.getEmail())
                .phone(userData.getPhone())
                .password(userData.getPassword())
                .address(userData.getAddress())
                .city(userData.getCity())
                .zip(userData.getZip())
                .country(userData.getCountry())
                .build();
    }
}
