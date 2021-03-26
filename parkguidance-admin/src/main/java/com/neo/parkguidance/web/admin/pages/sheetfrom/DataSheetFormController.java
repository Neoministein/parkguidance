package com.neo.parkguidance.web.admin.pages.sheetfrom;

import com.neo.parkguidance.core.entity.DataSheet;
import org.omnifaces.util.Faces;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;

import static com.github.adminfaces.template.util.Assert.has;
import static com.neo.parkguidance.web.utils.Utils.addDetailMessage;

@RequestScoped
@Named(value = DataSheetFormController.BEAN_NAME)
public class DataSheetFormController {

    public static final String BEAN_NAME = "dataSheetForm";

    @Inject
    private DataSheetFormModel model;

    @Inject
    private DataSheetFormFacade facade;

    public void init() {
        if(Faces.isAjaxRequest()){
            return;
        }

        model.setParkingGarages(facade.getParkingGarageList());
        if (has(model.getId())) {
            model.setItem(facade.findById(model.getId()));
        } else {
            model.setItem(new DataSheet());
        }
    }

    public DataSheetFormModel getModel() {
        return model;
    }

    public void remove() throws IOException {
        if (facade.remove(model.getItem())) {
            addDetailMessage("Datasheet from " + model.getItem().getId()
                    + " removed successfully");
            Faces.getFlash().setKeepMessages(true);
            Faces.redirect("dataSheet-list.xhtml");
        }
    }

    public void save() {
        StringBuilder msg = new StringBuilder("DataSheet Data for" + model.getItem().getParkingGarage().getName());

        if (model.getItem().getId() == null) {
            facade.create(model.getItem());
            msg.append(" created successfully");
        } else {
            facade.edit(model.getItem());
            msg.append(" updated successfully");
        }

        addDetailMessage(msg.toString());
    }

    public void clear() {
        model.setItem(new DataSheet());
        model.setId(null);
    }

    public boolean isNew() {
        return model.getItem() == null || model.getItem().getId() == null;
    }
}
