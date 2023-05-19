package com.example.loginserver.adapters.api.rest;

import com.example.loginserver.adapters.api.rest.dto.UpdatePasswordDto;
import com.example.loginserver.adapters.api.rest.dto.UserLoginRequestDto;
import com.example.loginserver.configuration.security.JwtUtil;
import com.example.loginserver.core.PasswordGenerator;
import com.example.loginserver.core.UserManager;
import com.example.loginserver.core.domain.UserData;
import com.example.loginserver.core.exceptions.InvalidCredentialsException;
import com.example.loginserver.core.exceptions.UserNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;

@RestController
@AllArgsConstructor
@Slf4j
public class CredentialsController {

    private static final String USER = "/credentials";
    private final UserManager userManager;
    private final JwtUtil jwtUtil;

    @PostMapping("/forgot/{email}")
    public ResponseEntity<?> findByEmail(@PathVariable String email) {
        HttpStatus status = userManager.findByEmail(email);
        System.out.println(status);
        if (status == HttpStatus.OK) {
            String password = PasswordGenerator.generateRandomPassword();
            userManager.updateToTemporaryPassword(password, email);
            try {
                PasswordGenerator.sendPasswordByEmail(email, password);
                return ResponseEntity.ok("Reset email sent");
            } catch (Exception ex) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                        "Failed to " + "send email");
            }
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email");
    }

    @PostMapping("/login")
    public ResponseEntity<UserData> login(@RequestBody UserLoginRequestDto loginDto) {
        UserData user = userManager.findByEmailAndPassword(loginDto.getEmail(),
                loginDto.getPassword());
        if (user != null) {
            UserDetails userDetails = new User(loginDto.getEmail(), loginDto.getPassword(),
                    new ArrayList<>());
            String token = jwtUtil.generateToken(userDetails);
            System.out.println("Bearer " + token);
            return ResponseEntity.ok().header("Authorization", "Bearer " + token).body(user);
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password");
    }

    @PatchMapping("/updatePassword")
    public ResponseEntity<?> updatePassword(@RequestBody UpdatePasswordDto updatePasswordDto,
                                            UsernamePasswordAuthenticationToken authentication) {
        String email = authentication.getName();
        try {
            HttpStatus status = userManager.updatePassword(email, updatePasswordDto.getEmail(),
                    updatePasswordDto.getOldPassword(), updatePasswordDto.getNewPassword());
            return ResponseEntity.status(status).body("User updated successfully");
        } catch (UserNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        } catch (InvalidCredentialsException ex) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, ex.getMessage());
        } catch (ResponseStatusException ex) {
            throw ex;
        } catch (Exception ex) {
            System.out.println(ex);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error updating " + "user");
        }
    }
}
