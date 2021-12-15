package com.neo.parkguidance.microservices.impl.rest.parkgarage;

import com.neo.parkguidance.framework.api.dao.EntityDao;
import com.neo.parkguidance.framework.api.wrapper.entity.JSONEntityWrapper;
import com.neo.parkguidance.framework.entity.ParkingGarage;
import com.neo.parkguidance.microservices.api.v1.RestAction;
import com.neo.parkguidance.microservices.impl.rest.AbstractEntityResource;
import com.neo.parkguidance.microservices.impl.rest.DefaultV1Response;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@RequestScoped
@Path(ParkingGarageResource.CONTEXT)
public class ParkingGarageResource extends AbstractEntityResource<ParkingGarage> {

    public static final String CONTEXT = "api/v1/garage";

    @Inject
    EntityDao<ParkingGarage> parkingGarageDao;

    @Inject
    JSONEntityWrapper<ParkingGarage> jsonEntityWrapper;

    @GET
    @Path("/occupied")
    public Response getOccupied() {
        RestAction restAction = () -> {
            JSONObject data = new JSONObject();
            for (ParkingGarage parkingGarage: parkingGarageDao.findAll()) {
                data.put(parkingGarage.getKey(), parkingGarage.getOccupied());
            }
            return DefaultV1Response.success(new JSONArray().put(data), CONTEXT);
        };
        return super.restCall(restAction, "/occupied");
    }

    @Override
    protected JSONEntityWrapper<ParkingGarage> getJSONEntityWrapper() {
        return jsonEntityWrapper;
    }

    @Override
    protected EntityDao<ParkingGarage> getEntityDao() {
        return parkingGarageDao;
    }

    @Override
    protected String getContext(String method) {
        return CONTEXT + method;
    }
}
