package com.epam.bookshop.strategy.query_creator.impl;

import com.epam.bookshop.criteria.Criteria;
import com.epam.bookshop.criteria.impl.BookCriteria;
import com.epam.bookshop.domain.impl.EntityType;
import com.epam.bookshop.strategy.query_creator.FindEntityQueryCreator;
import com.epam.bookshop.strategy.query_creator.impl.FindEntityQueryCreatorFactory;
import org.testng.annotations.Test;

public class FindBookQueryCreatorTest {

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

        FindEntityQueryCreator queryCreator = FindEntityQueryCreatorFactory.INSTANCE.create(EntityType.BOOK);
        queryCreator.setLocale("EN");
        String query = queryCreator.createQuery(criteria);
        System.out.println(query);
    }
}