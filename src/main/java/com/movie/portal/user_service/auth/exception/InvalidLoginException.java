package com.movie.portal.user_service.auth.exception;

public class InvalidLoginException extends RuntimeException {

    public InvalidLoginException() {
        super("Invalid credentials");
    }
}
