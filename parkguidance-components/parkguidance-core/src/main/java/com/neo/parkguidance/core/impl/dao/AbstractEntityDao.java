package com.neo.parkguidance.core.impl.dao;

import com.neo.parkguidance.core.entity.DataBaseEntity;
import com.neo.parkguidance.core.impl.event.DataBaseEntityChangeEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.*;

import javax.enterprise.event.Event;
import javax.enterprise.event.ObserverException;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

public abstract class AbstractEntityDao<T extends DataBaseEntity<T>> {

    private static final Logger LOGGER = LogManager.getLogger(AbstractEntityDao.class);

    @Inject
    Event<DataBaseEntityChangeEvent<T>> event;

    protected final Class<T> entityClass;

    @Inject
    protected AbstractEntityDao(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    protected abstract EntityManager getEntityManager();

    public void addSubCriteria(Criteria criteria, T object) {}

    public void create(T entity) {
        getEntityManager().persist(entity);
        fireEvent(new DataBaseEntityChangeEvent<>(DataBaseEntityChangeEvent.CREATE, entity));
    }

    public void edit(T entity) {
        getEntityManager().merge(entity);
        fireEvent(new DataBaseEntityChangeEvent<>(DataBaseEntityChangeEvent.EDIT, entity));
    }

    public void remove(T entity) {
        getEntityManager().remove(getEntityManager().merge(entity));
        fireEvent(new DataBaseEntityChangeEvent<>(DataBaseEntityChangeEvent.REMOVE, entity));
    }

    public T find(Object id) {
        return getEntityManager().find(entityClass, id);
    }

    public List<T> findAll() {
        @SuppressWarnings("java:S3740")
        CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        return getEntityManager().createQuery(cq).getResultList();
    }

    public int count() {
        @SuppressWarnings("java:S3740")
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

    private void fireEvent(DataBaseEntityChangeEvent<T> changeEvent) {
        try {
            event.fire(changeEvent);
        } catch (ObserverException | IllegalArgumentException ex) {
            LOGGER.warn("An exception occurred while firing event [{}] [{}] [{}] {}",
                    changeEvent.getStatus(),
                    entityClass.getName(),
                    changeEvent.getChangedObject().getPrimaryKey(),
                    ex.getMessage());
        }
    }
}
