package com.example.demo.dto;

public class UtenteDTO {

    private String username;
    private String nome;
    private String email;
    private Double credito;
    private String connectedMachineId;

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public Double getCredito() { return credito; }
    public void setCredito(Double credito) { this.credito = credito; }
    public String getConnectedMachineId() { return connectedMachineId; }
    public void setConnectedMachineId(String connectedMachineId) { this.connectedMachineId = connectedMachineId; }
}
