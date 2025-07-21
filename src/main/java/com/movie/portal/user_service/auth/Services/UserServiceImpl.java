package com.movie.portal.user_service.auth.Services;

import com.movie.portal.user_service.auth.Dao.UserDaoRepository;
import com.movie.portal.user_service.auth.Dto.UserDto;
import com.movie.portal.user_service.auth.Dto.UserResponse;
import com.movie.portal.user_service.auth.Model.User;
import com.movie.portal.user_service.auth.exception.DatabaseException;
import com.movie.portal.user_service.auth.exception.DuplicateEmailException;
import com.movie.portal.user_service.uitility.AppConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * Implementation of the UserService interface.
 * Handles user registration and business logic.
 */
@Service
public class UserServiceImpl implements UserService {
    private final UserDaoRepository userDaoRepository;
    private final BCryptPasswordEncoder encoder;

    @Autowired
    public UserServiceImpl(UserDaoRepository userDaoRepository, BCryptPasswordEncoder encoder) {
        this.userDaoRepository = userDaoRepository;
        this.encoder = encoder;
    }

    @Override
    public UserResponse registerUser(UserDto userDto) {
        if (userDaoRepository.findUserByEmail(userDto.getEmail()).isPresent()) {
            throw new DuplicateEmailException(userDto.getEmail());
        }
        User user = new User();
        user.setEmail(userDto.getEmail());
        user.setPassword(encoder.encode(userDto.getPassword()));
        user.setUsername(userDto.getUserName());
        user.setRole(AppConstants.ROLE_USER);

        try {
            User saved = userDaoRepository.saveUser(user);
            return new UserResponse(saved.getId(), saved.getUsername(), saved.getEmail(), saved.getRole());

        } catch (DataAccessException dbEx) {
            throw new DatabaseException("Could not register user at the moment. Please try again.", dbEx);
        }
    }

    @Override
    public void updateUser(Long id, UserDto request, boolean isAdmin) {
        User user = userDaoRepository.findUserById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (isAdmin) {
            if (StringUtils.hasText(request.getUserRole())) {
                user.setRole(request.getUserRole());
            }
            if (StringUtils.hasText(request.getUserName())) {
                user.setUsername(request.getUserName());
            }
            if (StringUtils.hasText(request.getEmail())) {
                user.setEmail(request.getEmail());
            }
            if (StringUtils.hasText(request.getPassword())) {
                user.setPassword(encoder.encode(request.getPassword()));
            }
        }

        userDaoRepository.updateUser(user);
    }

}
