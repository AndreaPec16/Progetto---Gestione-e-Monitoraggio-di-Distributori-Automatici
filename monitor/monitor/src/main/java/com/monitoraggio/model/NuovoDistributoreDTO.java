package com.monitoraggio.model;

public class NuovoDistributoreDTO {
    private String codice;
    private double lat;
    private double lon;

    public String getCodice() { return codice; }
    public void setCodice(String codice) { this.codice = codice; }
    public double getLat() { return lat; }
    public void setLat(double lat) { this.lat = lat; }
    public double getLon() { return lon; }
    public void setLon(double lon) { this.lon = lon; }
}
