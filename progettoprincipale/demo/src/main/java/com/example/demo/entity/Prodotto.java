package com.example.demo.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "prodotto")
public class Prodotto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String nome;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal prezzo;

    private int consumoCaffe;
    private int consumoLatte;
    private int consumoCioccolato;
    private int consumoTe;
    private int consumoAcqua;

    public Prodotto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public BigDecimal getPrezzo() {
        return prezzo;
    }

    public void setPrezzo(BigDecimal prezzo) {
        this.prezzo = prezzo;
    }

    public int getConsumoCaffe() {
        return consumoCaffe;
    }

    public void setConsumoCaffe(int consumoCaffe) {
        this.consumoCaffe = consumoCaffe;
    }

    public int getConsumoLatte() {
        return consumoLatte;
    }

    public void setConsumoLatte(int consumoLatte) {
        this.consumoLatte = consumoLatte;
    }

    public int getConsumoCioccolato() {
        return consumoCioccolato;
    }

    public void setConsumoCioccolato(int consumoCioccolato) {
        this.consumoCioccolato = consumoCioccolato;
    }

    public int getConsumoTe() {
        return consumoTe;
    }

    public void setConsumoTe(int consumoTe) {
        this.consumoTe = consumoTe;
    }

    public int getConsumoAcqua() {
        return consumoAcqua;
    }

    public void setConsumoAcqua(int consumoAcqua) {
        this.consumoAcqua = consumoAcqua;
    }
}
