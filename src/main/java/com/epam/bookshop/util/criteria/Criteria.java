package com.epam.bookshop.util.criteria;

import com.epam.bookshop.domain.Entity;

/**
 * Utility class that is used in different situations :)
 *
 * @param <T>
 */
public abstract class Criteria<T extends Entity> {
    protected Long entityId;

    public Long getEntityId() {
        return entityId;
    }

    public Criteria (Builder<? extends Entity> builder) {
        this.entityId = builder.entityId;
    }

    protected static abstract class Builder<T extends Entity> {
        private Long entityId;

        public Builder<T> id(Long entityId) {
            this.entityId = entityId;
            return this;
        }

        public abstract Criteria build();
    }
}
