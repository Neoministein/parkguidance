package com.neo.parkguidance.web.impl.entity.converter;

import com.neo.parkguidance.framework.api.dao.EntityDao;
import com.neo.parkguidance.framework.entity.DataBaseEntity;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Inject;

/**
 * This abstract class is used for creating a {@link DataBaseEntity} for {@link Converter}
 * @param <T> a {@link DataBaseEntity} implementation
 */
public abstract class AbstractDataBaseEntityConverter<T extends DataBaseEntity> implements Converter<T> {

    @Inject
    EntityDao<T> abstractEntityDao;

    protected abstract T newInstance();

    public T getAsObject(FacesContext context, UIComponent component, String value) {
        T object = abstractEntityDao.find(value);
        if (object == null) {
            return newInstance();
        }
        return object;
    }

    public String getAsString(FacesContext facesContext, UIComponent uiComponent,
            T object) {
        return object.getPrimaryKey().toString();
    }
}
