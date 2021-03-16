package com.neo.parkguidance.parkdata.receiver.api.servlet;

import com.neo.parkguidance.parkdata.receiver.impl.ParkingServiceFacade;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

public class ParkingService extends HttpServlet {

    @Inject
    private ParkingServiceFacade facade;

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        StringBuilder buffer = new StringBuilder();

        BufferedReader br = req.getReader();
        String line;
        while ((line = br.readLine()) != null){
            buffer.append(line);
        }
        resp.setStatus(facade.test(buffer.toString()));
    }
}
