package com.epam.bookshop.validator;

import com.epam.bookshop.util.criteria.Criteria;
import com.epam.bookshop.domain.Entity;
import com.epam.bookshop.exception.ValidatorException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * Interface for validation
 *
 * @param <T> annotation type
 */
public interface AnnotationValidator<T extends Annotation>  {

    void validateEntity(Field field, T annotation, Entity entity) throws ValidatorException;

    void validateCriteria(Field field, T annotation, Criteria<? extends Entity> criteria) throws ValidatorException;
}
