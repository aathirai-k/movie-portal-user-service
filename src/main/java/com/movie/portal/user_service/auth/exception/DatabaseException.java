package com.movie.portal.user_service.auth.exception;

/**
 * Exception thrown when a database-related error occurs.
 * This is a runtime exception used to wrap lower-level database exceptions.
 */
public class DatabaseException extends RuntimeException {

    public DatabaseException(String message) {
        super(message);
    }

    public DatabaseException(String message, Throwable cause) {
        super(message, cause);
    }
}
