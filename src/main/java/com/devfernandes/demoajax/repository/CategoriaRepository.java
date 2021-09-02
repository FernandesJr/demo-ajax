package com.devfernandes.demoajax.repository;

import com.devfernandes.demoajax.domain.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
}
