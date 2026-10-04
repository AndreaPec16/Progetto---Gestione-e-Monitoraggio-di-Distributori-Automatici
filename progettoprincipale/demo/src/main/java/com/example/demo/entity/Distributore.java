package com.example.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "distributori")
public class Distributore {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "codice_visivo")
    private String codiceVisivo;
    private String indirizzo;
    private String modello;


    @Column(name = "livello_zucchero") private Integer livelloZucchero;
    @Column(name = "livello_caffe") private Integer livelloCaffe;
    @Column(name = "livello_bicchieri") private Integer livelloBicchieri;
    @Column(name = "livello_acqua") private Integer livelloAcqua;
    @Column(name = "livello_palette") private Integer livelloPalette;
    @Column(name = "livello_cioccolato") private Integer livelloCioccolato;
    @Column(name = "livello_latte") private Integer livelloLatte;
    @Column(name = "livello_te") private Integer livelloTe;
    @Column(name = "latitudine") private Double latitudine;
    @Column(name = "longitudine") private Double longitudine;

    @Column(name = "data_ultima_manutenzione")
    private LocalDateTime dataUltimaManutenzione;

    public LocalDateTime getDataUltimaManutenzione() { return dataUltimaManutenzione; }
    public void setDataUltimaManutenzione(LocalDateTime dataUltimaManutenzione) { this.dataUltimaManutenzione = dataUltimaManutenzione; }
    
    public Double getLatitudine() { return latitudine; }
    public Double getLongitudine() { return longitudine; }
    public Integer getId() { return id; }
    public String getCodiceVisivo() { return codiceVisivo; }
    public String getIndirizzo() { return indirizzo; }
    public String getModello() { return modello; }
    public Integer getLivelloZucchero() { return livelloZucchero; }
    public Integer getLivelloCaffe() { return livelloCaffe; }
    public Integer getLivelloBicchieri() { return livelloBicchieri; }
    public Integer getLivelloAcqua() { return livelloAcqua; }
    public Integer getLivelloPalette() { return livelloPalette; }
    public Integer getLivelloCioccolato() { return livelloCioccolato; }
    public Integer getLivelloLatte() { return livelloLatte; }
    public Integer getLivelloTe() { return livelloTe; }

    public void setCodiceVisivo(String codiceVisivo) { this.codiceVisivo = codiceVisivo; }
    public void setLongitudine(Double longitudine) { this.longitudine = longitudine; }
    public void setLatitudine(Double latitudine) { this.latitudine = latitudine; }
    public void setId(Integer id) { this.id = id; }
    public void setIndirizzo(String indirizzo) { this.indirizzo = indirizzo; }
    public void setLivelloAcqua(Integer livelloAcqua) { this.livelloAcqua = livelloAcqua; }
    public void setLivelloBicchieri(Integer livelloBicchieri) { this.livelloBicchieri = livelloBicchieri; }
    public void setLivelloCaffe(Integer livelloCaffe) { this.livelloCaffe = livelloCaffe; }
    public void setLivelloCioccolato(Integer livelloCioccolato) { this.livelloCioccolato = livelloCioccolato; }
    public void setLivelloLatte(Integer livelloLatte) { this.livelloLatte = livelloLatte; }
    public void setLivelloPalette(Integer livelloPalette) { this.livelloPalette = livelloPalette; }
    public void setLivelloTe(Integer livelloTe) { this.livelloTe = livelloTe; }
    public void setLivelloZucchero(Integer livelloZucchero) { this.livelloZucchero = livelloZucchero; }
    public void setModello(String modello) { this.modello = modello; }
}
