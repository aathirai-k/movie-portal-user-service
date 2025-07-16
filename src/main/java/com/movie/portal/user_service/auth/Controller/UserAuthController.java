package com.movie.portal.user_service.auth.Controller;

import com.movie.portal.user_service.auth.Dto.LoginRequestDto;
import com.movie.portal.user_service.auth.Dto.LoginResponse;
import com.movie.portal.user_service.auth.Services.UserAuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * This controller handles user login requests.
 */
@RestController
@RequestMapping("/api/v1/auth")
public class UserAuthController {
    private final UserAuthService userAuthService;

    public UserAuthController(UserAuthService userAuthService) {
        this.userAuthService = userAuthService;
    }

    /**
     * Login user.
     *
     * @param request the Login data
     * @return the Logged in user details
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequestDto request) {
        return ResponseEntity.ok(userAuthService.loginUser(request));
    }
}
