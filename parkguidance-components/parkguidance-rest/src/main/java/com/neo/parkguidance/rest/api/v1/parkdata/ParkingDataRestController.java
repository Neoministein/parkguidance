package com.neo.parkguidance.rest.api.v1.parkdata;

import com.neo.parkguidance.framework.entity.ParkingGarage;
import com.neo.parkguidance.framework.impl.utils.MathUtils;
import com.neo.parkguidance.framework.impl.utils.StringUtils;
import com.neo.parkguidance.rest.api.InternalRestException;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Path("v1/parkdata")
@Consumes({"application/json"})
@Produces({"application/json"})
@Stateless
public class ParkingDataRestController {

     @Inject
     ParkingDataRestFacade facade;

    @GET
    public Response get(
            @QueryParam("key") String key,
            @QueryParam("halfHourOfDay") Integer halfHourOfDay,
            @QueryParam("hourOfDay") Integer hourOfDay) {
        try {
            if (StringUtils.isEmpty(key)) {
                throw new InternalRestException(HttpServletResponse.SC_BAD_REQUEST, "No key specified");
            }

            if (MathUtils.isInBounds(halfHourOfDay,0 ,47)) {
                return Response.ok().entity(facade.getSingleData(key,halfHourOfDay).toString()).build();
            }

            if (MathUtils.isInBounds(hourOfDay, 0,23)) {
                return Response.ok().entity(facade.getSingleData(key,halfHourOfDay * 2).toString()).build();
            }

            throw new InternalRestException(HttpServletResponse.SC_BAD_REQUEST, "No time of day specified");
        } catch (InternalRestException ex) {
            return Response.status(ex.getResponseStatus()).entity(ex.getMessage()).build();
        } catch (Exception ex) {
            return Response.status(HttpServletResponse.SC_INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    @PUT
    public Response put(
            @QueryParam("accessKey") String accessKey,
            @QueryParam("token") String token,
            @QueryParam("key") String key,
            @QueryParam("type") String type,
            @QueryParam("count") Integer count) {
        try {
            if (!StringUtils.isEmpty(accessKey)) {
                ParkingGarage parkingGarage = facade.getAccessKey(accessKey);
                facade.updateParkingData(parkingGarage, type, count);
                return Response.ok().build();
            }
            if (!StringUtils.isEmpty(token)) {
                if (!StringUtils.isEmpty(key)) {
                    throw new InternalRestException(HttpServletResponse.SC_BAD_REQUEST, "No key specified");
                }
                ParkingGarage parkingGarage = facade.getKey(key, token);
                facade.updateParkingData(parkingGarage, type, count);
                return Response.ok().build();
            }

            throw new InternalRestException(HttpServletResponse.SC_BAD_REQUEST, "No authentication specified");
        } catch (InternalRestException ex) {
            return Response.status(ex.getResponseStatus()).entity(ex.getMessage()).build();
        } catch (Exception ex) {
            return Response.status(HttpServletResponse.SC_INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }
}
