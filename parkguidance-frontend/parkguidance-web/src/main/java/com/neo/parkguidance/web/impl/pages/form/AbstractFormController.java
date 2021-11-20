package com.neo.parkguidance.web.impl.pages.form;

import com.neo.parkguidance.framework.entity.DataBaseEntity;
import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;

import static com.github.adminfaces.template.util.Assert.has;
import static com.neo.parkguidance.web.impl.utils.Utils.addDetailMessage;

public abstract class AbstractFormController<T extends DataBaseEntity> {
    
    public void init() {
        if (!getModel().isInitialized()) {
            if (has(getModel().getPrimaryKey())) {
                getModel().setEntity(getFacade().findEntityById(getModel().getPrimaryKey()));
            } else {
                getModel().setEntity(getFacade().newEntity());
            }
            getModel().setInitialized(true);
        }
    }

    protected abstract String getRedirectLocation();

    public void remove() {
        if (getFacade().remove(getModel().getEntity())) {
            addDetailMessage(getModel().getEntity().getClass().getSimpleName() + " " + getModel().getEntity().getPrimaryKey()
                    + " removed successfully");
            Faces.getFlash().setKeepMessages(true);
            Faces.redirect(getRedirectLocation());
        }
    }

    public void save() {
        try {
            StringBuilder msg = new StringBuilder(getModel().getEntity().getClass().getSimpleName() + " " + getModel().getEntity().getPrimaryKey());

            if (getModel().getPrimaryKey() == null) {
                getFacade().create(getModel().getEntity());
                msg.append(" created successfully");
            } else {
                getFacade().edit(getModel().getEntity());
                msg.append(" updated successfully");
            }

            addDetailMessage(msg.toString());
        }catch (Exception e) {
            Messages.addError(null, e.getMessage());
        }
    }

    public void clear() {
        getModel().setEntity(getFacade().newEntity());
        getModel().setPrimaryKey(null);
    }

    public boolean isNew() {
        return getModel().getEntity() == null || getModel().getEntity().getPrimaryKey() == null;
    }

    public abstract AbstractFormModel<T> getModel();

    protected abstract AbstractFormFacade<T> getFacade();
}
