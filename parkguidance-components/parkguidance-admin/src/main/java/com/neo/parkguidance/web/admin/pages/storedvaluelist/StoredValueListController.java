package com.neo.parkguidance.web.admin.pages.storedvaluelist;

import com.neo.parkguidance.core.entity.StoredValue;
import com.neo.parkguidance.web.impl.pages.lazy.AbstractLazyController;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * The controller for the StoredValueList screen
 */
@RequestScoped
@Named(StoredValueListController.BEAN_NAME)
public class StoredValueListController extends AbstractLazyController<StoredValue> {

    public static final String BEAN_NAME = "storedValueList";

    @Inject
    StoredValueListModel mode;

    @Inject
    StoredValueListFacade facade;

    @Override
    @PostConstruct
    public void init() {
        super.init();
    }

    @Override
    public StoredValueListModel getModel() {
        return mode;
    }

    @Override
    protected StoredValueListFacade getFacade() {
        return facade;
    }
}
