package com.neo.parkguidance.parkdata.sorter.api;

import com.neo.parkguidance.parkdata.sorter.impl.SortParkingDataImpl;

import javax.inject.Inject;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This class is a REST endpoint for sorting the ParkingData in ElasticSearch
 */
@WebServlet("/api/park-data/sorter")
public class DataSorterService extends HttpServlet {

    @Inject
    private SortParkingDataImpl dataSorter;

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) {
        dataSorter.sortParkingData();
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        resp.setStatus(HttpServletResponse.SC_OK);
    }
}
