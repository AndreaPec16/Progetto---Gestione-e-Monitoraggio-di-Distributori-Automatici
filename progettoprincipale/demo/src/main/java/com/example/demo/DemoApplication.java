package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * Classe principale dell'applicazione Spring Boot.
 */
@SpringBootApplication
public class DemoApplication {

	/**
	 * Metodo main, punto di avvio dell'applicazione.
	 * @param args Argomenti passati dalla riga di comando.
	 */
	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	/**
	 * Bean per creare un'istanza di RestTemplate, utilizzata per le chiamate HTTP.
	 * @return Un'istanza di RestTemplate.
	 */
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
