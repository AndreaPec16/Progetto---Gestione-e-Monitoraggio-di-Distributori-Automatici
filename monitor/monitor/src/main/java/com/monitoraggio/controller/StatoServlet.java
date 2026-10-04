package com.monitoraggio.controller;

import com.monitoraggio.service.MonitorService;
import com.monitoraggio.service.exception.NotFoundException;
import com.monitoraggio.service.exception.ValidationException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Collectors;

/** Gestisce il cambio di stato di un distributore */
@WebServlet("/api/stato/*")
public class StatoServlet extends HttpServlet {

    private final MonitorService monitorService = new MonitorService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        addCorsHeaders(resp);
        String codice = getCodiceFromPath(req);
        String body = req.getReader().lines().collect(Collectors.joining());
        try {
            monitorService.changeStatusCommand(codice, body);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write("Stato di " + codice + " aggiornato con successo.");
        } catch (ValidationException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        } catch (NotFoundException e) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Errore interno durante l'aggiornamento dello stato.");
        }
    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        addCorsHeaders(resp);
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    private void addCorsHeaders(HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
    }

    private String getCodiceFromPath(HttpServletRequest req) {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            return null;
        }
        return pathInfo.substring(1);
    }
}
