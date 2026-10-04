package com.example.demo.dto;

import java.math.BigDecimal;

public class ProdottoDTO {
    private String nome;
    private BigDecimal prezzo;

    public ProdottoDTO(String nome, BigDecimal prezzo) {
        this.nome = nome;
        this.prezzo = prezzo;
    }

    public String getNome() {
        return nome;
    }

    public BigDecimal getPrezzo() {
        return prezzo;
    }
}
