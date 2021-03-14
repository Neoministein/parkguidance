package com.neo.parkguidance.web.infra.entity;

import com.github.adminfaces.starter.infra.model.SortOrder;
import com.github.adminfaces.starter.model.Car;
import com.neo.parkguidance.entity.ParkingData;
import com.neo.parkguidance.web.infra.table.Filter;

import javax.ejb.Stateless;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.github.adminfaces.template.util.Assert.has;

@Stateless
public class ParkingDataEntityService extends ParkingDataEntityManager {
    public List<ParkingData> paginate(Filter<ParkingData> filter) {
        List<ParkingData> pagedCars = new ArrayList<>();
        if(has(filter.getSortOrder()) && !SortOrder.UNSORTED.equals(filter.getSortOrder())) {
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

        List<Predicate<ParkingData>> predicates = configFilter(filter);

        List<ParkingData> pagedList = findAll().stream().filter(predicates
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

    private List<Predicate<ParkingData>> configFilter(Filter<ParkingData> filter) {
        List<Predicate<ParkingData>> predicates = new ArrayList<>();
        if (filter.hasParam("id")) {
            Predicate<ParkingData> idPredicate = (ParkingData c) -> filter.getParam("id").equals(c.getId());
            predicates.add(idPredicate);
        }

        if (has(filter.getEntity())) {
            ParkingData filterEntity = filter.getEntity();
            if (has(filterEntity.getParkingGarage())) {
                Predicate<ParkingData> namePredicate = (ParkingData c) -> c.getParkingGarage().getName().toLowerCase().contains(filterEntity.getParkingGarage().getName().toLowerCase());
                predicates.add(namePredicate);
            }
        }

        return predicates;
    }
    public long count(Filter<ParkingData> filter) {
        return findAll().stream()
                .filter(configFilter(filter).stream()
                        .reduce(Predicate::or).orElse(t -> true))
                .count();
    }
}
