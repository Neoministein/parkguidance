package com.neo.parkguidance.web.infra.entity;

import com.neo.parkguidance.core.entity.DataBaseEntity;
import com.neo.parkguidance.core.impl.dao.AbstractEntityDao;
import com.neo.parkguidance.web.infra.table.Filter;
import com.neo.parkguidance.web.infra.table.SortOrder;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.github.adminfaces.template.util.Assert.has;

public class LazyEntityService<T extends DataBaseEntity> extends LazyDataModel<T> {

    private final AbstractEntityDao<T> entityDao;
    private final Filter<T> filter;
    private ConfigFilter<T> configFilter;

    public LazyEntityService(AbstractEntityDao<T> entityDao, Filter<T> filter) {
        this.entityDao = entityDao;
        this.filter = filter;
        this.configFilter = config -> predicates();
    }

    @Override
    public List<T> load(int first, int pageSize, String sortField, org.primefaces.model.SortOrder sortOrder,
            Map<String, FilterMeta> filterBy) {
        com.neo.parkguidance.web.infra.table.SortOrder order = null;
        if (sortOrder != null) {
            switch (sortOrder) {
            case UNSORTED:
                order = com.neo.parkguidance.web.infra.table.SortOrder.UNSORTED;
                break;
            case ASCENDING:
                order = com.neo.parkguidance.web.infra.table.SortOrder.ASCENDING;
                break;
            case DESCENDING:
                order =  com.neo.parkguidance.web.infra.table.SortOrder.DESCENDING;
                break;
            }
        }
        filter.setFirst(first).setPageSize(pageSize)
                .setSortField(sortField).setSortOrder(order)
                .setParams(filterBy);
        List<T> list = paginate(filter);
        setRowCount((int) count(filter));

        return list;
    }

    public List<Predicate<T>> predicates() {
        List<Predicate<T>> predicates = new ArrayList<>();
        if (filter.hasParam("id")) {
            Predicate<T> idPredicate = (T c) -> filter.getParam("id").equals(c.getId());
            predicates.add(idPredicate);
        }
        return predicates;

    }

    private List<T> paginate(Filter<T> filter) {
        List<T> pagedCars = new ArrayList<>();
        if(!SortOrder.UNSORTED.equals(filter.getSortOrder())) {
            pagedCars = entityDao.findAll().stream().
                    sorted((c1, c2) -> {
                        if (filter.getSortOrder().isAscending()) {
                            return Long.compare(c1.getId(), c2.getId());
                        } else {
                            return Long.compare(c2.getId(), c1.getId());
                        }
                    })
                    .collect(Collectors.toList());
        }

        int page = filter.getFirst() + filter.getPageSize();
        if (filter.getParams().isEmpty()) {
            pagedCars = pagedCars.subList(filter.getFirst(), Math.min(page, entityDao.findAll().size()));
            return pagedCars;
        }

        List<Predicate<T>> predicates = configFilter.filter(filter);

        List<T> pagedList = entityDao.findAll().stream().filter(predicates
                .stream().reduce(Predicate::or).orElse(t -> true))
                .collect(Collectors.toList());

        if (page < pagedList.size()) {
            pagedList = pagedList.subList(filter.getFirst(), page);
        }

        if (has(filter.getSortField())) {
            pagedList = pagedList.stream().
                    sorted((c1, c2) -> {
                        boolean asc = SortOrder.ASCENDING.equals(filter.getSortOrder());
                        if (asc) {
                            return Long.compare(c1.getId(), c2.getId());
                        } else {
                            return Long.compare(c2.getId(), c1.getId());
                        }
                    })
                    .collect(Collectors.toList());
        }
        return pagedList;
    }

    private long count(Filter<T> filter) {
        return entityDao.findAll().stream()
                .filter(configFilter.filter(filter).stream()
                        .reduce(Predicate::or).orElse(t -> true))
                .count();
    }

    public void setConfigFilter(ConfigFilter<T> configFilter) {
        this.configFilter = configFilter;
    }

    public ConfigFilter<T> getConfigFilter() {
        return configFilter;
    }
}
