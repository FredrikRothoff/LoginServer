package com.example.loginserver.adapters.spi;

import com.example.loginserver.core.domain.TemporaryUserData;
import com.example.loginserver.core.domain.UserData;
import com.example.loginserver.core.domain.UserStatus;
import com.example.loginserver.core.ports.store.UserStorage;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
public class JdbcUserStorage implements UserStorage {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public void createUser(UserData userData, UserStatus userStatus) {
        String sql = "INSERT INTO users (email, password, first_name, last_name, phone, address, "
                + "city, zip, country, roles) VALUES (:email, :password, :first_name, :last_name,"
                + " " + ":phone, :address, :city, :zip, :country, :roles)";
        MapSqlParameterSource parameterSource = new MapSqlParameterSource().addValue("email",
                userData.getEmail()).addValue("password", userData.getPassword()).addValue(
                        "first_name", userData.getFirst_name()).addValue("last_name",
                userData.getLast_name()).addValue("phone", userData.getPhone()).addValue("address"
                , userData.getAddress()).addValue("city", userData.getCity()).addValue("zip",
                userData.getZip()).addValue("country", userData.getCountry()).addValue("roles",
                "USER"); // Set role to 'user' by default
        namedParameterJdbcTemplate.update(sql, parameterSource);
    }

    @Override
    public void deleteUser(UserData userData, UserStatus userStatus) {
        String sql = "DELETE FROM users WHERE email = :email";
        MapSqlParameterSource parameterSource = new MapSqlParameterSource().addValue("email",
                userData.getEmail());
        namedParameterJdbcTemplate.update(sql, parameterSource);
    }

    @Override
    public void updateUser(UserData userData, UserStatus userStatus) {
        String sql = "UPDATE users SET password = :password, first_name = :first_name, last_name "
                + "= :last_name, phone = :phone, address = :address, city = :city, zip = :zip, " + "country = :country WHERE email = :email ";
        MapSqlParameterSource parameterSource = new MapSqlParameterSource().addValue("email",
                userData.getEmail()).addValue("password", userData.getPassword()).addValue(
                        "first_name", userData.getFirst_name()).addValue("last_name",
                userData.getLast_name()).addValue("phone", userData.getPhone()).addValue("address"
                , userData.getAddress()).addValue("city", userData.getCity()).addValue("zip",
                userData.getZip()).addValue("country", userData.getCountry());
        namedParameterJdbcTemplate.update(sql, parameterSource);
    }

    @Override
    public Optional<UserData> findByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = :email";
        MapSqlParameterSource parameterSource = new MapSqlParameterSource().addValue("email",
                email);
        List<UserData> userData = namedParameterJdbcTemplate.query(sql, parameterSource,
                new UserDataRowMapper());
        if (userData.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(userData.get(0));
        }
    }

    @Override
    public void createTemporaryUser(TemporaryUserData temporaryUserData, UserStatus userStatus) {
        String sql =
                "INSERT INTO temporaryuser (email, password, first_name, last_name, phone, " +
                        "address, city, zip, country, token, roles) VALUES (:email, :password, " + ":first_name, " + ":last_name, :phone, :address, :city, :zip, :country, " + ":token, :roles)";
        MapSqlParameterSource parameterSource = new MapSqlParameterSource().addValue("email",
                temporaryUserData.getEmail()).addValue("password",
                temporaryUserData.getPassword()).addValue("first_name",
                temporaryUserData.getFirst_name()).addValue("last_name",
                temporaryUserData.getLast_name()).addValue("phone", temporaryUserData.getPhone()).addValue("address", temporaryUserData.getAddress()).addValue("city", temporaryUserData.getCity()).addValue("zip", temporaryUserData.getZip()).addValue("country", temporaryUserData.getCountry()).addValue("token", UUID.randomUUID()).addValue("roles", "USER"); // Set role to 'user' by default
        namedParameterJdbcTemplate.update(sql, parameterSource);
    }

    @Override
    public UserData findByToken(String token) {
        String sql = "SELECT * FROM temporaryuser WHERE token = :token";
        MapSqlParameterSource parameterSource = new MapSqlParameterSource().addValue("token",
                token);
        List<UserData> userData = namedParameterJdbcTemplate.query(sql, parameterSource,
                new UserDataRowMapper());
        return userData.get(0);
    }

    @Override
    public String findUUIDByEmail(String email) {
        String sql = "SELECT token FROM temporaryuser WHERE email = :email";
        MapSqlParameterSource parameterSource = new MapSqlParameterSource().addValue("email",
                email);
        List<String> tokens = namedParameterJdbcTemplate.queryForList(sql, parameterSource,
                String.class);
        return tokens.get(0);
    }

    @Override
    public void deleteTemporaryUser(String email, UserStatus userStatus) {
        String sql = "DELETE FROM temporaryuser WHERE email = :email";
        MapSqlParameterSource parameterSource = new MapSqlParameterSource().addValue("email",
                email);
        namedParameterJdbcTemplate.update(sql, parameterSource);
    }

    @Override
    public void updateToTemporaryPassword(String password, String email) {
        String sql = "UPDATE users SET password = :password WHERE email = :email";
        MapSqlParameterSource parameterSource = new MapSqlParameterSource().addValue("email",
                email).addValue("password", password);
        namedParameterJdbcTemplate.update(sql, parameterSource);
    }

    @Override
    public UserStatus updatePassword(String email, String newPassword, UserStatus userStatus) {
        String sql = "UPDATE users SET password = :newPassword WHERE email = :email";
        MapSqlParameterSource parameterSource = new MapSqlParameterSource().addValue("email",
                email).addValue("newPassword", newPassword);
        namedParameterJdbcTemplate.update(sql, parameterSource);
        return userStatus;
    }
}
