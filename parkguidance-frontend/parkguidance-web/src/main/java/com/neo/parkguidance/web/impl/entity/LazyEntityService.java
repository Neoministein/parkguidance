package com.neo.parkguidance.web.impl.entity;

import com.neo.parkguidance.framework.api.dao.EntityDao;
import com.neo.parkguidance.framework.entity.DataBaseEntity;
import com.neo.parkguidance.framework.impl.utils.MathUtils;
import com.neo.parkguidance.web.impl.table.Filter;
import org.hibernate.criterion.Order;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;

import java.util.List;
import java.util.Map;

/**
 * This class is a Generic Implementation for a {@link DataBaseEntity} {@link LazyDataModel}
 * @param <T> the wanted DataBaseEntity
 */
public class LazyEntityService<T extends DataBaseEntity> extends LazyDataModel<T> {

    private final EntityDao<T> entityDao;
    private final Filter<T> filter;

    public LazyEntityService(EntityDao<T> entityDao, Filter<T> filter) {
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
        List<T> dataSource = entityDao.findLikeExample(filter.getEntity(), first, pageSize, order.getPropertyName(), order.isAscending());
        setRowCount(Math.toIntExact(entityDao.findCountLikeExample(filter.getEntity())));
        return dataSource;
    }

    @Override
    public T getRowData(String rowKey) {
        Long number = MathUtils.parseLong(rowKey);
        if (number != null) {
            return entityDao.find(number);
        }
        return entityDao.find((rowKey));
    }

    @Override
    public Object getRowKey(T object) {
        return object.getPrimaryKey();
    }
}
