package com.neo.parkguidance.web.admin.validation;

import com.neo.parkguidance.core.entity.StoredValue;
import com.neo.parkguidance.core.impl.dao.AbstractEntityDao;
import org.omnifaces.util.Messages;

import javax.ejb.Stateless;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import java.io.Serializable;

/**
 * This class is a JSF {@link Validator} which checks if the {@link StoredValue} key is unique
 */
@Stateless
@FacesValidator(value = UniqueStoredValueValidator.BEAN_NAME, managed = true)
public class UniqueStoredValueValidator implements Validator<String>, Serializable {

    public static final String BEAN_NAME = "uniqueStoredValueValidator";

    @Inject
    AbstractEntityDao<StoredValue> dao;

    @Override
    public void validate(FacesContext facesContext, UIComponent uiComponent, String o) {
        if (o.replaceAll("[a-zA-Z\\d\\-_.]","").length() > 0) {
            FacesMessage msg = Messages.createError("Unsupported Character, Valid Characters include a-Z, 0-9, '.', '_' and '-'.");
            facesContext.addMessage(uiComponent.getClientId(facesContext), msg);
            throw new ValidatorException(msg);
        }

        if (dao.find(o) != null) {
            FacesMessage msg = Messages.createError("Key already exists");
            facesContext.addMessage(uiComponent.getClientId(facesContext), msg);
            throw new ValidatorException(msg);
        }
    }
}