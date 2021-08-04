package com.neo.parkguidance.core.impl.event;

/**
 * This class is used to signal parkdata sort request or response
 */
public class ParkDataChangeEvent extends ChangeEvent {

    public static final String SORT_REQUEST = "sortRequest";
    public static final String SORTED_RESPONSE = "sortedResponse";

    private final String status;

    public ParkDataChangeEvent(String status) {
        super(ParkDataChangeEvent.class.getName());
        this.status = status;
    }

    public ParkDataChangeEvent(String type, String status) {
        super(type);
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
