package com.neo.parkguidance.web.impl.entity.converter;

import com.neo.parkguidance.core.entity.Permission;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.inject.Named;

/**
 * This class is {@link javax.faces.convert.Converter} impl for the {@link Permission} entity
 */
@Named(value = EntityPermissionConverter.BEAN_NAME)
public class EntityPermissionConverter extends AbstractDataBaseEntityConverter<Permission> {

    public static final String BEAN_NAME = "entityPermissionConverter";

    @Override
    public Permission getAsObject(FacesContext context, UIComponent component, String value) {
        Permission object = abstractEntityDao.findOneByColumn(Permission.C_NAME, value);
        if (object == null) {
            return newInstance();
        }
        return object;
    }
    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent,
            Permission object) {
        return object.getName();
    }

    @Override
    protected Permission newInstance() {
        return new Permission();
    }
}
