package com.movie.portal.user_service.auth.Services;

import com.movie.portal.user_service.auth.Dao.UserDao;
import com.movie.portal.user_service.auth.Dto.UserDto;
import com.movie.portal.user_service.auth.Dto.UserResponse;
import com.movie.portal.user_service.auth.Model.User;
import com.movie.portal.user_service.auth.exception.DatabaseException;
import com.movie.portal.user_service.auth.exception.DuplicateEmailException;
import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Implementation of the UserService interface.
 * Handles user registration and business logic.
 */
@Service
public class UserServiceImpl implements UserService {
    private final UserDao userDao;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public UserResponse registerUser(UserDto userDto) {
        if (userDao.findUserByEmail(userDto.getEmail()).isPresent()) {
            throw new DuplicateEmailException(userDto.getEmail());
        }
        User user = new User();
        user.setEmail(userDto.getEmail());
        user.setPassword(encoder.encode(userDto.getPassword()));
        user.setUsername(userDto.getUserName());
        user.setRole(userDto.getUserRole());

        try {
            User saved = userDao.saveUser(user);
            return new UserResponse(saved.getId(), saved.getUsername(), saved.getEmail(), saved.getRole());

        } catch (DataAccessException dbEx) {
            throw new DatabaseException("Could not register user at the moment. Please try again.", dbEx);
        }
    }
}
