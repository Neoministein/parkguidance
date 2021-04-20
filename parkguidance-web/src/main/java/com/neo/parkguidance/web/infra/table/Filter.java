package com.neo.parkguidance.web.infra.table;

import org.hibernate.criterion.Order;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


public class Filter<T extends Serializable> implements Serializable{
    private T entity;
    private int first;
    private int pageSize;
    private String sortField;
    private Order sortOrder;
    private Map<String, Object> params = new HashMap<>();


    public Filter() {
    }

    public Filter(T entity) {
        this.entity = entity;
    }

    public Filter setFirst(int first) {
        this.first = first;
        return this;
    }

    public int getFirst() {
        return first;
    }

    public Filter setPageSize(int pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    public int getPageSize() {
        return pageSize;
    }

    public Filter setSortField(String sortField) {
        this.sortField = sortField;
        return this;
    }

    public String getSortField() {
        return sortField;
    }

    public Filter setSortOrder(Order sortOrder) {
        this.sortOrder = sortOrder;
        return this;
    }

    public Order getSortOrder() {
        return sortOrder;
    }

    public Filter setParams(Map<String, Object> params) {
        this.params = params;
        return this;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public T getEntity() {
        return entity;
    }

    public Filter setEntity(T entity) {
        this.entity = entity;
        return this;
    }

    public Filter addParam(String key, Object value) {
        getParams().put(key, value);
        return this;
    }

    public boolean hasParam(String key) {
        return getParams().containsKey(key) && getParam(key) != null;
    }

    public Object getParam(String key) {
        return getParams().get(key);
    }
}
