package com.movie.portal.user_service.auth.Controller;

import com.movie.portal.user_service.auth.Dto.UserResponse;
import com.movie.portal.user_service.auth.Services.UserService;
import com.movie.portal.user_service.auth.Dto.UserDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * This controller handles user registration requests.
 */
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Registers a new user.
     *
     * @param request the registration data
     * @return the registered user details
     */
    @PostMapping("/register")
    public ResponseEntity<UserResponse> registerUser(@RequestBody UserDto request) {
        return ResponseEntity.ok(userService.registerUser(request));
    }
}
