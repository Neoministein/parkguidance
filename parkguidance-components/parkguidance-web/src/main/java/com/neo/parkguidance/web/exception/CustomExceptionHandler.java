package com.neo.parkguidance.web.exception;

import com.github.adminfaces.template.exception.BusinessException;
import com.github.adminfaces.template.util.Assert;
import com.github.adminfaces.template.util.WebXml;
import org.omnifaces.util.Exceptions;
import org.omnifaces.util.Messages;

import javax.ejb.EJBException;
import javax.faces.FacesException;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.context.FacesContext;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.PhaseId;
import javax.faces.validator.ValidatorException;
import javax.servlet.http.HttpServletRequest;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CustomExceptionHandler extends ExceptionHandlerWrapper {

    private static final Logger LOGGER = Logger.getLogger(CustomExceptionHandler.class.getName());

    private static final List<Class<? extends Throwable>> UNCHECK_EXCEPTION = Arrays.asList(ValidatorException.class);
    private ExceptionHandler wrapped;

    public CustomExceptionHandler(ExceptionHandler exceptionHandler) {
        this.wrapped = exceptionHandler;
    }

    @Override
    public ExceptionHandler getWrapped() {
        return this.wrapped;
    }

    @Override
    public void handle() throws FacesException {
        FacesContext context = FacesContext.getCurrentInstance();
        this.findErrorMessages(context);
        this.handleException(context);
        this.getWrapped().handle();
    }

    private void handleException(FacesContext context) {
        Iterator<ExceptionQueuedEvent> unhandledExceptionQueuedEvents = this.getUnhandledExceptionQueuedEvents().iterator();
        if (unhandledExceptionQueuedEvents.hasNext()) {
            Throwable exception = ((ExceptionQueuedEvent)unhandledExceptionQueuedEvents.next()).getContext().getException();
            unhandledExceptionQueuedEvents.remove();
            Throwable rootCause = Exceptions.unwrap(exception).getCause();
            if (UNCHECK_EXCEPTION.contains(rootCause.getClass())) {
                return;
            }
            if (rootCause instanceof BusinessException) {
                this.handleBusinessException(context, (BusinessException)rootCause);
                return;
            }

            this.goToErrorPage(context, rootCause);
        }

    }

    private void goToErrorPage(FacesContext context, Throwable e) {
        LOGGER.log(Level.WARNING, "", e);
        if (e instanceof FileNotFoundException) {
            throw new FacesException(e);
        } else {
            HttpServletRequest request = (HttpServletRequest)context.getExternalContext().getRequest();
            Map<String, Object> sessionMap = context.getExternalContext().getSessionMap();
            String referer = request.getHeader("Referer");
            sessionMap.put("userAgent", request.getHeader("user-agent"));
            sessionMap.put("requestedUri", Assert.has(referer) ? referer : request.getRequestURL());
            sessionMap.put("stacktrace", e);
            sessionMap.put("errorMessage", e != null ? e.getMessage() : "");
            sessionMap.put("exceptionType", e != null ? e.getClass().getName() : null);
            String userIp = request.getHeader("x-forwarded-for") != null ? request.getHeader("x-forwarded-for").split(",")[0] : request.getRemoteAddr();
            sessionMap.put("userIp", userIp);
            String errorPage = this.findErrorPage(e);
            if (!Assert.has(errorPage)) {
                String errorPageParam = context.getExternalContext().getInitParameter("com.github.adminfaces.ERROR_PAGE");
                if (!Assert.has(errorPageParam)) {
                    errorPage = "500.xhtml";
                }
            }

            try {
                context.getExternalContext().redirect(context.getExternalContext().getRequestContextPath() + errorPage);
            } catch (IOException var9) {
                LOGGER.log(Level.SEVERE, "Could not redirect user to error page: " + context.getExternalContext().getRequestContextPath() + errorPage, var9);
            }

        }
    }

    private String findErrorPage(Throwable exception) {
        if (exception instanceof EJBException && exception.getCause() != null) {
            exception = exception.getCause();
        }

        String errorPage = WebXml.INSTANCE.findErrorPageLocation(exception);
        return errorPage;
    }

    private void handleBusinessException(FacesContext context, BusinessException e) {
        if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
            throw new FacesException(e);
        } else {
            if (Assert.has(e.getExceptionList())) {
                Iterator var3 = e.getExceptionList().iterator();

                while(var3.hasNext()) {
                    BusinessException be = (BusinessException)var3.next();
                    this.addFacesMessage(be);
                }
            } else {
                this.addFacesMessage(e);
            }

            this.validationFailed(context);
            context.renderResponse();
        }
    }

    private void addFacesMessage(BusinessException be) {
        FacesMessage facesMessage = new FacesMessage();
        if (Assert.has(be.getSummary())) {
            facesMessage.setSummary(be.getSummary());
        }

        if (Assert.has(be.getDetail())) {
            facesMessage.setDetail(be.getDetail());
        }

        if (Assert.has(be.getSeverity())) {
            facesMessage.setSeverity(be.getSeverity());
        } else {
            facesMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
        }

        Messages.add(be.getFieldId(), facesMessage);
    }

    private void validationFailed(FacesContext context) {
        Map<Object, Object> callbackParams = (Map)context.getAttributes().get("CALLBACK_PARAMS");
        if (callbackParams == null) {
            callbackParams = new HashMap();
            ((Map)callbackParams).put("CALLBACK_PARAMS", callbackParams);
        }

        ((Map)callbackParams).put("validationFailed", true);
    }

    private void findErrorMessages(FacesContext context) {
        if (!context.getMessageList().isEmpty() && !context.isValidationFailed()) {
            Iterator var2 = context.getMessageList().iterator();

            while(var2.hasNext()) {
                FacesMessage msg = (FacesMessage)var2.next();
                if (msg.getSeverity().equals(FacesMessage.SEVERITY_ERROR) || msg.getSeverity().equals(FacesMessage.SEVERITY_FATAL)) {
                    this.validationFailed(context);
                    break;
                }
            }

        }
    }
}
