package com.neo.parkguidance.ms.user.impl.dao;

import com.neo.parkguidance.ms.user.api.entity.DataBaseEntity;
import com.neo.parkguidance.ms.user.api.dao.EntityDao;

import javax.persistence.*;
import javax.persistence.criteria.*;
import javax.transaction.Transactional;
import java.util.*;

@Transactional( rollbackOn = PersistenceException.class)
public abstract class AbstractEntityDao<T extends DataBaseEntity> implements EntityDao<T> {

    protected final Class<T> entityClass;

    protected AbstractEntityDao(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    protected abstract EntityManager getEntityManager();

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
        try {
            return getEntityManager().find(entityClass, primaryKey);
        } catch (NoResultException | IllegalArgumentException  ex) {
            return null;
        }
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

        Order order;
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
        Query q = getEntityManager().createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

    public T findOneByColumn(String columnName, Object value) {
        Map<String, Object> column = new HashMap<>();
        column.put(columnName, value);
        return findOneByColumn(column);
    }

    public T findOneByColumn(Map<String, Object> column) {
        try {
            return getTypedQueryByColumn(column, Collections.emptyMap()).getSingleResult();
        } catch (NoResultException | IllegalArgumentException ex) {
            return null;
        }
    }

    public List<T> findByColumn(String columnName, Object value) {
        Map<String, Object> column = new HashMap<>();
        column.put(columnName, value);
        return findByColumn(column, Collections.emptyMap());
    }

    public List<T> findByColumn(Map<String, Object> column, Map<String, Boolean> order) {
        try {
            return getTypedQueryByColumn(column, order).getResultList();
        } catch (NoResultException | IllegalArgumentException  ex) {
            return Collections.emptyList();
        }
    }

    public List<T> findByColumn(Map<String, Object> column, Map<String, Boolean> order, int offset ,int maxReturn) {
        try {
            return getTypedQueryByColumn(column, order).setFirstResult(offset).setMaxResults(maxReturn).getResultList();
        } catch (NoResultException | IllegalArgumentException  ex) {
            return Collections.emptyList();
        }
    }

    protected TypedQuery<T> getTypedQueryByColumn(Map<String, Object> columns, Map<String, Boolean> order) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();

        AbstractQuery<T> aQuery = cb.createQuery(entityClass);

        Root<T> root = aQuery.from(entityClass);
        Predicate predicate = cb.isTrue(cb.literal(true));
        for (Map.Entry<String, Object> column: columns.entrySet()) {
            predicate = cb.and(predicate ,cb.equal(root.get(column.getKey()), column.getValue()));
        }
        aQuery.where(predicate);

        CriteriaQuery<T> cQuery = ((CriteriaQuery<T>) aQuery).select(root);
        cQuery.orderBy(mapOrders(order, cb, root));
        return getEntityManager().createQuery(cQuery);
    }

    protected List<Order> mapOrders(Map<String, Boolean> orders, CriteriaBuilder cb, Root<T> root) {
        List<Order> orderList = new ArrayList<>();
        for (Map.Entry<String, Boolean> order: orders.entrySet()) {
            if (order.getValue().booleanValue()) {
                orderList.add(cb.asc(root.get(order.getKey())));
            } else {
                orderList.add(cb.desc(root.get(order.getKey())));
            }

        }
        return orderList;
    }
}
