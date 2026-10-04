package com.monitoraggio.controller;

import com.monitoraggio.service.MonitorService;
import com.monitoraggio.service.exception.ValidationException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**Gestisce la ricezione dei segnali di heartbeat dai distributori.*/
@WebServlet("/api/heartbeat")
public class HeartbeatServlet extends HttpServlet {

    private final MonitorService monitorService = new MonitorService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        addCorsHeaders(resp);
        String codice = req.getParameter("codice");
        String latStr = req.getParameter("lat");
        String lonStr = req.getParameter("lon");
        try {
            monitorService.processHeartbeat(codice, latStr, lonStr);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write("Heartbeat ricevuto con successo per " + codice);
        } catch (ValidationException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        }
    }
    
    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        addCorsHeaders(resp);
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    private void addCorsHeaders(HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
    }
}
