package com.movie.portal.user_service.auth.Controller;

import com.movie.portal.user_service.auth.Dto.UserResponse;
import com.movie.portal.user_service.auth.Services.UserService;
import com.movie.portal.user_service.auth.Dto.UserDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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

    /**
     * Update user information.
     *
     * @param id the user id
     * @param request the user information
     * @param authentication the user authentication
     * @return success message
     */
    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> updateUser(
            @PathVariable Long id,
            @RequestBody UserDto request,
            Authentication authentication) {

        boolean isAdmin = authentication.getAuthorities()
                .stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        userService.updateUser(id, request, isAdmin);
        return ResponseEntity.ok("User updated successfully");
    }
}
