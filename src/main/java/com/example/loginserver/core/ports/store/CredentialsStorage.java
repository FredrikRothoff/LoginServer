package com.example.loginserver.core.ports.store;

import com.example.loginserver.core.domain.CredentialsStatus;
import com.example.loginserver.core.domain.UserData;

import java.util.Optional;

public interface CredentialsStorage {


    Optional<UserData> findByEmail(String email, CredentialsStatus credentialsStatus);
}
