package com.devfernandes.demoajax.dto;

import com.devfernandes.demoajax.domain.Categoria;
import org.springframework.format.annotation.NumberFormat;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class PromocaoDto {

    @NotNull
    private Long id;

    @NotBlank(message = "Titulo não pode ser vazio.")
    private String titulo;

    @NotBlank(message = "link da imagem não pode ser vazio.")
    private String linkImagem;

    @NotNull(message = "Por favor, informe o preço da promoção.")
    @NumberFormat(style = NumberFormat.Style.CURRENCY, pattern = "#,##0.00")
    private BigDecimal preco;

    private String descricao;

    @NotNull(message = "Por favor, informe a categoria da promoção.")
    private Categoria categoria;

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

    public String getLinkImagem() {
        return linkImagem;
    }

    public void setLinkImagem(String linkImagem) {
        this.linkImagem = linkImagem;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }
}
