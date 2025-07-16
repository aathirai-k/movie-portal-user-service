package com.movie.portal.user_service.auth.Services;

import com.movie.portal.user_service.auth.Dto.LoginRequestDto;
import com.movie.portal.user_service.auth.Dto.LoginResponse;

/**
 * Service interface for user-login-related operations.
 */
public interface UserAuthService {
    /**
     * Authenticate user login.
     *
     * @param request the data transfer object containing user login info
     * @return a LoginResponse DTO representing the user and token
     */
    public LoginResponse loginUser(LoginRequestDto request);
}
