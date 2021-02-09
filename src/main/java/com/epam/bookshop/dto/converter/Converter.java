package com.epam.bookshop.dto.converter;

import com.epam.bookshop.domain.Entity;

/**
 * Interface responsible for converting {@link Entity} to DTO
 *
 * @param <T> DTO type
 * @param <E> entity to convert type
 */
@FunctionalInterface
public interface Converter<T extends Entity, E extends Entity> {
    T convert(E entity);
}
