package com.neo.parkguidance.web.admin.validation;

import com.neo.parkguidance.framework.entity.ConfigValue;
import com.neo.parkguidance.framework.impl.validation.ConfigurationValidator;
import com.neo.parkguidance.framework.impl.validation.EntityValidationException;
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
 * This class is a JSF {@link Validator} which checks if the {@link ConfigValue} key is unique
 */
@Stateless
@FacesValidator(value = ConfigValueKeyValidator.BEAN_NAME, managed = true)
public class ConfigValueKeyValidator implements Validator<String>, Serializable {

    public static final String BEAN_NAME = "configValueKeyValidator";

    @Inject
    ConfigurationValidator entityValidation;

    @Override
    public void validate(FacesContext facesContext, UIComponent uiComponent, String o) {
        try {
            entityValidation.checkInvalidCharsInKey(o);
        } catch (EntityValidationException ex) {
            FacesMessage msg = Messages.createError(ex.getMessage());
            facesContext.addMessage(uiComponent.getClientId(facesContext), msg);
            throw new ValidatorException(msg);
        }
    }
}