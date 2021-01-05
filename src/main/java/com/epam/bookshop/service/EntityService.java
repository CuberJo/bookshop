package com.epam.bookshop.service;

import com.epam.bookshop.domain.Entity;
import com.epam.bookshop.criteria.Criteria;
import com.epam.bookshop.exception.EntityNotFoundException;
import com.epam.bookshop.exception.ValidatorException;

import java.util.Collection;
import java.util.Optional;

public interface EntityService<T extends Entity> {

    T create(T entity) throws ValidatorException;
    Collection<T> findAll();
    Collection<T> findAll(Criteria criteria) throws ValidatorException;
    Optional<T> findById(long id) throws EntityNotFoundException;
    Optional<T> find(Criteria criteria) throws ValidatorException;
    Optional<T> update(T entity) throws EntityNotFoundException, ValidatorException;
    boolean delete(long id) throws EntityNotFoundException;
    boolean delete(T entity) throws EntityNotFoundException, ValidatorException;
}
