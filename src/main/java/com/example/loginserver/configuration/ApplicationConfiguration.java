package com.example.loginserver.configuration;

import com.example.loginserver.adapters.spi.CredentialsDbStorage;
import com.example.loginserver.adapters.spi.UserDbStorage;
import com.example.loginserver.core.UserManager;
import com.example.loginserver.core.ports.store.CredentialsStorage;
import com.example.loginserver.core.ports.store.UserStorage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@Configuration
public class ApplicationConfiguration {

    @Bean
    public UserStorage userStorage(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        return new UserDbStorage(namedParameterJdbcTemplate);
    }

    @Bean
    public CredentialsStorage credentialsStorage(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        return new CredentialsDbStorage(namedParameterJdbcTemplate);
    }

    @Bean
    public UserManager userManager(UserStorage userStorage, CredentialsStorage credentialsStorage) {
        return new UserManager(userStorage, credentialsStorage);
    }
}
