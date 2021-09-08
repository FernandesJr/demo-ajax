package com.devfernandes.demoajax.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "categorias")
public class Categoria implements Serializable {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "titulo", nullable = false, unique = true)
    private String titulo;

    //O Json ignore serve para evitar uma recursividade ao inserir uma promocao na resposta Json
    //Ao Chegar para montar a Promocao em Json chega no atributo categoria, a library Jackson vem até aqui
    //Adicina id e titulo, ao Chegar na lista de Promocao volta a Promocao e recomeça tudo novamente.
    @JsonIgnore
    @OneToMany(mappedBy = "categoria")
    private List<Promocao> promocoes;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public List<Promocao> getPromocoes() {
        return promocoes;
    }

    public void setPromocoes(List<Promocao> promocoes) {
        this.promocoes = promocoes;
    }

    @Override
    public String toString() {
        return "Categoria [id=" + id + ", titulo=" + titulo + "]";
    }
}
