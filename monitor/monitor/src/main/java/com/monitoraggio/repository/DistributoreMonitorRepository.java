package com.monitoraggio.repository;

import com.monitoraggio.model.DistributoreMonitor;

import java.sql.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DistributoreMonitorRepository {

    private static final String URL = "jdbc:mysql://localhost:3306/monitoraggio_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Driver MySQL non trovato", e);
        }
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public Optional<DistributoreMonitor> findByCodice(String codice) {
        String sql = "SELECT * FROM stato_macchine WHERE codice_visivo = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, codice);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Optional.of(mapRowToDistributore(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public List<DistributoreMonitor> findAll() {
        List<DistributoreMonitor> distributori = new ArrayList<>();
        String sql = "SELECT * FROM stato_macchine";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                distributori.add(mapRowToDistributore(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return distributori;
    }

    public void save(DistributoreMonitor distributore) {
        String sql = "INSERT INTO stato_macchine (codice_visivo, stato, ultimo_heartbeat, latitudine, longitudine) " +
                "VALUES (?, ?, ?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE " +
                "stato = ?, ultimo_heartbeat = ?, latitudine = ?, longitudine = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            long timestamp = distributore.getUltimoHeartbeat().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
            ps.setString(1, distributore.getCodice());
            ps.setString(2, distributore.getStato());
            ps.setLong(3, timestamp);
            ps.setDouble(4, distributore.getLatitudine());
            ps.setDouble(5, distributore.getLongitudine());
            ps.setString(6, distributore.getStato());
            ps.setLong(7, timestamp);
            ps.setDouble(8, distributore.getLatitudine());
            ps.setDouble(9, distributore.getLongitudine());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void updateStato(String codice, String nuovoStato) {
        String sql = "UPDATE stato_macchine SET stato = ? WHERE codice_visivo = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nuovoStato);
            ps.setString(2, codice);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int deleteByCodice(String codice) {
        String sql = "DELETE FROM stato_macchine WHERE codice_visivo = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, codice);
            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    private DistributoreMonitor mapRowToDistributore(ResultSet rs) throws SQLException {
        long timestampMillis = rs.getLong("ultimo_heartbeat");
        LocalDateTime ultimoHeartbeat = LocalDateTime.ofInstant(Instant.ofEpochMilli(timestampMillis), ZoneId.systemDefault());
        return new DistributoreMonitor(
                rs.getString("codice_visivo"),
                rs.getString("stato"),
                rs.getDouble("latitudine"),
                rs.getDouble("longitudine"),
                ultimoHeartbeat
        );
    }
}
