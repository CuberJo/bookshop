package com.epam.bookshop.util.query_creator;

import com.epam.bookshop.util.criteria.Criteria;
import com.epam.bookshop.domain.Entity;

public interface EntityQueryCreator<T extends Entity> {
    String createQuery(Criteria<T> criteria, String operator);
}
