package com.example.loginserver.adapters.api.rest;

import com.example.loginserver.adapters.api.rest.dto.UserLoginRequestDto;
import com.example.loginserver.configuration.security.JwtUtil;
import com.example.loginserver.core.UserManager;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
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
        if (status == HttpStatus.OK) {
            return ResponseEntity.ok("Reset email sent");
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserLoginRequestDto loginDto) {
        HttpStatus status = userManager.findByEmailAndPassword(loginDto.getEmail(), loginDto.getPassword());
        if (status == HttpStatus.OK) {
            UserDetails userDetails = new User(loginDto.getEmail(), loginDto.getPassword(), new ArrayList<>());
            String token = jwtUtil.generateToken(userDetails);
            System.out.println("Bearer " + token);
            return ResponseEntity.ok()
                    .header("Authorization", "Bearer " + token)
                    .build();
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password");
    }
}
