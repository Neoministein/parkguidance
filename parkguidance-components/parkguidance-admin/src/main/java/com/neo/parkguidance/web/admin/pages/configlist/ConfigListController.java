package com.neo.parkguidance.web.admin.pages.configlist;

import com.neo.parkguidance.core.entity.Configuration;
import com.neo.parkguidance.web.impl.pages.lazy.AbstractLazyController;
import com.neo.parkguidance.web.impl.utils.Utils;
import org.primefaces.PrimeFaces;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * The controller for the ConfigList screen
 */
@RequestScoped
@Named(ConfigListController.BEAN_NAME)
public class ConfigListController extends AbstractLazyController<Configuration> {

    public static final String BEAN_NAME = "configList";

    @Inject ConfigListModel mode;

    @Inject ConfigListFacade facade;

    @Override
    @PostConstruct
    public void init() {
        super.init();
    }

    public void reload() {
        facade.reload();
        Utils.addDetailMessage("Stored values have been reloaded from the database");
        PrimeFaces.current().ajax().update("form");
    }

    @Override
    public ConfigListModel getModel() {
        return mode;
    }

    @Override
    protected ConfigListFacade getFacade() {
        return facade;
    }
}
