package com.neo.parkguidance.web.admin.pages.storedvaluelist;

import com.neo.parkguidance.core.entity.StoredValue;
import com.neo.parkguidance.web.impl.pages.lazy.AbstractLazyFacade;
import com.neo.parkguidance.web.impl.table.Filter;

import javax.ejb.Stateless;

/**
 * The screen facade for the StoredValueList screen
 */
@Stateless
public class StoredValueListFacade extends AbstractLazyFacade<StoredValue> {

    public Filter<StoredValue> newFilter() {
        return new Filter<>(new StoredValue());
    }
}
