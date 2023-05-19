package com.example.loginserver.core.domain;

import lombok.Builder;
import lombok.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

@Value
@Builder
public class TemporaryUserData {

    String first_name;
    String last_name;
    String email;
    String phone;
    String password;
    String address;
    String city;
    String zip;
    String country;
    UUID token;

    public TemporaryUserData encryptPassword() {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encryptedPassword = passwordEncoder.encode(password);
        return new TemporaryUserData(first_name, last_name, email, phone, encryptedPassword,
                address, city, zip, country, token);
    }
}
