package com.example.demo.controller;

import com.example.demo.dto.UtenteConnessoDTO;
import com.example.demo.service.DistributoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * Controller per la gestione delle interazioni con i distributori.
 */
@Controller
public class DistributoreController {

    @Autowired
    private DistributoreService distributoreService;

    /**
     * Restituisce la home page del gestore.
     * @return La vista homeG.
     */
    @GetMapping("/Gestore/home")
    public String homeGestore() {
        return "homeG";
    }

    /**
     * Restituisce la home page della sezione manutenzione.
     * @param authentication L'oggetto Authentication per recuperare l'utente loggato.
     * @param model Il modello a cui aggiungere attributi.
     * @return La vista home_ad.
     */
    @GetMapping("/manutenzione")
    public String homeManutenzione(Authentication authentication, Model model) {
        model.addAttribute("username", authentication.getName());
        return "home_ad";
    }

    /**
     * Mostra la pagina di una specifica macchinetta, con informazioni sul distributore e sull'utente connesso.
     * @param codiceVisivo Il codice visivo della macchinetta.
     * @param model Il modello a cui aggiungere gli attributi per la vista.
     * @return La vista homeM.
     */
    @GetMapping("/macchinetta/{codiceVisivo}")
    public String getMacchinettaPage(@PathVariable String codiceVisivo, Model model) {
        model.addAttribute("distributore", distributoreService.findByCodiceVisivo(codiceVisivo));
        UtenteConnessoDTO utente = distributoreService.getUtenteConnesso(codiceVisivo);
        model.addAttribute("utente", utente);
        return "homeM";
    }

    /**
     * Endpoint API per ottenere la lista dei distributori in formato XML.
     * @return ResponseEntity con il contenuto XML.
     */
    @GetMapping(value = "/api/distributori/xml", produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> getDistributoriXml() {
        String xmlContent = distributoreService.getDistributoriXml();
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_XML).body(xmlContent);
    }

    /**
     * Endpoint API per l'acquisto di un prodotto.
     * @param codiceVisivo Codice della macchinetta.
     * @param username Username dell'utente.
     * @param prodotto Nome del prodotto.
     * @param zucchero Livello di zucchero.
     * @return ResponseEntity con il risultato dell'acquisto.
     */
    @PostMapping("/api/distributori/{codiceVisivo}/acquista")
    @ResponseBody
    public ResponseEntity<String> acquistaProdotto(@PathVariable String codiceVisivo, @RequestParam String username, @RequestParam String prodotto, @RequestParam int zucchero) {
        return distributoreService.acquistaProdotto(codiceVisivo, username, prodotto, zucchero);
    }
    
    /**
     * Endpoint API per cambiare lo stato di una macchinetta.
     * @param codiceVisivo Codice della macchinetta.
     * @param nuovoStato Nuovo stato da impostare.
     * @return Messaggio di risultato.
     */
    @PostMapping("/api/distributori/{codiceVisivo}/stato")
    @ResponseBody
    public String cambiaStatoMacchinetta(@PathVariable String codiceVisivo, @RequestParam String nuovoStato) {
        return distributoreService.cambiaStato(codiceVisivo, nuovoStato);
    }

    /**
     * Endpoint API per ripristinare le scorte di una macchinetta.
     * @param codiceVisivo Codice della macchinetta.
     * @return Messaggio di risultato.
     */
    @PostMapping("/api/distributori/{codiceVisivo}/ripristina")
    @ResponseBody
    public String ripristinaScorte(@PathVariable String codiceVisivo) {
        return distributoreService.ripristinaScorte(codiceVisivo);
    }

    /**
     * Endpoint API per aggiungere una nuova macchinetta.
     * @param codice Codice visivo.
     * @param indirizzo Indirizzo.
     * @param modello Modello.
     * @param lat Latitudine.
     * @param lon Longitudine.
     * @return Messaggio di risultato.
     */
    @PostMapping("/api/distributori/aggiungi")
    @ResponseBody
    public String aggiungiMacchinetta(@RequestParam String codice, @RequestParam String indirizzo, @RequestParam String modello, @RequestParam Double lat, @RequestParam Double lon) {
        return distributoreService.aggiungiDistributore(codice, indirizzo, modello, lat, lon);
    }

    /**
     * Endpoint API per eliminare una macchinetta.
     * @param codiceVisivo Codice della macchinetta da eliminare.
     * @return Messaggio di risultato.
     */
    @DeleteMapping("/api/distributori/{codiceVisivo}/elimina")
    @ResponseBody
    public String eliminaMacchinetta(@PathVariable String codiceVisivo) {
        return distributoreService.eliminaDistributore(codiceVisivo);
    }

    /**
     * Endpoint API per verificare se un utente è connesso a una macchinetta.
     * @param codiceVisivo Codice della macchinetta.
     * @return ResponseEntity con i dati dell'utente connesso o No Content.
     */
    @GetMapping("/api/distributori/{codiceVisivo}/check-utente")
    public ResponseEntity<UtenteConnessoDTO> checkUtenteConnesso(@PathVariable String codiceVisivo) {
        UtenteConnessoDTO utenteConnesso = distributoreService.getUtenteConnesso(codiceVisivo);
        if (utenteConnesso != null) {
            return ResponseEntity.ok(utenteConnesso);
        } else {
            return ResponseEntity.noContent().build();
        }
    }
}
