package com.monitoraggio.model;

public class DistributoreStatusDTO {

    private String codice;
    private String stato;
    private double lat;
    private double lon;
    private long lastSeen; // Secondi dall'ultimo heartbeat

    public DistributoreStatusDTO(String codice, String stato, double lat, double lon, long lastSeen) {
        this.codice = codice;
        this.stato = stato;
        this.lat = lat;
        this.lon = lon;
        this.lastSeen = lastSeen;
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

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public long getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(long lastSeen) {
        this.lastSeen = lastSeen;
    }
}
