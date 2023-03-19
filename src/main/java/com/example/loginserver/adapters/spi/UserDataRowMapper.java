package com.example.loginserver.adapters.spi;

import com.example.loginserver.core.domain.UserData;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDataRowMapper implements RowMapper<UserData> {
    @Override
    public UserData mapRow(ResultSet rs, int rowNum) throws SQLException {
        return UserData.builder()
                .first_name(rs.getString("first_name"))
                .last_name(rs.getString("last_name"))
                .email(rs.getString("email"))
                .phone(rs.getString("phone"))
                .password(rs.getString("password"))
                .address(rs.getString("address"))
                .city(rs.getString("city"))
                .zip(rs.getString("zip"))
                .country(rs.getString("country"))
                .build();
    }

}
