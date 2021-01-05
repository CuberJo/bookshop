package com.epam.bookshop.strategy.query_creator.impl;

import com.epam.bookshop.criteria.Criteria;
import com.epam.bookshop.criteria.impl.UserCriteria;
import com.epam.bookshop.domain.impl.EntityType;
import com.epam.bookshop.domain.impl.User;
import com.epam.bookshop.strategy.query_creator.FindEntityQueryCreator;
import org.testng.annotations.Test;

public class FindUserQueryCreatorTest {

    @Test
    public void testCreateQuery() {
        Criteria<User> criteria = UserCriteria.builder()
                .id(1L)
                .name("John")
                .email("john@gmail.com")
                .build();
        FindEntityQueryCreator queryCreator = FindEntityQueryCreatorFactory.INSTANCE.create(EntityType.BOOK);
        String query = queryCreator.createQuery(criteria);
        System.out.println(query);
    }
}