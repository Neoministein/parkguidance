package com.neo.parkguidance.rest.api.v1.parkgrage;

import com.neo.parkguidance.core.entity.ParkingGarage;
import com.neo.parkguidance.rest.api.InternalRestException;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Path("v1/parkingGarage")
@Consumes({"application/json"})
@Produces({"application/json"})
@Stateless
public class ParkingGarageRestController {

    @Inject
    ParkingGarageRestFacade facade;

    @GET
    public Response get(
            @QueryParam("accessKey") String accessKey,
            @QueryParam("token") String token,
            @QueryParam("key") String key,
            @QueryParam("name") String name,
            @QueryParam("city") String city) {
        try {
            if (accessKey != null) {
                return Response.ok().entity(facade.parseParkingGarageToJSONObject(facade.getAccessKey(accessKey), true).toString()).build();
            }
            boolean authRequest = false;
            if (token != null) {
                facade.authorizedUser(token);
                authRequest = true;
            }

            if (key != null) {
                return Response.ok().entity(facade.parseParkingGarageToJSONObject(facade.getKey(key), authRequest).toString()).build();
            }
            if (name == null && city == null) {
                return Response.ok().entity(facade.parseParkingGarageToJSONArray(facade.getAll(), authRequest).toString())
                        .build();
            }

            ParkingGarage example = new ParkingGarage();
            example.setName(name);
            example.getAddress().setCityName(city);
            return Response.ok().entity(facade.parseParkingGarageToJSONArray(facade.getLike(example), authRequest).toString()).build();

        } catch (InternalRestException ex) {
            return Response.status(ex.getResponseStatus(), ex.getMessage()).build();
        } catch (Exception ex) {
            return Response.status(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ex.getMessage()).build();
        }
    }

    @POST
    public Response post(
            @QueryParam("accessKey") String accessKey,
            @QueryParam("token") String token,
            @QueryParam("key") String key,
            @QueryParam("name") String name,
            @QueryParam("spaces") Integer spaces,
            @QueryParam("price") String price,
            @QueryParam("operator") String operator,
            @QueryParam("description") String description,
            @QueryParam("cityName") String cityName,
            @QueryParam("plz") Integer plz,
            @QueryParam("street") String street,
            @QueryParam("number") Integer number,
            @QueryParam("revokeAccessKey") Boolean revokeAccessKey) {
        try {
            if (accessKey == null && token == null) {
                throw new InternalRestException(400, "Authentication required");
            }

            ParkingGarage parkingGarage;
            if (accessKey != null) {
                parkingGarage = facade.getAccessKey(accessKey);
            } else {
                parkingGarage = facade.getKey(token, key);
            }

            if (Boolean.TRUE.equals(revokeAccessKey)) {
                facade.revokeAccessKey(parkingGarage);
                return Response.ok().entity(facade.parseParkingGarageToJSONObject(parkingGarage, true).toString()).build();
            }

            boolean changed = false;
            if (name != null) {
                parkingGarage.setName(name);
                changed = true;
            }
            if (spaces != null) {
                parkingGarage.setSpaces(spaces);
                changed = true;
            }
            if (price != null) {
                parkingGarage.setPrice(price);
                changed = true;
            }
            if (operator != null) {
                parkingGarage.setOperator(operator);
                changed = true;
            }
            if (description != null) {
                parkingGarage.setDescription(description);
                changed = true;
            }
            if (cityName != null) {
                parkingGarage.getAddress().setCityName(cityName);
                changed = true;
            }
            if (plz != null) {
                parkingGarage.getAddress().setPlz(plz);
                changed = true;
            }
            if (street != null) {
                parkingGarage.getAddress().setStreet(street);
                changed = true;
            }
            if (number != null) {
                parkingGarage.getAddress().setNumber(number);
                changed = true;
            }

            if (!changed) {
                return Response.status(HttpServletResponse.SC_BAD_REQUEST, "No change parameters provided").build();
            }
            facade.updateGarage(parkingGarage);

            return Response.ok().entity(facade.parseParkingGarageToJSONObject(parkingGarage, true).toString()).build();
        } catch (InternalRestException ex) {
            return Response.status(ex.getResponseStatus(), ex.getMessage()).build();
        } catch (Exception ex) {
            return Response.status(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ex.getMessage()).build();
        }
    }

    @PUT
    public Response put(
            @QueryParam("token") String token,
            @QueryParam("key") String key,
            @QueryParam("name") String name,
            @QueryParam("spaces") Integer spaces,
            @QueryParam("price") String price,
            @QueryParam("operator") String operator,
            @QueryParam("description") String description,
            @QueryParam("cityName") String cityName,
            @QueryParam("plz") Integer plz,
            @QueryParam("street") String street,
            @QueryParam("number") Integer number) {
        try {
        if (token == null) {
            throw new InternalRestException(400, "Authentication required");
        }
            facade.authorizedUser(token);
            ParkingGarage parkingGarage = new ParkingGarage();

            parkingGarage.setKey(key);
            parkingGarage.setName(name);
            parkingGarage.setSpaces(spaces);
            parkingGarage.setPrice(price);
            parkingGarage.setOperator(operator);
            parkingGarage.setDescription(description);
            parkingGarage.getAddress().setCityName(cityName);
            parkingGarage.getAddress().setPlz(plz);
            parkingGarage.getAddress().setStreet(street);
            parkingGarage.getAddress().setNumber(number);

            facade.checkForMissingValues(parkingGarage);
            facade.createGarage(parkingGarage);

            return Response.ok().entity(facade.parseParkingGarageToJSONObject(parkingGarage, true)).build();
        } catch (InternalRestException ex) {
            return Response.status(ex.getResponseStatus(), ex.getMessage()).build();
        } catch (Exception ex) {
            return Response.status(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ex.getMessage()).build();
        }
    }

}