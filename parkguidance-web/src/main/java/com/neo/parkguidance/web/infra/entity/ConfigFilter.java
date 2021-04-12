package com.neo.parkguidance.web.infra.entity;

import com.neo.parkguidance.core.entity.DataBaseEntity;
import com.neo.parkguidance.web.infra.table.Filter;

import java.util.List;
import java.util.function.Predicate;

public interface ConfigFilter<T extends DataBaseEntity> {

    List<Predicate<T>> filter(Filter<T> filter);
}
