package com.epam.bookshop.exception;

/**
 * The exception will be thrown when entity {@link com.epam.bookshop.domain.Entity} could not be found in database
 */
public class EntityNotFoundException extends Exception {

    public EntityNotFoundException() {
        super();
    }

    public EntityNotFoundException(String message) {
        super(message);
    }

    public EntityNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public EntityNotFoundException(Throwable cause) {
        super(cause);
    }
}

