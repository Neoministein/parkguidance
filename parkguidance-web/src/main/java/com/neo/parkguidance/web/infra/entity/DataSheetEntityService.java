package com.neo.parkguidance.web.infra.entity;

import com.neo.parkguidance.core.entity.DataSheet;
import com.neo.parkguidance.core.impl.dao.DataSheetEntityManager;
import com.neo.parkguidance.web.infra.table.Filter;
import com.neo.parkguidance.web.infra.table.SortOrder;

import javax.ejb.Stateless;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.github.adminfaces.template.util.Assert.has;

@Stateless
public class DataSheetEntityService extends DataSheetEntityManager {

    public List<DataSheet> paginate(Filter<DataSheet> filter) {
        List<DataSheet> pagedCars = new ArrayList<>();
        if(!SortOrder.UNSORTED.equals(filter.getSortOrder())) {
            pagedCars = findAll().stream().
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
            pagedCars = pagedCars.subList(filter.getFirst(), Math.min(page, findAll().size()));
            return pagedCars;
        }

        List<Predicate<DataSheet>> predicates = configFilter(filter);

        List<DataSheet> pagedList = findAll().stream().filter(predicates
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

    private List<Predicate<DataSheet>> configFilter(Filter<DataSheet> filter) {
        List<Predicate<DataSheet>> predicates = new ArrayList<>();
        if (filter.hasParam("id")) {
            Predicate<DataSheet> idPredicate = (DataSheet c) -> filter.getParam("id").equals(c.getId());
            predicates.add(idPredicate);
        }

        return predicates;
    }
    public long count(Filter<DataSheet> filter) {
        return findAll().stream()
                .filter(configFilter(filter).stream()
                        .reduce(Predicate::or).orElse(t -> true))
                .count();
    }
}
