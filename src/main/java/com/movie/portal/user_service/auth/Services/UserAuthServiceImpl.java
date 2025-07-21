package com.movie.portal.user_service.auth.Services;

import com.movie.portal.user_service.auth.Dao.UserDaoRepository;
import com.movie.portal.user_service.auth.Dto.LoginRequestDto;
import com.movie.portal.user_service.auth.Dto.LoginResponse;
import com.movie.portal.user_service.auth.Dto.UserResponse;
import com.movie.portal.user_service.auth.Model.User;
import com.movie.portal.user_service.auth.exception.InvalidLoginException;
import com.movie.portal.user_service.auth.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Implementation of the UserAuthService interface.
 * Handles user login and business logic.
 */
@Service
public class UserAuthServiceImpl implements UserAuthService {
    @Autowired
    private UserDaoRepository userDaoRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Override
    public LoginResponse loginUser(LoginRequestDto request) {
        validateUserInput(request);
        User exitingUser = userDaoRepository.findUserByEmail(request.getEmail()).orElseThrow(() -> new UserNotFoundException(request.getEmail()));

        validateUserPassword(request, exitingUser);
        String token = jwtService.generateToken(exitingUser);

        return new LoginResponse(token, toUserResponse(exitingUser));
    }

    private UserResponse toUserResponse(User exitingUser) {
        return new UserResponse.Builder().id(exitingUser.getId()).userRole(exitingUser.getRole()).userName(exitingUser.getUsername()).email(exitingUser.getEmail()).build();
    }

    private void validateUserInput(LoginRequestDto request) {
        if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }

        if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("Password is required");
        }
    }

    private void validateUserPassword(LoginRequestDto request, User exitingUser) {
        if (!passwordEncoder.matches(request.getPassword(), exitingUser.getPassword())) {
            throw new InvalidLoginException();
        }
    }
}
