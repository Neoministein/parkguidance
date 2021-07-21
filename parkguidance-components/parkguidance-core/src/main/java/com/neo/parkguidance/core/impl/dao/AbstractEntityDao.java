package com.neo.parkguidance.core.impl.dao;

import com.neo.parkguidance.core.entity.DataBaseEntity;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.*;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

public abstract class AbstractEntityDao<T extends DataBaseEntity<T>> {

    protected final Class<T> entityClass;

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

    public T find(Object id) {
        return getEntityManager().find(entityClass, id);
    }

    public List<T> findAll() {
        CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        return getEntityManager().createQuery(cq).getResultList();
    }

    public int count() {
        CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        Root<T> rt = cq.from(entityClass);
        cq.select(getEntityManager().getCriteriaBuilder().count(rt));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

    public List<T> findByColumn(String columnName, Object columnData) {
        CriteriaBuilder cb= getEntityManager().getCriteriaBuilder();

        AbstractQuery<T> cq1=cb.createQuery(entityClass);

        Root<T> stud1=cq1.from(entityClass);

        cq1.where(cb.equal(stud1.get(columnName), columnData));

        CriteriaQuery<T> select1 = ((CriteriaQuery<T>) cq1).select(stud1);
        TypedQuery<T> tq1 = getEntityManager().createQuery(select1);
        return tq1.getResultList();
    }

    public List<T> findLikeExample(T object, int first, int pageSize ,org.hibernate.criterion.Order order) {
        Session session = (Session) getEntityManager().getDelegate();
        Criteria criteria = session.createCriteria(entityClass);
        Example example = Example.create(object).ignoreCase().enableLike(MatchMode.ANYWHERE);
        criteria.add(example);

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
