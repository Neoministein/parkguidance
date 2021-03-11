package com.neo.parkguidance.api.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

public class BaseServlet extends HttpServlet {

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        StringBuilder buffer = new StringBuilder();

        BufferedReader br = req.getReader();
        String line;
        while ((line = br.readLine()) != null){
            buffer.append(line);
        }

        System.out.println(buffer.toString());
        resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
    }
}