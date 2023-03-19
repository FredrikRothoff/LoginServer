package com.example.loginserver.core.ports.store;

import com.example.loginserver.core.domain.UserData;
import com.example.loginserver.core.domain.UserStatus;

import java.util.Optional;

public interface UserStorage {

    void createUser(UserData userData, UserStatus userStatus);

    void deleteUser(UserData userData, UserStatus userStatus);

    void updateUser(UserData userData, UserStatus userStatus);

    Optional<UserData> findByEmail(String email);
}
