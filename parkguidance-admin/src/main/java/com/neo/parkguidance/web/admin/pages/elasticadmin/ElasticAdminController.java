package com.neo.parkguidance.web.admin.pages.elasticadmin;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

@RequestScoped
@Named(ElasticAdminController.BEAN_NAME)
public class ElasticAdminController {

    public static final String BEAN_NAME = "elasticAdmin";

    @Inject
    ElasticAdminModel model;

    @Inject
    ElasticAdminFacade facade;

    @PostConstruct
    public void init() {
        if(!model.isInitialized()) {
            facade.updateClusterData(model);
            model.setInitialized(true);
        }
    }

    public String getStatusStyleClass() {
        return  facade.statusStyleClass(model.getStatus());
    }

    public ElasticAdminModel getModel() {
        return model;
    }
}
