package com.movie.portal.user_service.auth.Dao;

import com.movie.portal.user_service.auth.Model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Optional;

@Repository
public class UserDaoImpl implements UserDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public User saveUser(User user) {
        final String sql =
                "INSERT INTO users (username, email, role, password) VALUES (?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    sql,
                    Statement.RETURN_GENERATED_KEYS
            );
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getRole());
            ps.setString(4, user.getPassword());
            return ps;
        }, keyHolder);

        Number key = keyHolder.getKey();          // assumes PK is numeric
        if (key != null) {
            user.setId(key.longValue());
        }

        return user;
    }

    @Override
    public Optional<User> findUserByEmail(String email) {
        String sql = "SELECT id, email, password, username, role FROM users WHERE email = ?";
        return jdbcTemplate
                .query(sql, new BeanPropertyRowMapper<>(User.class), email)
                .stream()
                .findFirst();
    }
}
