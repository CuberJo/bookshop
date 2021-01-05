package com.epam.bookshop.exception;

public class UnknownEntityException extends RuntimeException {
    private final String entityName;
    private final Object[] args;

    public UnknownEntityException(String entityName) {
        super();
        this.entityName = entityName;
        this.args = null;
    }

    public UnknownEntityException(String entityName, Object[] args) {
        super();
        this.entityName = entityName;
        this.args = args;
    }

    @Override
    public String getMessage() {
        StringBuilder errorMessage = new StringBuilder("");
        if (entityName != "" && entityName.isEmpty()) {
            errorMessage.append(entityName + " ");
        }
        if (args != null) {
            errorMessage.append(args.toString());
        }
        // you should use entityName, args (if necessary)
        return entityName;
    }
}
