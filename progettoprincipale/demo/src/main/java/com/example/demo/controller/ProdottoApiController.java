package com.example.demo.controller;

import com.example.demo.dto.ProdottoDTO;
import com.example.demo.service.DistributoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller API per la gestione dei prodotti.
 */
@RestController
@RequestMapping("/api")
public class ProdottoApiController {

    @Autowired
    private DistributoreService distributoreService;

    /**
     * Endpoint API per ottenere la lista di tutti i prodotti disponibili.
     * @return Lista di ProdottoDTO.
     */
    @GetMapping("/prodotti")
    public List<ProdottoDTO> getProdotti() {
        return distributoreService.getProdottiDisponibili();
    }
}
