package com.neo.parkguidance.core.impl.dao;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.List;

public abstract class AbstractEntityFacade<T> {

    protected final Class<T> entityClass;

    protected AbstractEntityFacade(Class<T> entityClass) {
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

    public T find(Object id) {
        return getEntityManager().find(entityClass, id);
    }

    public List<T> findAll() {
        CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        return getEntityManager().createQuery(cq).getResultList();
    }

    public List<T> findRange(int[] range) {
        CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        q.setMaxResults(range[1] - range[0] + 1);
        q.setFirstResult(range[0]);
        return q.getResultList();
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
        List<T> list1 = tq1.getResultList();

        return list1;
    }

    public T findHighestId() {
        CriteriaBuilder cb= getEntityManager().getCriteriaBuilder();

        AbstractQuery<T> cq1=cb.createQuery(entityClass);

        Root<T> stud1=cq1.from(entityClass);

        CriteriaQuery<T> select1 = ((CriteriaQuery<T>) cq1).select(stud1);
        select1.orderBy(cb.desc(stud1.get("id")));
        TypedQuery<T> tq1 = getEntityManager().createQuery(select1);
        List<T> list1 = tq1.setMaxResults(1).getResultList();

        if(!list1.isEmpty()) {

            return list1.get(0);
        }
            throw new IllegalArgumentException(getClass().getName() + " no entries found");
    }

    public T findById(Integer id) {
        List<T> list = findByColumn("id",id);
        if(!list.isEmpty()) {

            return list.get(0);
        } else {
            throw new IllegalArgumentException(getClass().getName() + " not found with id " + id);
        }
    }


}
