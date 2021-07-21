package com.neo.parkguidance.parkdata.receiver.api.servlet;

import com.neo.parkguidance.parkdata.receiver.impl.ParkingServiceFacade;

import javax.inject.Inject;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

/**
 * This class is a REST endpoint for updating {@link com.neo.parkguidance.core.entity.ParkingGarage} and its data
 */
public class ParkingService extends HttpServlet {

    @Inject
    private ParkingServiceFacade facade;

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) {
        StringBuilder buffer = new StringBuilder();
        try {
            BufferedReader br = req.getReader();
            String line;
            while ((line = br.readLine()) != null){
                buffer.append(line);
            }
            resp.setStatus(facade.updateParkingData(buffer.toString()));
        }catch (IOException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}
