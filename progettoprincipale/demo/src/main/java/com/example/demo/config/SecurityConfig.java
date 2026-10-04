package com.example.demo.config;

import com.example.demo.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

/**
 * Configurazione di Spring Security.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Bean per l'encoder delle password.
     * @return Un'istanza di BCryptPasswordEncoder.
     */
    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Provider di autenticazione che utilizza un UserDetailsService custom.
     * @param customUserDetailsService Il servizio per caricare i dettagli dell'utente.
     * @return Un DaoAuthenticationProvider configurato.
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider(CustomUserDetailsService customUserDetailsService) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(customUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /**
     * Gestore custom per il successo dell'autenticazione, che reindirizza in base al ruolo.
     * @return Un AuthenticationSuccessHandler.
     */
    @Bean
    public AuthenticationSuccessHandler customAuthenticationSuccessHandler() {
        return (request, response, authentication) -> {
            authentication.getAuthorities().forEach(authority -> {
                try {
                    if (authority.getAuthority().equals("ROLE_GESTORE")) {
                        response.sendRedirect("/Gestore/home");
                    } else if (authority.getAuthority().equals("ROLE_MANUTENTORE")) {
                        response.sendRedirect("/manutenzione");
                    } else if (authority.getAuthority().equals("ROLE_CLIENTE")) {
                        response.sendRedirect("/Cliente/home");
                    }
                } catch (Exception e) {
                    System.err.println("Errore durante il reindirizzamento: " + e.getMessage());
                }
            });
        };
    }

    /**
     * Configura la catena di filtri di sicurezza HTTP.
     * @param http L'oggetto HttpSecurity da configurare.
     * @return La SecurityFilterChain costruita.
     * @throws Exception se si verifica un errore durante la configurazione.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authorize -> authorize
                // Regole pubbliche
                .requestMatchers("/login", "/registrazione", "/api/auth/register").permitAll()
                .requestMatchers("/css/**", "/js/**", "/immagini/**", "/Macchina/**", "/Cliente/registrazione.css", "/Cliente/registrazione.js").permitAll()
                .requestMatchers("/macchinetta/**", "/api/prodotti", "/api/distributori/*/check-utente").permitAll()

                //  Permetti acquisto e controllo stato a tutti gli utenti autenticati ---
                .requestMatchers("/api/distributori/*/acquista", "/api/distributori/*/stato").authenticated()

                // Regole protette
                .requestMatchers("/Gestore/**", "/api/manutentori/**").hasRole("GESTORE")
                .requestMatchers("/manutenzione").hasRole("MANUTENTORE")
                .requestMatchers("/Cliente/home", "/api/utenti/**", "/api/connect", "/api/disconnect").hasRole("CLIENTE")
                
                .requestMatchers("/api/distributori/**").hasAnyRole("MANUTENTORE", "GESTORE")

                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .successHandler(customAuthenticationSuccessHandler())
                .failureUrl("/login?error=true")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            )
            .csrf(csrf -> csrf.disable());

        return http.build();
    }
}
