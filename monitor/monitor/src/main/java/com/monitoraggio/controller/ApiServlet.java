package com.monitoraggio.controller;

import com.google.gson.Gson;
import com.monitoraggio.model.DistributoreStatusDTO;
import com.monitoraggio.service.MonitorService;
import com.monitoraggio.service.exception.NotFoundException;
import com.monitoraggio.service.exception.ValidationException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/** Gestisce le operazioni CRUD sui distributori via API. */
@WebServlet("/api/distributori/*")
public class ApiServlet extends HttpServlet {

    private final MonitorService monitorService = new MonitorService();
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        addCorsHeaders(resp);
        try {
            List<DistributoreStatusDTO> distributori = monitorService.getAllDistributors();
            String jsonResponse = gson.toJson(distributori);
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            resp.getWriter().write(jsonResponse);
        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Errore interno durante il recupero dei dati.");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        addCorsHeaders(resp);
        String body = req.getReader().lines().collect(Collectors.joining());
        try {
            monitorService.registraNuovoDistributore(body);
            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.getWriter().write("Distributore creato con successo.");
        } catch (ValidationException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Errore interno durante la creazione.");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        addCorsHeaders(resp);
        String codice = getCodiceFromPath(req);
        try {
            monitorService.eliminaDistributore(codice);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write("Distributore " + codice + " eliminato con successo.");
        } catch (ValidationException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        } catch (NotFoundException e) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Errore interno durante l'eliminazione.");
        }
    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        addCorsHeaders(resp);
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    private void addCorsHeaders(HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, DELETE, OPTIONS");
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
