package com.neo.parkguidance.web.infra.entity;

import com.neo.parkguidance.core.entity.DataBaseEntity;
import com.neo.parkguidance.core.impl.dao.AbstractEntityDao;
import com.neo.parkguidance.web.infra.table.Filter;
import org.hibernate.criterion.Order;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;

import java.util.List;
import java.util.Map;

public class LazyEntityService<T extends DataBaseEntity<T>> extends LazyDataModel<T> {

    private final AbstractEntityDao<T> entityDao;
    private final Filter<T> filter;

    public LazyEntityService(AbstractEntityDao<T> entityDao, Filter<T> filter) {
        this.entityDao = entityDao;
        this.filter = filter;
    }

    @Override
    public List<T> load(int first, int pageSize, String sortField, org.primefaces.model.SortOrder sortOrder,
            Map<String, FilterMeta> filterBy) {
        Order order = Order.asc(DataBaseEntity.C_ID);
        if (sortOrder != null && sortField != null) {
            switch (sortOrder) {
            case ASCENDING:
            case UNSORTED:
                order = Order.asc(sortField);
                break;
            case DESCENDING:
                order = Order.desc(sortField);
                break;
            }
        }
        List<T> dataSource = entityDao.findLikeExample(filter.getEntity(), first, pageSize, order);
        setRowCount(Math.toIntExact(entityDao.findCountLikeExample(filter.getEntity())));
        return dataSource;
    }

    @Override
    public T getRowData(String rowKey) {
        return entityDao.find(Long.valueOf(rowKey));
    }

    @Override
    public Object getRowKey(T object) {
        return object.getId();
    }
}
