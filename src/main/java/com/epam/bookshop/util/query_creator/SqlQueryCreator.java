package com.epam.bookshop.util.query_creator;

import com.epam.bookshop.domain.Entity;
import com.epam.bookshop.util.criteria.Criteria;

/**
 * Interface for creating sql queries by incoming {@link Criteria}
 *
 * @param <T>
 */
public interface SqlQueryCreator<T extends Entity> {
    String createQuery(Criteria<T> criteria, String operator);
}
