package com.neo.parkguidance.parkdata.sorter.api;

import com.neo.parkguidance.parkdata.sorter.impl.SortParkingDataImpl;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DataSorterService extends HttpServlet {

    @Inject
    private SortParkingDataImpl dataSorter;

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        dataSorter.sortParkingData();
    }
}
