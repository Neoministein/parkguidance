package com.neo.parkguidance.web.admin.pages.storedvalueform;

import com.neo.parkguidance.core.entity.StoredValue;
import org.omnifaces.cdi.ViewScoped;

import java.io.Serializable;

/**
 * The screen model for the StoredValueForm screen
 */
@ViewScoped
public class StoredValueFormModel implements Serializable {

    private boolean initialized = false;
    private String key;
    private StoredValue item;
    private String hiddenValue;

    public boolean isInitialized() {
        return initialized;
    }

    public void setInitialized(boolean initialized) {
        this.initialized = initialized;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public StoredValue getItem() {
        return item;
    }

    public void setItem(StoredValue item) {
        this.item = item;
    }

    public String getHiddenValue() {
        return hiddenValue;
    }

    public void setHiddenValue(String hiddenValue) {
        this.hiddenValue = hiddenValue;
    }
}
