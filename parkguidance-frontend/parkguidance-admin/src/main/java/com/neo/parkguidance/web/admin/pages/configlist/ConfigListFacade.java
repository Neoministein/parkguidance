package com.neo.parkguidance.web.admin.pages.configlist;

import com.neo.parkguidance.core.api.config.ConfigService;
import com.neo.parkguidance.core.entity.Configuration;
import com.neo.parkguidance.web.impl.pages.lazy.AbstractLazyFacade;
import com.neo.parkguidance.web.impl.table.Filter;

import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 * The screen facade for the ConfigList screen
 */
@Stateless
public class ConfigListFacade extends AbstractLazyFacade<Configuration> {

    @Inject
    ConfigService configService;

    public Filter<Configuration> newFilter() {
        return new Filter<>(new Configuration());
    }

    public void reload() {
        configService.reload();
    }
}
