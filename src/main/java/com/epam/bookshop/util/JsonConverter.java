package com.epam.bookshop.util;

import com.epam.bookshop.domain.Entity;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Converts instances to JSON
 */
public class JsonConverter {
    private static final Logger logger = LoggerFactory.getLogger(JsonConverter.class);

    private static JsonConverter instance;
    private static final ReentrantLock lock = new ReentrantLock();

    /**
     * Thread-safe singleton creation
     * @return instance of {@link JsonConverter} class
     */
    public static JsonConverter getInstance() {
        lock.lock();
        try {
            if (instance == null) {
                instance = new JsonConverter();
            }
        } finally {
            lock.unlock();
        }

        return instance;
    }


    /**
     * @param rows number of rows of entity in database
     */
    public String write(int rows) {
        ObjectMapper mapper = new ObjectMapper();
        String jsonStr = "";
        try {
            jsonStr = mapper.writeValueAsString(rows);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }

        return jsonStr;
    }


    /**
     * @param entity entity to serialize into JSON
     */
    public String write(Entity entity) {
        ObjectMapper mapper = new ObjectMapper();
        String jsonStr = "";
        try {
            jsonStr = mapper.writeValueAsString(entity);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }

        return jsonStr;
    }

    /**
     * @param entities {@link Collection} entities to serialize into JSON
     */
    public String write(Collection<? extends Entity> entities) {
        ObjectMapper mapper = new ObjectMapper();
        String jsonStr = "";
        try {
            jsonStr = mapper.writeValueAsString(entities);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }

        return jsonStr;
    }

    /**
     * @param os {@link OutputStream} object used to write to
     * @param entity entity to serialize into JSON
     */
    public void write(OutputStream os, Entity entity) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writeValue(os, entity);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * @param os os {@link OutputStream} object used to write to
     * @param entities {@link Collection} entities to serialize into JSON
     */
    public void write(OutputStream os, Collection<? extends Entity> entities) {
        entities.forEach(entity -> write(os, entity));
    }
}
