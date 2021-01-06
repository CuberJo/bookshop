package com.epam.bookshop.strategy.validator;

import com.epam.bookshop.criteria.Criteria;
import com.epam.bookshop.domain.Entity;
import com.epam.bookshop.exception.ValidatorException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public interface Validatable<T extends Annotation>  {
    void validateEntity(Field field, T annotation, Entity entity) throws ValidatorException;
    void validateCriteria(Field field, T annotation, Criteria criteria) throws ValidatorException;
    void setLocale(String locale);
}
