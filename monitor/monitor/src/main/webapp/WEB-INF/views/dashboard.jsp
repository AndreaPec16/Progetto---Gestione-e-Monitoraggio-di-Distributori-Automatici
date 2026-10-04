<%@ page import="com.monitoraggio.model.DistributoreStatusDTO" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    // Questa pagina riceve i dati dalla DashboardServlet
    List<DistributoreStatusDTO> distributori = (List<DistributoreStatusDTO>) request.getAttribute("distributori");
%>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title>Dashboard Monitoraggio</title>
    <link rel="stylesheet" href="https://unpkg.com/leaflet@1.9.4/dist/leaflet.css"/>
    <script src="https://unpkg.com/leaflet@1.9.4/dist/leaflet.js"></script>
    <style>
        body { font-family: 'Segoe UI', sans-serif; background-color: #1a1a1a; color: #fff; margin: 0; padding: 20px; }
        h1 { text-align: center; color: #00d4ff; border-bottom: 2px solid #333; padding-bottom: 10px; }
        .container { display: flex; gap: 20px; height: 80vh; }
        .sidebar { flex: 1; background: #2a2a2a; padding: 15px; border-radius: 8px; overflow-y: auto; }
        .card { padding: 15px; margin-bottom: 10px; border-radius: 5px; border-left: 6px solid #555; background: #333; }
        .card.ATTIVO { border-color: #00ff00; }
        .card.GUASTO { border-color: #ff0000; background: #3d0000; }
        .card.MANUTENZIONE { border-color: #ffcc00; }
        .map-wrapper { flex: 2; border: 2px solid #444; border-radius: 8px; }
        #map { width: 100%; height: 100%; }
    </style>
</head>
<body>

<h1>MONITOR</h1>

<div class="container">
    <div class="sidebar">
        <h3>Stato Distriutori (<%= distributori != null ? distributori.size() : 0 %>)</h3>
        <% if (distributori != null) {
            for (DistributoreStatusDTO d : distributori) { %>
            <div class="card <%= d.getStato() %>">
                <h3 style="margin:0"><%= d.getCodice() %></h3>
                <p style="margin:5px 0">Stato: <strong><%= d.getStato() %></strong></p>
                <small>Ultimo segnale: <%= d.getLastSeen() %> sec fa</small>
            </div>
        <%  }
           } %>
    </div>

    <div class="map-wrapper">
        <div id="map"></div>
    </div>
</div>

<script>
    var map = L.map('map').setView([38.1157, 13.3615], 13);
    L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {
        attribution: '&copy; OpenStreetMap contributors'
    }).addTo(map);

    <% if (distributori != null) {
        for (DistributoreStatusDTO d : distributori) { %>
        L.marker([<%= d.getLat() %>, <%= d.getLon() %>])
            .addTo(map)
            .bindPopup("<b><%= d.getCodice() %></b><br>Stato: <%= d.getStato() %>");
    <%  }
       } %>
</script>

</body>
</html>
