package com.hkg.helidon.airport.repository;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.transform.DistinctRootEntityResultTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public abstract class AbstractEntityDao<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractEntityDao.class);

    protected final Class<T> entityClass;

    @Inject
    protected AbstractEntityDao(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    protected abstract EntityManager getEntityManager();

    public void addSubCriteria(Criteria criteria, T object) {}

    public void create(T entity) {
        getEntityManager().persist(entity);
    }

    public void edit(T entity) {
        getEntityManager().merge(entity);
    }

    public void remove(T entity) {
        getEntityManager().remove(getEntityManager().merge(entity));
    }

    public T find(Object primaryKey) {
        return getEntityManager().find(entityClass, primaryKey);
    }

    public List<T> findAll() {
        @SuppressWarnings("java:S3740")
        CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        return getEntityManager().createQuery(cq).getResultList();
    }

    public List<T> findAll(String column, boolean asc) {
        CriteriaBuilder cb= getEntityManager().getCriteriaBuilder();

        CriteriaQuery<T> cq1=cb.createQuery(entityClass);

        Root<T> stud1=cq1.from(entityClass);

        CriteriaQuery<T> select1 = cq1.select(stud1);

        javax.persistence.criteria.Order order;
        if (asc) {
            order = cb.asc(stud1.get(column));
        } else {
            order = cb.desc(stud1.get(column));
        }

        select1.orderBy(order);
        return getEntityManager().createQuery(select1).getResultList();
    }

    public int count() {
        @SuppressWarnings("java:S3740")
        CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        Root<T> rt = cq.from(entityClass);
        cq.select(getEntityManager().getCriteriaBuilder().count(rt));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

    public List<T> findByColumn(String columnName, Object value) {
        try {
            return getTypedQueryByColumn(columnName, value).getResultList();
        } catch (NoResultException ex) {
            return Collections.emptyList();
        }
    }

    public T findOneByColumn(String columnName, Object value) {
        try {
            return getTypedQueryByColumn(columnName, value).getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }

    public T findOneByColumn(Map<String, Object> column) {
        try {
            return getTypedQueryByColumn(column).getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }

    protected TypedQuery<T> getTypedQueryByColumn(String columnName, Object columnData) {
        CriteriaBuilder cb= getEntityManager().getCriteriaBuilder();

        AbstractQuery<T> cq1=cb.createQuery(entityClass);

        Root<T> stud1=cq1.from(entityClass);

        cq1.where(cb.equal(stud1.get(columnName), columnData));

        CriteriaQuery<T> select1 = ((CriteriaQuery<T>) cq1).select(stud1);
        return getEntityManager().createQuery(select1);
    }

    protected TypedQuery<T> getTypedQueryByColumn(Map<String, Object> columns) {
        CriteriaBuilder cb= getEntityManager().getCriteriaBuilder();

        AbstractQuery<T> cq1=cb.createQuery(entityClass);

        Root<T> stud1=cq1.from(entityClass);
        Predicate predicate = cb.isTrue(cb.literal(true));
        for (Map.Entry<String, Object> column: columns.entrySet()) {
            predicate = cb.and(predicate ,cb.equal(stud1.get(column.getKey()), column.getValue()));
        }
        cq1.where(predicate);

        CriteriaQuery<T> select1 = ((CriteriaQuery<T>) cq1).select(stud1);
        return getEntityManager().createQuery(select1);
    }

    public List<T> findLikeExample(T object) {
        Session session = (Session) getEntityManager().getDelegate();
        Criteria criteria = session.createCriteria(entityClass);
        Example example = Example.create(object).ignoreCase().enableLike(MatchMode.ANYWHERE);
        criteria.add(example);

        addSubCriteria(criteria, object);
        return criteria.list();
    }

    public List<T> findLikeExample(T object, int first, int pageSize ,org.hibernate.criterion.Order order) {
        Session session = (Session) getEntityManager().getDelegate();
        Criteria criteria = session.createCriteria(entityClass);
        Example example = Example.create(object).ignoreCase().enableLike(MatchMode.ANYWHERE);
        criteria.add(example);
        criteria.setResultTransformer(DistinctRootEntityResultTransformer.INSTANCE);

        addSubCriteria(criteria, object);

        criteria.setFirstResult(first);
        criteria.setMaxResults(pageSize);
        criteria.addOrder(order);
        return criteria.list();
    }

    public Long findCountLikeExample(T object) {
        Session session = (Session) getEntityManager().getDelegate();
        Criteria criteria = session.createCriteria(entityClass);
        Example example = Example.create(object).ignoreCase().enableLike(MatchMode.ANYWHERE);
        criteria.add(example);
        criteria.setProjection(Projections.rowCount());
        return (Long) criteria.uniqueResult();
    }
}
