package com.epam.bookshop.strategy.query_creator;

import com.epam.bookshop.criteria.Criteria;
import com.epam.bookshop.domain.Entity;

public interface EntityQueryCreator<T extends Entity> {
    String createQuery(Criteria<T> criteria);
}
