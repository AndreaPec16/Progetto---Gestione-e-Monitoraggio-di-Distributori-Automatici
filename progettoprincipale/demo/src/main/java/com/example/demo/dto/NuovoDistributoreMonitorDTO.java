package com.example.demo.dto;

public class NuovoDistributoreMonitorDTO {
    private String codice;
    private Double lat;
    private Double lon;

    public NuovoDistributoreMonitorDTO(String codice, Double lat, Double lon) {
        this.codice = codice;
        this.lat = lat;
        this.lon = lon;
    }

    public String getCodice() { return codice; }
    public Double getLat() { return lat; }
    public Double getLon() { return lon; }
}
