package com.monitoraggio.controller;

import com.monitoraggio.model.DistributoreStatusDTO;
import com.monitoraggio.service.MonitorService;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Gestisce la visualizzazione della dashboard principale.
 * Recupera i dati dal service e li inoltra alla pagina JSP.
 */
@WebServlet(name = "DashboardServlet", urlPatterns = {"/dashboard", ""})
public class DashboardServlet extends HttpServlet {

    private final MonitorService monitorService = new MonitorService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<DistributoreStatusDTO> distributori = monitorService.getAllDistributors();
        request.setAttribute("distributori", distributori);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/dashboard.jsp");
        dispatcher.forward(request, response);
    }
}
