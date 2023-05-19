package com.example.loginserver.core.ports.store;

import com.example.loginserver.core.domain.TemporaryUserData;
import com.example.loginserver.core.domain.UserData;
import com.example.loginserver.core.domain.UserStatus;

import java.util.Optional;

public interface UserStorage {

    void createUser(UserData userData, UserStatus userStatus);

    void deleteUser(UserData userData, UserStatus userStatus);

    void updateUser(UserData userData, UserStatus userStatus);

    void createTemporaryUser(TemporaryUserData temporaryUserData, UserStatus userStatus);

    UserData findByToken(String token);

    String findUUIDByEmail(String email);

    void deleteTemporaryUser(String email, UserStatus userStatus);

    void updateToTemporaryPassword(String password, String email);

    Optional<UserData> findByEmail(String email);

    UserStatus updatePassword(String email, String newPassword, UserStatus userStatus);
}
