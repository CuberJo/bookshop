package com.epam.bookshop.domain;

import java.util.Objects;

public abstract class Entity {
    protected Long entityId;

    public Entity() {
    }

    public Entity(Long entityId) {
        this.entityId = entityId;
    }

    public long getEntityId() {
        return entityId;
    }
    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Entity entity = (Entity) o;
        return entityId.equals(entity.entityId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(entityId);
    }
}
