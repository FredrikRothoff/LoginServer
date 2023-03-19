package com.example.loginserver.adapters.spi;

import com.example.loginserver.core.domain.CredentialsStatus;
import com.example.loginserver.core.domain.UserData;
import com.example.loginserver.core.ports.store.CredentialsStorage;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class CredentialsDbStorage implements CredentialsStorage {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public Optional<UserData> findByEmail(String email, CredentialsStatus credentialsStatus) {
        String sql = "SELECT * FROM users WHERE email = :email";
        MapSqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("email", email);
        List<UserData> userData = namedParameterJdbcTemplate.query(sql, parameterSource, new UserDataRowMapper());
        if (userData.isEmpty()) {
            return Optional.empty();
        }
        else {
            return Optional.of(userData.get(0));
        }
    }
}
