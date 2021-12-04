package com.neo.parkguidance.microservices.impl.dao;

import com.neo.parkguidance.framework.api.dao.EntityDao;
import com.neo.parkguidance.framework.entity.GarageInfo;
import com.neo.parkguidance.microservices.api.dao.EntityDaoAbstraction;
import org.hibernate.Criteria;

import javax.enterprise.context.RequestScoped;
import javax.persistence.RollbackException;
import java.util.List;
import java.util.Map;

/**
 * //TODO IMPLEMENT ELASTIC
 */
@RequestScoped
public class GarageInfoRepository implements EntityDaoAbstraction<GarageInfo>, EntityDao<GarageInfo> {

    @Override
    public void addSubCriteria(Criteria criteria, GarageInfo object) {

    }

    @Override
    public void create(GarageInfo entity) {
        throw new RollbackException();
    }

    @Override
    public void edit(GarageInfo entity) {
        throw new RollbackException();
    }

    @Override
    public void remove(GarageInfo entity) {
        throw new RollbackException();
    }

    @Override
    public int count() {
        return 0;
    }

    @Override
    public GarageInfo find(Object primaryKey) {
        return null;
    }

    @Override
    public List<GarageInfo> findAll() {
        return null;
    }

    @Override
    public List<GarageInfo> findAll(String column, boolean asc) {
        return null;
    }

    @Override
    public List<GarageInfo> findByColumn(String columnName, Object value) {
        return null;
    }

    @Override
    public GarageInfo findOneByColumn(String columnName, Object value) {
        return null;
    }

    @Override
    public GarageInfo findOneByColumn(Map<String, Object> column) {
        return null;
    }

    @Override
    public List<GarageInfo> findLikeExample(GarageInfo object) {
        return null;
    }

    @Override
    public List<GarageInfo> findLikeExample(GarageInfo object, int first, int pageSize, String columnToSortBy,
            boolean ascending) {
        return null;
    }

    @Override
    public Long findCountLikeExample(GarageInfo object) {
        return null;
    }
}
