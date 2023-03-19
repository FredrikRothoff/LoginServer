package com.example.loginserver.core;

import com.example.loginserver.core.domain.UserData;
import com.example.loginserver.core.exceptions.InvalidCredentialsException;
import com.example.loginserver.core.exceptions.UserAlreadyExistException;
import com.example.loginserver.core.exceptions.UserNotFoundException;
import com.example.loginserver.core.ports.store.CredentialsStorage;
import com.example.loginserver.core.ports.store.UserStorage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static com.example.loginserver.core.domain.CredentialsStatus.USER_FOUND;
import static com.example.loginserver.core.domain.UserStatus.*;

@AllArgsConstructor
@Slf4j
public class UserManager {

    private final UserStorage userStorage;
    private final CredentialsStorage credentialsStorage;

    public HttpStatus createUser(UserData userData) {
        Optional<UserData> user = userStorage.findByEmail(userData.getEmail());
        if (user.isPresent()) {
            throw new UserAlreadyExistException("Somebody with that email already exists: " + userData.getEmail());
        }
        userStorage.createUser(userData.encryptPassword(), CREATED_SUCCESSFULLY);
        return HttpStatus.OK;
    }

    public HttpStatus deleteUser(UserData userData) {
        Optional<UserData> deletedUser = userStorage.findByEmail(userData.getEmail());
        if (deletedUser.isEmpty()) {
            throw new UserNotFoundException("Could not find user by " + userData.getEmail());
        }
        userStorage.deleteUser(userData, DELETED_SUCCESSFULLY);
        return HttpStatus.OK;
    }

    public HttpStatus updateUser(UserData userData) {
        Optional<UserData> user = userStorage.findByEmail(userData.getEmail());
        if (user.isPresent()) {
            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            if (passwordEncoder.matches(userData.getPassword(), user.get().getPassword())) {
                userStorage.updateUser(userData.encryptPassword(), UPDATE_SUCCESSFULLY);
                return HttpStatus.OK;
            }
            throw new InvalidCredentialsException("Invalid password for user: " + userData.getEmail());
        }
        throw new UserNotFoundException("Could not find user by " + userData.getEmail());
    }


    public HttpStatus findByEmail(String email) {
        Optional<UserData> user = credentialsStorage.findByEmail(email, USER_FOUND);
        if (user.isPresent()) {
            return HttpStatus.OK;
        }
        throw new UserNotFoundException("Could not find user by " + email);
    }

    public HttpStatus findByEmailAndPassword(String email, String password) {
        Optional<UserData> user = credentialsStorage.findByEmail(email, USER_FOUND);
        if (user.isPresent()) {
            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            if (passwordEncoder.matches(password, user.get().getPassword())) {
                return HttpStatus.OK;
            }
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password");
    }
}
