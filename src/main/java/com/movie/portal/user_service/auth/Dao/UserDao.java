package com.movie.portal.user_service.auth.Dao;

import com.movie.portal.user_service.auth.Model.User;

import java.util.Optional;


public interface UserDao {
    User saveUser(User user);

    Optional<User> findUserByEmail(String email);
}
