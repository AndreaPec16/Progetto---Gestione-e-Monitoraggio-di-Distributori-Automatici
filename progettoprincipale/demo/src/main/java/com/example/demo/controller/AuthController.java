package com.example.demo.controller;

import com.example.demo.dto.RegistrationDTO;
import com.example.demo.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller per la gestione di login e registrazione.
 */
@Controller
public class AuthController {

    @Autowired
    private AuthService authService;

    /**
     * Mostra la pagina di login.
     * @return La vista "login".
     */
    @GetMapping("/login")
    public String paginaLogin() {
        return "login";
    }

    /**
     * Mostra la pagina di registrazione.
     * @return La vista "registrazione".
     */
    @GetMapping("/registrazione")
    public String paginaRegistrazione() {
        return "registrazione";
    }

    /**
     * Endpoint API per la registrazione di un nuovo utente.
     * @param registrationDTO I dati di registrazione.
     * @return ResponseEntity con il risultato dell'operazione.
     */
    @PostMapping("/api/auth/register")
    @ResponseBody
    public ResponseEntity<String> registerUser(@RequestBody RegistrationDTO registrationDTO) {
        return authService.registerUser(registrationDTO);
    }
}
