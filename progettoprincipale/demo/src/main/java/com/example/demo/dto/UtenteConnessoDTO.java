package com.example.demo.dto;

public class UtenteConnessoDTO {
    private String username;
    private String nome;
    private Double credito;
    private String connectedMachineId;

    public UtenteConnessoDTO(String username, String nome, Double credito, String connectedMachineId) {
        this.username = username;
        this.nome = nome;
        this.credito = credito;
        this.connectedMachineId = connectedMachineId;
    }

    public String getUsername() { return username; }
    public String getNome() { return nome; }
    public Double getCredito() { return credito; }
    public String getConnectedMachineId() { return connectedMachineId; }
}
