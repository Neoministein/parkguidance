package com.neo.parkguidance.web.user.pages.search;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * The controller for the search screen
 */
@RequestScoped
@Named(value = SearchController.BEAN_NAME)
public class SearchController {
    public static final String BEAN_NAME = "search";

    @Inject
    SearchFacade facade;

    @Inject
    SearchModel model;

    @PostConstruct
    public void init() {
        if (!model.isInstantiated()) {
            model.setFilter(facade.newFilter());
            model.setLazyDataModel(
                    facade.initDataModel(model.getFilter()));

            model.setInstantiated(true);
        }
    }

    public SearchModel getModel() {
        return model;
    }
}
