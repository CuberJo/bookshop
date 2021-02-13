package com.epam.bookshop.exception;

/**
 * The exception will be thrown when passed entity is not matches to existing ones
 */
public class UnknownEntityException extends RuntimeException {

    public UnknownEntityException() {
        super();
    }

    public UnknownEntityException(String message) {
        super(message);
    }

    public UnknownEntityException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnknownEntityException(Throwable cause) {
        super(cause);
    }
}
