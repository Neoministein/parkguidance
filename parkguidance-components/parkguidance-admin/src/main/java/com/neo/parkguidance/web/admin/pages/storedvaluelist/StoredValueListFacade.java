package com.neo.parkguidance.web.admin.pages.storedvaluelist;

import com.neo.parkguidance.core.api.config.ConfigService;
import com.neo.parkguidance.core.entity.ConfigValue;
import com.neo.parkguidance.web.impl.pages.lazy.AbstractLazyFacade;
import com.neo.parkguidance.web.impl.table.Filter;

import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 * The screen facade for the StoredValueList screen
 */
@Stateless
public class StoredValueListFacade extends AbstractLazyFacade<ConfigValue> {

    @Inject ConfigService configService;

    public Filter<ConfigValue> newFilter() {
        return new Filter<>(new ConfigValue());
    }

    public void reload() {
        configService.reload();
    }
}
