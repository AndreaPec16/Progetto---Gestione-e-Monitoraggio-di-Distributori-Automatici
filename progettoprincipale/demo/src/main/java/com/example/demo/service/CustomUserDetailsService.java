package com.example.demo.service;

import com.example.demo.entity.Utente;
import com.example.demo.repository.UtenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;

/**
 * Implementazione custom di UserDetailsService per caricare i dati dell'utente dal database.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UtenteRepository utenteRepository;

    /**
     * Carica i dettagli di un utente tramite il suo username.
     * @param username L'username da cercare.
     * @return Un oggetto UserDetails che rappresenta l'utente.
     * @throws UsernameNotFoundException se l'utente non viene trovato.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Utente utente = utenteRepository.findByUsername(username);
        if (utente == null) {
            throw new UsernameNotFoundException("Utente non trovato con username: " + username);
        }
        
        Collection<? extends GrantedAuthority> authorities = 
            Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + utente.getRuolo()));

        return new User(utente.getUsername(), utente.getPassword(), authorities);
    }
}
