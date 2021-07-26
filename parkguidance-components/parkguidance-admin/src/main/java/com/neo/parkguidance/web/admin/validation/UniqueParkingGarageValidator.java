package com.neo.parkguidance.web.admin.validation;

import com.neo.parkguidance.core.entity.ParkingGarage;
import com.neo.parkguidance.core.impl.dao.AbstractEntityDao;
import org.omnifaces.util.Messages;

import javax.ejb.Stateless;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.inject.Inject;
import java.io.Serializable;

/**
 * This class is a JSF {@link Validator} which checks if the {@link ParkingGarage} key is unique
 */
@Stateless
@FacesValidator(value = UniqueParkingGarageValidator.BEAN_NAME, managed = true)
public class UniqueParkingGarageValidator implements Validator<String>, Serializable {

    public static final String BEAN_NAME = "uniqueParkingGarageValidator";

    @Inject
    AbstractEntityDao<ParkingGarage> parkingGarageDao;

    @Override
    public void validate(FacesContext facesContext, UIComponent uiComponent, String o) {
        if (!parkingGarageDao.findByColumn(ParkingGarage.C_KEY, o).isEmpty()) {
            FacesMessage msg = Messages.createError("Key already exists");
            facesContext.addMessage(uiComponent.getClientId(facesContext), msg);
        }
    }
}