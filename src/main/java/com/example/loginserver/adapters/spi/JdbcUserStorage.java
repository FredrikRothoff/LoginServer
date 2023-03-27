package com.example.loginserver.adapters.spi;

import com.example.loginserver.core.domain.UserData;
import com.example.loginserver.core.domain.UserStatus;
import com.example.loginserver.core.ports.store.UserStorage;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class JdbcUserStorage implements UserStorage {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public void createUser(UserData userData, UserStatus userStatus) {
        String sql = "INSERT INTO users (email, password, first_name, last_name, phone, address, city, zip, country, roles) VALUES (:email, :password, :first_name, :last_name, :phone, :address, :city, :zip, :country, :roles)";
        MapSqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("email", userData.getEmail())
                .addValue("password", userData.getPassword())
                .addValue("first_name", userData.getFirst_name())
                .addValue("last_name", userData.getLast_name())
                .addValue("phone", userData.getPhone())
                .addValue("address", userData.getAddress())
                .addValue("city", userData.getCity())
                .addValue("zip", userData.getZip())
                .addValue("country", userData.getCountry())
                .addValue("roles", "USER"); // Set role to 'user' by default
        namedParameterJdbcTemplate.update(sql, parameterSource);
    }

    @Override
    public void deleteUser(UserData userData, UserStatus userStatus) {
        String sql = "DELETE FROM users WHERE email = :email";
        MapSqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("email", userData.getEmail());
        namedParameterJdbcTemplate.update(sql, parameterSource);
    }

    @Override
    public void updateUser(UserData userData, UserStatus userStatus) {
        String sql = "UPDATE users SET password = :password, first_name = :first_name, last_name = :last_name, phone = :phone, address = :address, city = :city, zip = :zip, country = :country WHERE email = :email ";
        MapSqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("email", userData.getEmail())
                .addValue("password", userData.getPassword())
                .addValue("first_name", userData.getFirst_name())
                .addValue("last_name", userData.getLast_name())
                .addValue("phone", userData.getPhone())
                .addValue("address", userData.getAddress())
                .addValue("city", userData.getCity())
                .addValue("zip", userData.getZip())
                .addValue("country", userData.getCountry());
        namedParameterJdbcTemplate.update(sql, parameterSource);
    }

    @Override
    public Optional<UserData> findByEmail(String email) {
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
