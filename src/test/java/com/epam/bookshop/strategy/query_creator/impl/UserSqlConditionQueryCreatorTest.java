package com.epam.bookshop.strategy.query_creator.impl;

import com.epam.bookshop.constant.UtilStringConstants;
import com.epam.bookshop.util.criteria.Criteria;
import com.epam.bookshop.util.criteria.impl.UserCriteria;
import com.epam.bookshop.domain.impl.EntityType;
import com.epam.bookshop.domain.impl.User;
import com.epam.bookshop.util.query_creator.SqlConditionQueryCreator;
import com.epam.bookshop.util.query_creator.impl.SqlConditionQueryCreatorFactory;
import org.testng.annotations.Test;

public class UserSqlConditionQueryCreatorTest {

    @Test
    public void testCreateQuery() {
        Criteria<User> criteria = UserCriteria.builder()
                .id(1L)
                .name("John")
                .email("john@gmail.com")
                .build();
        SqlConditionQueryCreator queryCreator = SqlConditionQueryCreatorFactory.INSTANCE.create(EntityType.BOOK);
        String query = queryCreator.createQuery(criteria, UtilStringConstants.EQUALS);
        System.out.println(query);
    }
}