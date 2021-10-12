package com.neo.parkguidance.web.admin.pages.elasticadmin;

import com.neo.parkguidance.elastic.impl.ElasticSearchProvider;
import org.json.JSONObject;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.io.IOException;

/**
 * The screen facade for the ElasticAdmin screen
 */
@Stateless
public class ElasticAdminFacade {

    @Inject
    ElasticSearchProvider elasticSearchProvider;

    public void updateClusterData(ElasticAdminModel model) {
        try {
            JSONObject response = new JSONObject(elasticSearchProvider.sendLowLevelRequest("GET","/_cluster/health/",null));
            model.setStatus(response.getString("status"));
            model.setTimedOut(response.getBoolean("timed_out"));
            model.setNumberOfPendingTasks(response.getInt("number_of_pending_tasks"));
            model.setTaskMaxWaitingInQueueMillis(response.getInt("number_of_in_flight_fetch"));
        }catch (IOException e) {
            model.setStatus("unavailable");
            model.setTimedOut(true);
            model.setNumberOfPendingTasks(-1);
            model.setTaskMaxWaitingInQueueMillis(-1);
        }
    }

    public void reconnect() {
        elasticSearchProvider.reconnect();
    }

    public String statusStyleClass(String status){
        switch (status) {
        case "red":
            return "btn-danger";
        case "yellow":
            return "btn-warning";
        case "green":
            return "btn-success";
        default:
            return "btn-info";
        }
    }
}
