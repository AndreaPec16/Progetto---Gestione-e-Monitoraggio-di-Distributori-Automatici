package com.monitoraggio.service;

import com.google.gson.Gson;
import com.monitoraggio.model.DistributoreMonitor;
import com.monitoraggio.model.DistributoreStatusDTO;
import com.monitoraggio.model.NuovoDistributoreDTO;
import com.monitoraggio.model.StatoUpdateRequest;
import com.monitoraggio.repository.DistributoreMonitorRepository;
import com.monitoraggio.service.exception.NotFoundException;
import com.monitoraggio.service.exception.ValidationException;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class MonitorService {

    private final DistributoreMonitorRepository repository = new DistributoreMonitorRepository();
    private final Gson gson = new Gson();

    /** Registra un nuovo distributore nel sistema. */
    public void registraNuovoDistributore(String jsonBody) throws ValidationException {
        if (jsonBody == null || jsonBody.trim().isEmpty()) {
            throw new ValidationException("Il corpo della richiesta non può essere vuoto.");
        }
        NuovoDistributoreDTO dto = gson.fromJson(jsonBody, NuovoDistributoreDTO.class);

        if (dto == null || dto.getCodice() == null || dto.getCodice().trim().isEmpty()) {
            throw new ValidationException("I dati del nuovo distributore non sono validi o il codice è mancante.");
        }
        if (repository.findByCodice(dto.getCodice()).isPresent()) {
            throw new ValidationException("Distributore con codice " + dto.getCodice() + " esiste già.");
        }

        DistributoreMonitor nuovoDistributore = new DistributoreMonitor();
        nuovoDistributore.setCodice(dto.getCodice());
        nuovoDistributore.setLatitudine(dto.getLat());
        nuovoDistributore.setLongitudine(dto.getLon());
        nuovoDistributore.setStato("ATTIVO");
        nuovoDistributore.setUltimoHeartbeat(LocalDateTime.now());
        repository.save(nuovoDistributore);
    }

    /** Elimina un distributore dal sistema. */
    public void eliminaDistributore(String codice) throws NotFoundException, ValidationException {
        if (codice == null || codice.trim().isEmpty()) {
            throw new ValidationException("Il codice del distributore non può essere vuoto.");
        }
        int righeEliminate = repository.deleteByCodice(codice);
        if (righeEliminate == 0) {
            throw new NotFoundException("Nessun distributore trovato con codice " + codice + " da eliminare.");
        }
    }

    /** Processa un segnale di heartbeat, aggiornando o creando un distributore. */
    public void processHeartbeat(String codice, String latStr, String lonStr) throws ValidationException {
        if (codice == null || codice.trim().isEmpty()) {
            throw new ValidationException("Il parametro 'codice' è obbligatorio.");
        }
        double lat = 0.0;
        double lon = 0.0;
        try {
            if (latStr != null && !latStr.trim().isEmpty()) lat = Double.parseDouble(latStr);
            if (lonStr != null && !lonStr.trim().isEmpty()) lon = Double.parseDouble(lonStr);
        } catch (NumberFormatException e) {
            throw new ValidationException("Coordinate non valide.");
        }
        Optional<DistributoreMonitor> distributoreOpt = repository.findByCodice(codice);
        DistributoreMonitor distributore = distributoreOpt.orElse(new DistributoreMonitor());
        distributore.setCodice(codice);
        distributore.setLatitudine(lat);
        distributore.setLongitudine(lon);
        distributore.setUltimoHeartbeat(LocalDateTime.now());
        if (distributore.getStato() == null || "GUASTO".equals(distributore.getStato())) {
            distributore.setStato("ATTIVO");
        }
        repository.save(distributore);
    }

    /** Forza il cambio di stato di un distributore. */
    public void changeStatusCommand(String codice, String jsonBody) throws ValidationException, NotFoundException {
        if (codice == null || codice.trim().isEmpty()) {
            throw new ValidationException("Il codice del distributore non può essere vuoto.");
        }
        if (jsonBody == null || jsonBody.trim().isEmpty()) {
            throw new ValidationException("Il corpo della richiesta non può essere vuoto.");
        }
        StatoUpdateRequest request = gson.fromJson(jsonBody, StatoUpdateRequest.class);
        if (request == null || request.getStato() == null || request.getStato().trim().isEmpty()) {
            throw new ValidationException("Il nuovo stato non può essere vuoto.");
        }
        if (repository.findByCodice(codice).isEmpty()) {
            throw new NotFoundException("Nessun distributore trovato con codice " + codice);
        }
        repository.updateStato(codice, request.getStato());
    }

    /** Restituisce una lista di tutti i distributori con il loro stato attuale. */
    public List<DistributoreStatusDTO> getAllDistributors() {
        return repository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    /** Metodo schedulato per controllare i distributori inattivi e marcarli come guasti. */
    public void checkDistributorStatus() {
        List<DistributoreMonitor> distributori = repository.findAll();
        LocalDateTime now = LocalDateTime.now();
        for (DistributoreMonitor distributore : distributori) {
            if ("ATTIVO".equals(distributore.getStato())) {
                long minutesSinceLastHeartbeat = Duration.between(distributore.getUltimoHeartbeat(), now).toMinutes();
                if (minutesSinceLastHeartbeat >= 3) {
                    System.out.println(">>> ALERT: Distributore " + distributore.getCodice() + " è GUASTO.");
                    repository.updateStato(distributore.getCodice(), "GUASTO");
                }
            }
        }
    }

    /** Converte un'entità DistributoreMonitor in un DTO per l'esposizione via API. */
    private DistributoreStatusDTO mapToDTO(DistributoreMonitor distributore) {
        long lastSeenSeconds = 0;
        if (distributore.getUltimoHeartbeat() != null) {
            lastSeenSeconds = Duration.between(distributore.getUltimoHeartbeat(), LocalDateTime.now()).getSeconds();
        }
        return new DistributoreStatusDTO(
                distributore.getCodice(),
                distributore.getStato(),
                distributore.getLatitudine(),
                distributore.getLongitudine(),
                lastSeenSeconds
        );
    }
}
