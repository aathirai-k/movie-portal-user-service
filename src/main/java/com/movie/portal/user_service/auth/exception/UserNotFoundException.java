package com.movie.portal.user_service.auth.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String email) {
        super("User with email " + email + " does not exist. Please register.");
    }
}
