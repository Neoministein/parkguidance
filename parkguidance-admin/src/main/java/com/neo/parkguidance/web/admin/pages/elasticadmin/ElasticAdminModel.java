package com.neo.parkguidance.web.admin.pages.elasticadmin;

import org.omnifaces.cdi.ViewScoped;

import java.io.Serializable;

@ViewScoped
public class ElasticAdminModel implements Serializable {

    private String status;
    private boolean timedOut;
    private int numberOfPendingTasks;
    private int taskMaxWaitingInQueueMillis;

    private boolean initialized;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isTimedOut() {
        return timedOut;
    }

    public void setTimedOut(boolean timedOut) {
        this.timedOut = timedOut;
    }

    public int getNumberOfPendingTasks() {
        return numberOfPendingTasks;
    }

    public void setNumberOfPendingTasks(int numberOfPendingTasks) {
        this.numberOfPendingTasks = numberOfPendingTasks;
    }

    public int getTaskMaxWaitingInQueueMillis() {
        return taskMaxWaitingInQueueMillis;
    }

    public void setTaskMaxWaitingInQueueMillis(int taskMaxWaitingInQueueMillis) {
        this.taskMaxWaitingInQueueMillis = taskMaxWaitingInQueueMillis;
    }

    public void setInitialized(boolean initialized) {
        this.initialized = initialized;
    }

    public boolean isInitialized() {
        return initialized;
    }
}
