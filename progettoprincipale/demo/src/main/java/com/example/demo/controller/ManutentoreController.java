package com.example.demo.controller;

import com.example.demo.service.ManutentoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller per la gestione dei manutentori.
 */
@RestController
@RequestMapping("/api/manutentori")
public class ManutentoreController {

    @Autowired
    private ManutentoreService manutentoreService;

    /**
     * Endpoint API per aggiungere un nuovo manutentore.
     * @param nome Il nome del manutentore.
     * @param email L'email del manutentore.
     * @param username L'username per il login.
     * @param password La password per il login.
     * @return Messaggio di risultato.
     */
    @PostMapping("/aggiungi")
    public String aggiungiManutentore(@RequestParam String nome, 
                                      @RequestParam String email,
                                      @RequestParam String username,
                                      @RequestParam String password) {
        return manutentoreService.aggiungiManutentore(nome, email, username, password);
    }

    /**
     * Endpoint API per eliminare un manutentore.
     * @param id L'ID del manutentore da eliminare.
     * @return Messaggio di risultato.
     */
    @DeleteMapping("/{id}/elimina")
    public String eliminaManutentore(@PathVariable Integer id) {
        return manutentoreService.eliminaManutentore(id);
    }

    /**
     * Endpoint API per ottenere la lista dei manutentori in formato XML.
     * @return ResponseEntity con il contenuto XML.
     */
    @GetMapping(value = "/xml", produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> getManutentoriXml() {
        String xmlContent = manutentoreService.getManutentoriXml();
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_XML).body(xmlContent);
    }
}
