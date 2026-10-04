package com.example.demo.controller;

import com.example.demo.service.ConnectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller per la gestione delle connessioni alle macchinette.
 */
@RestController
public class ConnectionController {

    @Autowired
    private ConnectionService connectionService;

    /**
     * Endpoint API per connettere l'utente autenticato a una macchinetta.
     * @param machineId L'ID della macchina a cui connettersi.
     * @param authentication L'oggetto Authentication per recuperare l'utente.
     * @return ResponseEntity con il risultato dell'operazione.
     */
    @PostMapping("/api/connect")
    public ResponseEntity<String> connectToMachine(@RequestParam String machineId, Authentication authentication) {
        String username = authentication.getName();
        return connectionService.connectToMachine(username, machineId);
    }

    /**
     * Endpoint API per disconnettere l'utente autenticato.
     * @param authentication L'oggetto Authentication per recuperare l'utente.
     * @return ResponseEntity con il risultato dell'operazione.
     */
    @PostMapping("/api/disconnect")
    public ResponseEntity<String> disconnectFromMachine(Authentication authentication) {
        String username = authentication.getName();
        return connectionService.disconnectFromMachine(username);
    }
}
