package com.epam.bookshop.strategy.query_creator.impl;

import constant.UtilStringConstants;
import com.epam.bookshop.util.criteria.Criteria;
import com.epam.bookshop.util.criteria.impl.UserCriteria;
import com.epam.bookshop.domain.impl.EntityType;
import com.epam.bookshop.domain.impl.User;
import com.epam.bookshop.util.query_creator.EntityQueryCreator;
import com.epam.bookshop.util.query_creator.impl.EntityQueryCreatorFactory;
import org.testng.annotations.Test;

public class UserQueryCreatorTest {

    @Test
    public void testCreateQuery() {
        Criteria<User> criteria = UserCriteria.builder()
                .id(1L)
                .name("John")
                .email("john@gmail.com")
                .build();
        EntityQueryCreator queryCreator = EntityQueryCreatorFactory.INSTANCE.create(EntityType.BOOK);
        String query = queryCreator.createQuery(criteria, UtilStringConstants.EQUALS);
        System.out.println(query);
    }
}