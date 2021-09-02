package com.devfernandes.demoajax.repository;

import com.devfernandes.demoajax.domain.Promocao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PromocaoRepository extends JpaRepository<Promocao, Long> {
}
