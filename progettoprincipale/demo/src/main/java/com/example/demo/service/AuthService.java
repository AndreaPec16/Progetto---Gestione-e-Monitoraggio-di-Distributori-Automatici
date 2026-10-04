package com.example.demo.service;

import com.example.demo.dto.RegistrationDTO;
import com.example.demo.entity.Utente;
import com.example.demo.repository.UtenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Service per la gestione dell'autenticazione e registrazione.
 */
@Service
public class AuthService {

    @Autowired
    private UtenteRepository utenteRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Registra un nuovo utente nel sistema.
     * @param registrationDTO I dati per la registrazione.
     * @return ResponseEntity con il risultato dell'operazione.
     */
    public ResponseEntity<String> registerUser(RegistrationDTO registrationDTO) {
        if (utenteRepository.findByUsername(registrationDTO.getUsername()) != null) {
            return ResponseEntity.badRequest().body("Username già in uso.");
        }

        Utente nuovoUtente = new Utente();
        nuovoUtente.setUsername(registrationDTO.getUsername());
        nuovoUtente.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));
        nuovoUtente.setEmail(registrationDTO.getEmail());
        nuovoUtente.setNome(registrationDTO.getNome());
        nuovoUtente.setRuolo("CLIENTE");
        nuovoUtente.setCredito(0.0);

        utenteRepository.save(nuovoUtente);

        return ResponseEntity.ok("OK");
    }
}
