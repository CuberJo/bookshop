package com.epam.bookshop.strategy.query_creator;

import com.epam.bookshop.criteria.Criteria;
import com.epam.bookshop.domain.Entity;

public interface FindEntityQueryCreator {
    String createQuery(Criteria<? extends Entity> criteria);
}
