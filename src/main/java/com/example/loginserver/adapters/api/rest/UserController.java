package com.example.loginserver.adapters.api.rest;

import com.example.loginserver.core.UserManager;
import com.example.loginserver.core.domain.UserData;
import com.example.loginserver.core.exceptions.InvalidCredentialsException;
import com.example.loginserver.core.exceptions.UserNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("/user")
public class UserController {

    private static final String USER = "/user";
    private final UserManager userManager;

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody UserData userData) {
        HttpStatus status = userManager.createUser(userData);
        if (status == HttpStatus.OK) {
            return ResponseEntity.ok("User created successfully");
        }
        else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Email already in use");
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUser(@RequestBody UserData userData) {
        HttpStatus status = userManager.deleteUser(userData);
        if (status == HttpStatus.OK) {
            return ResponseEntity.ok("User deleted successfully");
        }
        else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found");
        }
    }

    @PatchMapping("/update")
    public ResponseEntity<String> updateUser(@RequestBody UserData user) {
        try {
            HttpStatus status = userManager.updateUser(user);
            return ResponseEntity.status(status).body("User updated successfully");
        } catch (
                UserNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        } catch (
                InvalidCredentialsException ex) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, ex.getMessage());
        }
    }

}
