package com.example.loginserver.adapters.api.rest;

import com.example.loginserver.core.UserManager;
import com.example.loginserver.core.domain.UserData;
import com.example.loginserver.core.exceptions.InvalidCredentialsException;
import com.example.loginserver.core.exceptions.UserNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
    public ResponseEntity<String> deleteUser(@RequestBody UserData user) {
        try {
            HttpStatus status = userManager.deleteUser(user);
            return ResponseEntity.status(status).body("User deleted successfully");
        } catch (
                UserNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        } catch (
                InvalidCredentialsException ex) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, ex.getMessage());
        }
    }

    @PatchMapping("/update")
    public ResponseEntity<String> updateUser(@RequestBody UserData user, UsernamePasswordAuthenticationToken authentication) {
        String email = authentication.getName();
        try {
            HttpStatus status = userManager.updateUser(user, email);
            return ResponseEntity.status(status).body("User updated successfully");
        } catch (
                UserNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        } catch (
                InvalidCredentialsException ex) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, ex.getMessage());
        } catch (
                ResponseStatusException ex) {
            throw ex;
        } catch (
                Exception ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error updating user");
        }
    }
}


