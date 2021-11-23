package com.neo.parkguidance.microservices.impl.rest.v1.parkgarage;

import com.neo.parkguidance.framework.api.dao.EntityDao;
import com.neo.parkguidance.framework.entity.ParkingGarage;
import com.neo.parkguidance.microservices.api.v1.RestAction;
import com.neo.parkguidance.microservices.impl.rest.v1.AbstractResource;
import com.neo.parkguidance.microservices.impl.rest.v1.DefaultV1Response;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@RequestScoped
@Path(ParkingGarageResource.CONTEXT)
public class ParkingGarageResource extends AbstractResource {

    public static final String CONTEXT = "api/v1/garage";

    @Inject
    EntityDao<ParkingGarage> parkingGarageDao;

    @GET
    @Path("/occupied")
    public Response getOccupied() {
        RestAction restAction = () -> {
            JSONObject data = new JSONObject();
            for (ParkingGarage parkingGarage: parkingGarageDao.findAll()) {
                data.put(parkingGarage.getKey(), parkingGarage.getOccupied());
            }
            return Response.ok().entity(DefaultV1Response.success(new JSONArray().put(data), CONTEXT).toString()).build();
        };
        return super.restCall(restAction);
    }

    @Override
    protected String getContext() {
        return CONTEXT;
    }
}
