package com.movie.portal.user_service.auth.exception;

public class DuplicateEmailException extends RuntimeException {
    public DuplicateEmailException(String email) {
        super("Email %s is already registered.".formatted(email));
    }
}
