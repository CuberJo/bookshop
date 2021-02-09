package com.epam.bookshop.exception;

/**
 * Thrown when data query language operation fails
 */
public class DqlException extends Exception {

    public DqlException() {
        super();
    }

    public DqlException(String message) {
        super(message);
    }

    public DqlException(String message, Throwable cause) {
        super(message, cause);
    }

    public DqlException(Throwable cause) {
        super(cause);
    }
}
