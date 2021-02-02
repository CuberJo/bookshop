package com.epam.bookshop.service;

import com.epam.bookshop.domain.Entity;
import com.epam.bookshop.util.criteria.Criteria;
import com.epam.bookshop.exception.EntityNotFoundException;
import com.epam.bookshop.exception.ValidatorException;

import java.util.Collection;
import java.util.Optional;

/**
 * Business layer interface of the application
 */
public interface EntityService<T extends Entity> {

    T create(T entity) throws ValidatorException;
    Collection<T> findAll();
    Collection<T> findAll(Criteria<T> criteria) throws ValidatorException;
    Collection<T> findAll(int start, int total);
    Optional<T> findById(long id) throws EntityNotFoundException;
    Optional<T> find(Criteria<T> criteria) throws ValidatorException;
    Optional<T> update(T entity) throws ValidatorException;
    boolean delete(long id) throws EntityNotFoundException;
    boolean delete(T entity) throws EntityNotFoundException, ValidatorException;
    int count();

    /**
     * @param locale service language for error messages
     */
    void setLocale(String locale);
}
