package com.movie.portal.user_service.auth.exception;

/**
 * Exception thrown when attempting to register with an email
 * that already exists in the system.
 */
public class DuplicateEmailException extends RuntimeException {
    public DuplicateEmailException(String email) {
        super("Email %s is already registered.".formatted(email));
    }
}
