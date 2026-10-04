package com.example.demo.service;

import com.example.demo.dto.EntityMapper;
import com.example.demo.dto.ManutentoreDTO;
import com.example.demo.entity.Utente;
import com.example.demo.repository.UtenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder; // <-- IMPORT
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service per la gestione dei manutentori.
 */
@Service
public class ManutentoreService {

    @Autowired
    private UtenteRepository utenteRepository;

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Trova tutti gli utenti con ruolo MANUTENTORE e li converte in DTO.
     * @return Lista di ManutentoreDTO.
     */
    public List<ManutentoreDTO> findAllManutentoriAsDTO() {
        return utenteRepository.findAll().stream()
                .filter(u -> "MANUTENTORE".equals(u.getRuolo()))
                .map(EntityMapper::toManutentoreDTO)
                .collect(Collectors.toList());
    }

    /**
     * Genera un file XML con la lista dei manutentori.
     * @return Stringa contenente l'XML.
     */
    public String getManutentoriXml() {
        Context ctx = new Context();
        ctx.setVariable("manutentori", findAllManutentoriAsDTO());
        return templateEngine.process("xml/manutentori.xml", ctx).trim();
    }

    /**
     * Aggiunge un nuovo manutentore.
     * @param nome Il nome del manutentore.
     * @param email L'email del manutentore.
     * @param username L'username per il login.
     * @param password La password per il login.
     * @return Messaggio di successo o errore.
     */
    public String aggiungiManutentore(String nome, String email, String username, String password) {
        if (utenteRepository.findByUsername(username) != null) {
            return "ERRORE: Username già presente!";
        }
        Utente nuovoStaff = new Utente();
        nuovoStaff.setNome(nome);
        nuovoStaff.setEmail(email);
        nuovoStaff.setUsername(username);
        nuovoStaff.setPassword(passwordEncoder.encode(password));
        nuovoStaff.setRuolo("MANUTENTORE");
        nuovoStaff.setCredito(0.0);
        utenteRepository.save(nuovoStaff);
        return "OK: Manutentore aggiunto";
    }

    /**
     * Elimina un manutentore tramite il suo ID.
     * @param id L'ID del manutentore da eliminare.
     * @return Messaggio di successo o errore.
     */
    public String eliminaManutentore(Integer id) {
        if (utenteRepository.existsById(id)) {
            utenteRepository.deleteById(id);
            return "OK: Eliminato";
        }
        return "ERRORE: Utente non trovato";
    }
}
