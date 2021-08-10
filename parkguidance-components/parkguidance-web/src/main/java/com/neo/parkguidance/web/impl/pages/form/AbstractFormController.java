package com.neo.parkguidance.web.impl.pages.form;

import com.neo.parkguidance.core.entity.DataBaseEntity;
import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;

import javax.inject.Inject;

import static com.github.adminfaces.template.util.Assert.has;
import static com.neo.parkguidance.web.impl.utils.Utils.addDetailMessage;

public abstract class AbstractFormController<T extends DataBaseEntity<T>> {

    @Inject
    AbstractFormModel<T> model;

    @Inject
    AbstractFormFacade<T> facade;

    public void init() {
        if(Faces.isAjaxRequest()){
            return;
        }

        if (has(model.getPrimaryKey())) {
            model.setEntity(facade.findGarageById(model.getPrimaryKey()));
        } else {
            model.setEntity(facade.newEntity());
        }
    }

    protected abstract String getRedirectLocation();

    public void remove() {
        if (facade.remove(model.getEntity())) {
            addDetailMessage(model.getEntity().getClass().getSimpleName() + " " + model.getEntity().getPrimaryKey()
                    + " removed successfully");
            Faces.getFlash().setKeepMessages(true);
            Faces.redirect(getRedirectLocation());
        }
    }

    public void save() {
        try {
            StringBuilder msg = new StringBuilder(model.getEntity().getClass().getSimpleName() + " " + model.getEntity().getPrimaryKey());

            if (model.getPrimaryKey() == null) {
                facade.create(model.getEntity());
                msg.append(" created successfully");
            } else {
                facade.edit(model.getEntity());
                msg.append(" updated successfully");
            }

            addDetailMessage(msg.toString());
        }catch (Exception e) {
            Messages.addError(null, e.getMessage());
        }
    }

    public void clear() {
        model.setEntity(facade.newEntity());
        model.setPrimaryKey(null);
    }

    public boolean isNew() {
        return model.getEntity() == null || model.getEntity().getPrimaryKey() == null;
    }

    public AbstractFormModel<T> getModel() {
        return model;
    }

    public AbstractFormFacade<T> getFacade() {
        return facade;
    }
}
