package com.neo.parkguidance.admin.infra.entity;

import com.neo.parkguidance.core.entity.ParkingGarage;
import com.neo.parkguidance.core.impl.dao.ParkingGarageEntityManager;
import com.neo.parkguidance.admin.infra.table.Filter;
import com.neo.parkguidance.admin.infra.table.SortOrder;

import javax.ejb.Stateless;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.github.adminfaces.template.util.Assert.has;

@Stateless
public class ParkingGarageEntityService extends ParkingGarageEntityManager {

    public List<ParkingGarage> paginate(Filter<ParkingGarage> filter) {
        List<ParkingGarage> pagedCars = new ArrayList<>();
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

        List<Predicate<ParkingGarage>> predicates = configFilter(filter);

        List<ParkingGarage> pagedList = findAll().stream().filter(predicates
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

    private List<Predicate<ParkingGarage>> configFilter(Filter<ParkingGarage> filter) {
        List<Predicate<ParkingGarage>> predicates = new ArrayList<>();
        if (filter.hasParam("id")) {
            Predicate<ParkingGarage> idPredicate = (ParkingGarage c) -> filter.getParam("id").equals(c.getId());
            predicates.add(idPredicate);
        }

        if (has(filter.getEntity())) {
            ParkingGarage filterEntity = filter.getEntity();
            if (has(filterEntity.getName())) {
                Predicate<ParkingGarage> namePredicate = (ParkingGarage c) -> c.getName().toLowerCase().contains(filterEntity.getName().toLowerCase());
                predicates.add(namePredicate);
            }
        }
        return predicates;
    }
    public long count(Filter<ParkingGarage> filter) {
        return findAll().stream()
                .filter(configFilter(filter).stream()
                        .reduce(Predicate::or).orElse(t -> true))
                .count();
    }
}
