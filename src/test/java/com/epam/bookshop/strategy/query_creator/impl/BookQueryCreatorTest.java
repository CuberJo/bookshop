package com.epam.bookshop.strategy.query_creator.impl;

import com.epam.bookshop.util.constant.UtilStringConstants;
import com.epam.bookshop.util.criteria.Criteria;
import com.epam.bookshop.util.criteria.impl.BookCriteria;
import com.epam.bookshop.domain.impl.EntityType;
import com.epam.bookshop.util.query_creator.EntityQueryCreator;
import com.epam.bookshop.util.query_creator.impl.EntityQueryCreatorFactory;
import org.testng.annotations.Test;

public class BookQueryCreatorTest {

    @Test
    public void testCreateQuery() {
        Criteria criteria = BookCriteria.builder()
//                .id(1L)
//                .ISBN("!@$!@1241")
//                .title("asg")
                .price(13.0)
//                .publisher("Agaw")
                .genreId(1L)
                .build();

        EntityQueryCreator queryCreator = EntityQueryCreatorFactory.INSTANCE.create(EntityType.BOOK);
        String query = queryCreator.createQuery(criteria, UtilStringConstants.EQUALS);
        System.out.println(query);
    }
}