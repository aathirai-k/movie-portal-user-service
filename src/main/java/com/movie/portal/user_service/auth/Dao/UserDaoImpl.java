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

/**
 * Implementation of UserDao interface using JdbcTemplate for database access.
 * Handles saving users and retrieving users by email.
 */
@Repository
public class UserDaoImpl implements UserDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Saves the given User object to the database.
     * Uses a generated key holder to fetch and set the generated ID back to the User object.
     *
     * @param user the User object to save
     * @return the saved User with the generated ID set
     */
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

    /**
     * Finds a User in the database by their email address.
     *
     * @param email the email to search for
     * @return an Optional containing the User if found, or empty if not found
     */
    @Override
    public Optional<User> findUserByEmail(String email) {
        String sql = "SELECT id, email, password, username, role FROM users WHERE email = ?";
        return jdbcTemplate
                .query(sql, new BeanPropertyRowMapper<>(User.class), email)
                .stream()
                .findFirst();
    }
}
