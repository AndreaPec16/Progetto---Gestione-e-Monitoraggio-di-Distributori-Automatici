package com.example.demo.service;

import com.example.demo.entity.Utente;
import com.example.demo.repository.UtenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service per la gestione degli utenti.
 */
@Service
public class UtenteService {

    @Autowired
    private UtenteRepository utenteRepository;

    /**
     * Trova un utente tramite il suo username.
     * @param username L'username da cercare.
     * @return L'utente trovato, o null se non esiste.
     */
    public Utente findByUsername(String username) {
        return utenteRepository.findByUsername(username);
    }
    
    /**
     * Ricarica il credito di un utente.
     * @param username L'username dell'utente.
     * @param importo L'importo da ricaricare.
     * @return Messaggio di successo o errore.
     */
    public String ricaricaCredito(String username, double importo) {
        if (username == null) {
            return "ERRORE: Non sei loggato! Effettua il login.";
        }
        Utente u = findByUsername(username);
        if (u == null) {
            return "ERRORE: Utente non trovato nel database.";
        }
        if (importo <= 0) {
            return "ERRORE: L'importo deve essere positivo.";
        }
        double nuovoSaldo = u.getCredito() + importo;
        u.setCredito(nuovoSaldo);
        utenteRepository.save(u);
        return "OK: Ricarica effettuata. Nuovo saldo: " + String.format("%.2f", nuovoSaldo);
    }
}
