package com.movie.portal.user_service.auth.Services;

import com.movie.portal.user_service.auth.Dto.UserDto;
import com.movie.portal.user_service.auth.Dto.UserResponse;

public interface UserService {
    public UserResponse registerUser(UserDto request);
}
