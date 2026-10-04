package com.example.demo.service;

import com.example.demo.dto.MonitorDistributoreDTO;
import com.example.demo.entity.Utente;
import com.example.demo.repository.DistributoreRepository;
import com.example.demo.repository.UtenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service per la gestione della connessione e disconnessione degli utenti alle macchinette.
 */
@Service
public class ConnectionService {

    @Autowired
    private UtenteRepository utenteRepository;

    @Autowired
    private DistributoreRepository distributoreRepository;

    @Autowired
    private RestTemplate restTemplate;

    private final String MONITOR_URL = "http://localhost:8081/monitor/api";

    /**
     * Connette un utente a una macchinetta, disconnettendo eventuali altri utenti già connessi.
     * @param username L'username dell'utente da connettere.
     * @param machineId L'ID della macchina a cui connettersi.
     * @return ResponseEntity con il risultato dell'operazione.
     */
    @Transactional
    public ResponseEntity<String> connectToMachine(String username, String machineId) {
        if (username == null) {
            return ResponseEntity.status(401).body("Utente non loggato.");
        }
        if (!distributoreRepository.findByCodiceVisivo(machineId).isPresent()) {
            return ResponseEntity.status(404).body("Macchinetta non trovata.");
        }

        try {
            Map<String, String> statiMonitor = getStatiFromMonitor();
            String statoCorrente = statiMonitor.getOrDefault(machineId, "SCONOSCIUTO");

            if (!"ATTIVO".equals(statoCorrente)) {
                return ResponseEntity.status(409).body("Impossibile connettersi: la macchinetta è " + statoCorrente.toLowerCase() + ".");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Errore di comunicazione con il servizio di monitoraggio.");
        }


        List<Utente> utentiGiaConnessi = utenteRepository.findByConnectedMachineId(machineId);
        for (Utente u : utentiGiaConnessi) {
            if (!u.getUsername().equals(username)) {
                u.setConnectedMachineId(null);
                utenteRepository.save(u);
            }
        }

        Utente utente = utenteRepository.findByUsername(username);
        if (utente != null) {
            utente.setConnectedMachineId(machineId);
            utenteRepository.save(utente);
            return ResponseEntity.ok("Connesso alla macchinetta " + machineId);
        }
        return ResponseEntity.status(404).body("Utente non trovato.");
    }

    /**
     * Disconnette un utente da qualsiasi macchinetta.
     * @param username L'username dell'utente da disconnettere.
     * @return ResponseEntity con il risultato dell'operazione.
     */
    public ResponseEntity<String> disconnectFromMachine(String username) {
        if (username == null) {
            return ResponseEntity.status(401).body("Utente non loggato.");
        }
        Utente utente = utenteRepository.findByUsername(username);
        if (utente != null) {
            utente.setConnectedMachineId(null);
            utenteRepository.save(utente);
            return ResponseEntity.ok("Disconnesso.");
        }
        return ResponseEntity.status(404).body("Utente non trovato.");
    }

    /**
     * Recupera gli stati di tutti i distributori dal servizio di monitoraggio.
     * @return Mappa [codiceVisivo -> stato].
     */
    private Map<String, String> getStatiFromMonitor() {
        ResponseEntity<MonitorDistributoreDTO[]> response = restTemplate.getForEntity(MONITOR_URL + "/distributori", MonitorDistributoreDTO[].class);
        if (response.getBody() != null) {
            return Arrays.stream(response.getBody())
                    .collect(Collectors.toMap(MonitorDistributoreDTO::getCodice, MonitorDistributoreDTO::getStato));
        }
        return Map.of();
    }
}
