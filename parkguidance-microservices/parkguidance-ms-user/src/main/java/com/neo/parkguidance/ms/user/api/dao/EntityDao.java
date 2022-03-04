package com.neo.parkguidance.ms.user.api.dao;

import com.neo.parkguidance.ms.user.api.entity.DataBaseEntity;

import java.util.List;
import java.util.Map;

/**
 * This interfaces defines the interactions capability for persistence relational data storage per database entity
 */
public interface EntityDao<T extends DataBaseEntity> {

    /**
     * Creates a entry in the table for the given entity {@link T}
     *
     * @param entity the entity to be created
     */
    void create(T entity);

    /**
     * Edits the given entry in the table for the given entity {@link T}
     *
     * @param entity the entity to be edited
     */
    void edit(T entity);

    /**
     * Removes the entry in the table for the given entity {@link T}
     *
     * @param entity the entity to remove
     */
    void remove(T entity);

    /**
     * Counts the number of entries in the table {@link T}
     *
     * @return the number of entries
     */
    int count();

    /**
     * Finds the entry in the table and returns it as a object {@link T}
     *
     * @param primaryKey the primary key of the searched object
     *
     * @return the entry as a entity object
     */
    T find(Object primaryKey);

    /**
     * Finds all entries in the table and returns is as the object {@link T}
     *
     * @return all entities
     */
    List<T> findAll();

    /**
     * Finds all entries ordered by the column and order direction in the table and returns is as the object {@link T}
     *
     * @param column the column which the entries shall be sorted against
     * @param asc if true it will be sorted ascending else descending
     *
     * @return all entries sorted by the query
     */
    List<T> findAll(String column, boolean asc);

    /**
     * Finds the first entries in which the column value matches the given value </b>
     * Returns null if none are found
     *
     * @param columnName the column for the match
     * @param value the value to be matched
     *
     * @return the first entries which matches the query
     */
    T findOneByColumn(String columnName, Object value);



    /**
     * Finds the first entries in which the value matches the column </b>
     * Returns null if none are found
     *
     * @param column the column and data associated with the desired entity
     *
     * @return the first entries which matches the query
     */
    T findOneByColumn(Map<String, Object> column);

    /**
     * Finds all entries in which the column value matches the given value </b>
     * Returns {@link java.util.Collections#emptyList()} if none are found
     *
     * @param columnName the column for the match
     * @param value the value to be matched
     *
     * @return all entries which match the query
     */
    List<T> findByColumn(String columnName, Object value);

    /**
     * Finds all entries in which the column value matches the given value </b>
     * Returns {@link java.util.Collections#emptyList()} if none are found
     *
     * @param column the column for the match
     * @param order the value to be matched
     *
     * @return all entries which match the query
     */
    List<T> findByColumn(Map<String, Object> column, Map<String, Boolean> order);

    /**
     * Finds all entries in which the column value matches the given value </b>
     * Returns {@link java.util.Collections#emptyList()} if none are found
     *
     * @param column the column and data associated with the desired entity
     * @param order the order to sort the data against
     * @param offset the offset of the first value
     * @param maxReturn max entities to return
     *
     * @return all entries which match the query
     */
    List<T> findByColumn(Map<String, Object> column, Map<String, Boolean> order, int offset ,int maxReturn);
}
