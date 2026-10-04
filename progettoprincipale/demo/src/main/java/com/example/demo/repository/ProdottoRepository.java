package com.example.demo.repository;

import com.example.demo.entity.Prodotto;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ProdottoRepository extends JpaRepository<Prodotto, Long> {
    
    Optional<Prodotto> findByNome(String nome);
}
