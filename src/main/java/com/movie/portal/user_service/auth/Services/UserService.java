package com.movie.portal.user_service.auth.Services;

import com.movie.portal.user_service.auth.Dto.UserDto;
import com.movie.portal.user_service.auth.Dto.UserResponse;

/**
 * Service interface for user-related operations.
 */
public interface UserService {
    /**
     * Registers a new user with the given user data.
     *
     * @param request the data transfer object containing user registration info
     * @return a UserResponse DTO representing the registered user
     */
    public UserResponse registerUser(UserDto request);

    /**
     * Update user information.
     *
     * @param id the user id
     * @param request for user information
     * @param isAdmin for user is admin
     */
    public void updateUser(Long id, UserDto request, boolean isAdmin);
}
