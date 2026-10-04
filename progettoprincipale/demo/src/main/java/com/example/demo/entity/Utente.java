package com.example.demo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "utenti")
public class Utente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String username;
    private String password;
    private String email;
    private Double credito;

    @Column(name = "nome")
    private String nome;

    @Column(name = "ruolo")
    private String ruolo;

    @Column(name = "connected_machine_id")
    private String connectedMachineId;

    public Utente() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Double getCredito() { return credito; }
    public void setCredito(Double credito) { this.credito = credito; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getRuolo() { return ruolo; }
    public void setRuolo(String ruolo) { this.ruolo = ruolo; }

    public String getConnectedMachineId() { return connectedMachineId; }
    public void setConnectedMachineId(String connectedMachineId) { this.connectedMachineId = connectedMachineId; }
}
