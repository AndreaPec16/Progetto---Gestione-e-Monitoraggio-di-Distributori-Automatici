package com.example.demo.controller;

import com.example.demo.entity.Utente;
import com.example.demo.service.UtenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller per le operazioni relative all'utente cliente.
 */
@Controller
public class UtenteController {

    @Autowired
    private UtenteService utenteService;

    /**
     * Mostra la home page del cliente con i suoi dati.
     * @param authentication L'oggetto Authentication per recuperare l'utente.
     * @param model Il modello a cui aggiungere gli attributi per la vista.
     * @return La vista "homeC".
     */
    @GetMapping("/Cliente/home")
    public String homeCliente(Authentication authentication, Model model) {
        // Ottiene il nome utente dall'oggetto Authentication.
        String username = authentication.getName();

        Utente u = utenteService.findByUsername(username);
        if (u != null) {
            model.addAttribute("credito", u.getCredito());
            model.addAttribute("username", u.getUsername());
            model.addAttribute("nomeReale", u.getNome() != null ? u.getNome() : "Cliente");
            model.addAttribute("email", u.getEmail());
            model.addAttribute("connectedMachineId", u.getConnectedMachineId());
        }
        return "homeC";
    }

    /**
     * Endpoint API per la ricarica del credito dell'utente.
     * @param importo L'importo da ricaricare.
     * @param authentication L'oggetto Authentication per recuperare l'utente.
     * @return Messaggio di risultato.
     */
    @PostMapping("/api/utenti/ricarica")
    @ResponseBody
    public String ricaricaCredito(@RequestParam double importo, Authentication authentication) {
        // Anche qui, usa l'oggetto Authentication per sicurezza.
        String username = authentication.getName();
        return utenteService.ricaricaCredito(username, importo);
    }
}
