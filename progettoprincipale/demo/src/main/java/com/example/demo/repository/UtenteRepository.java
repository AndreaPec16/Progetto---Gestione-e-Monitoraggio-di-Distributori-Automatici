package com.example.demo.repository;

import com.example.demo.entity.Utente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UtenteRepository extends JpaRepository<Utente, Integer> {
    Utente findByUsername(String username);

    List<Utente> findByConnectedMachineId(String connectedMachineId);
}
