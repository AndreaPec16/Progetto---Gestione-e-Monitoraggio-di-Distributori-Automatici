package com.monitoraggio.model;

import java.time.LocalDateTime;

public class DistributoreMonitor {

    private String codice;
    private String stato;
    private double latitudine;
    private double longitudine;
    private LocalDateTime ultimoHeartbeat;

    public DistributoreMonitor() {
    }

    public DistributoreMonitor(String codice, String stato, double latitudine, double longitudine, LocalDateTime ultimoHeartbeat) {
        this.codice = codice;
        this.stato = stato;
        this.latitudine = latitudine;
        this.longitudine = longitudine;
        this.ultimoHeartbeat = ultimoHeartbeat;
    }

    // Getters and Setters

    public String getCodice() {
        return codice;
    }

    public void setCodice(String codice) {
        this.codice = codice;
    }

    public String getStato() {
        return stato;
    }

    public void setStato(String stato) {
        this.stato = stato;
    }

    public double getLatitudine() {
        return latitudine;
    }

    public void setLatitudine(double latitudine) {
        this.latitudine = latitudine;
    }

    public double getLongitudine() {
        return longitudine;
    }

    public void setLongitudine(double longitudine) {
        this.longitudine = longitudine;
    }

    public LocalDateTime getUltimoHeartbeat() {
        return ultimoHeartbeat;
    }

    public void setUltimoHeartbeat(LocalDateTime ultimoHeartbeat) {
        this.ultimoHeartbeat = ultimoHeartbeat;
    }
}
