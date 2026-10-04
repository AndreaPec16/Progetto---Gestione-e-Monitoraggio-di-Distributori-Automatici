package com.example.demo.dto;

public class DistributoreDTO {

    private String codiceVisivo;
    private String modello;
    private String indirizzo;
    private String stato;
    private Integer livelloAcqua;
    private Integer livelloZucchero;
    private Integer livelloCaffe;
    private Integer livelloTe;
    private Integer livelloLatte;
    private Integer livelloCioccolato;
    private Integer livelloBicchieri;
    private Integer livelloPalette;
    private Double latitudine;
    private Double longitudine;

    public String getCodiceVisivo() { return codiceVisivo; }
    public void setCodiceVisivo(String codiceVisivo) { this.codiceVisivo = codiceVisivo; }

    public String getModello() { return modello; }
    public void setModello(String modello) { this.modello = modello; }

    public String getIndirizzo() { return indirizzo; }
    public void setIndirizzo(String indirizzo) { this.indirizzo = indirizzo; }

    public String getStato() { return stato; }
    public void setStato(String stato) { this.stato = stato; }

    public Integer getLivelloAcqua() { return livelloAcqua; }
    public void setLivelloAcqua(Integer livelloAcqua) { this.livelloAcqua = livelloAcqua; }

    public Integer getLivelloZucchero() { return livelloZucchero; }
    public void setLivelloZucchero(Integer livelloZucchero) { this.livelloZucchero = livelloZucchero; }

    public Integer getLivelloCaffe() { return livelloCaffe; }
    public void setLivelloCaffe(Integer livelloCaffe) { this.livelloCaffe = livelloCaffe; }

    public Integer getLivelloTe() { return livelloTe; }
    public void setLivelloTe(Integer livelloTe) { this.livelloTe = livelloTe; }

    public Integer getLivelloLatte() { return livelloLatte; }
    public void setLivelloLatte(Integer livelloLatte) { this.livelloLatte = livelloLatte; }

    public Integer getLivelloCioccolato() { return livelloCioccolato; }
    public void setLivelloCioccolato(Integer livelloCioccolato) { this.livelloCioccolato = livelloCioccolato; }

    public Integer getLivelloBicchieri() { return livelloBicchieri; }
    public void setLivelloBicchieri(Integer livelloBicchieri) { this.livelloBicchieri = livelloBicchieri; }

    public Integer getLivelloPalette() { return livelloPalette; }
    public void setLivelloPalette(Integer livelloPalette) { this.livelloPalette = livelloPalette; }

    public Double getLatitudine() { return latitudine; }
    public void setLatitudine(Double latitudine) { this.latitudine = latitudine; }

    public Double getLongitudine() { return longitudine; }
    public void setLongitudine(Double longitudine) { this.longitudine = longitudine; }
}
