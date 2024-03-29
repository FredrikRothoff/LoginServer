package com.example.loginserver.core;

import com.example.loginserver.core.domain.TemporaryUserData;
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

    public HttpStatus createUser(String token) {
        UserData user = userStorage.findByToken(token);
        Optional<UserData> existingUser = userStorage.findByEmail(user.getEmail());
        if (existingUser.isPresent()) {
            throw new UserAlreadyExistException("Somebody with that email already exists: " + user.getEmail());
        }
        userStorage.createUser(user.encryptPassword(), CREATED_SUCCESSFULLY);
        userStorage.deleteTemporaryUser(user.getEmail(), DELETED_SUCCESSFULLY);
        return HttpStatus.OK;
    }

    public HttpStatus deleteUser(UserData userData) {
        Optional<UserData> user = userStorage.findByEmail(userData.getEmail());
        if (user.isPresent()) {
            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            if (passwordEncoder.matches(userData.getPassword(), user.get().getPassword())) {
                userStorage.deleteUser(userData.encryptPassword(), UPDATE_SUCCESSFULLY);
                return HttpStatus.OK;
            }
            throw new InvalidCredentialsException("Invalid password for user: " + userData.getEmail());
        }
        throw new UserNotFoundException("Could not find user by " + userData.getEmail());
    }

    public HttpStatus updateUser(UserData userData, String email) {
        if (!email.equals(userData.getEmail())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    "Email in token does not " + "match email in request body");
        }
        Optional<UserData> user = userStorage.findByEmail(email);
        if (user.isPresent()) {
            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            if (passwordEncoder.matches(userData.getPassword(), user.get().getPassword())) {
                userStorage.updateUser(userData.encryptPassword(), UPDATE_SUCCESSFULLY);
                return HttpStatus.OK;
            }
            throw new InvalidCredentialsException("Invalid password for user: " + email);
        }
        throw new UserNotFoundException("Could not find user by " + email);
    }

    public HttpStatus findByEmail(String email) {
        Optional<UserData> user = credentialsStorage.findByEmail(email, USER_FOUND);
        if (user.isPresent()) {
            return HttpStatus.OK;
        }
        throw new UserNotFoundException("Could not find user by " + email);
    }

    public UserData findByEmailAndPassword(String email, String password) {
        Optional<UserData> user = credentialsStorage.findByEmail(email, USER_FOUND);
        if (user.isPresent()) {
            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            if (passwordEncoder.matches(password, user.get().getPassword())) {
                return user.get();
            }
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password");
    }

    public HttpStatus createTemporaryUser(TemporaryUserData temporaryUserData) {
        Optional<UserData> user = userStorage.findByEmail(temporaryUserData.getEmail());
        if (user.isPresent()) {
            throw new UserAlreadyExistException("Somebody with that email already exists: " + temporaryUserData.getEmail());
        }
        userStorage.createTemporaryUser(temporaryUserData, CREATED_SUCCESSFULLY);
        return HttpStatus.OK;
    }

    public HttpStatus updateToTemporaryPassword(String password, String email) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encryptedPassword = passwordEncoder.encode(password);
        userStorage.updateToTemporaryPassword(encryptedPassword, email);
        return HttpStatus.ACCEPTED;
    }

    public HttpStatus updatePassword(String tokenEmail, String email, String oldPassword,
                                     String newPassword) {
        System.out.println(newPassword);
        if (!tokenEmail.equals(email)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    "Email in token does not " + "match email in request body");
        }
        Optional<UserData> user = userStorage.findByEmail(email);
        if (user.isPresent()) {
            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            if (passwordEncoder.matches(oldPassword, user.get().getPassword())) {
                String encryptedPassword = passwordEncoder.encode(newPassword);
                userStorage.updatePassword(email, encryptedPassword, UPDATE_SUCCESSFULLY);
                return HttpStatus.OK;
            }
            throw new InvalidCredentialsException("Invalid password for user: " + email);
        }
        throw new UserNotFoundException("Could not find user by " + email);
    }
}
