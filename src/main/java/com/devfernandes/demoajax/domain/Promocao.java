package com.devfernandes.demoajax.domain;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "promocoes")
public class Promocao implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Titulo não pode ser vazio.")
    @Column(name = "titulo", nullable = false)
    private String titulo;

    @NotBlank(message = "O link da oferta não pode ser vazio.")
    @Column(name = "link_promocao", nullable = false)
    private String linkPromocao;

    @Column(name = "site_promocao", nullable = false)
    private String site;

    @Column(name = "link_imagem", nullable = false)
    private String linkImagem;

    @Column(name = "descricao")
    private String descricao;

    @NotNull(message = "Por favor, informe o preço da promoção.")
    @NumberFormat(style = NumberFormat.Style.CURRENCY, pattern = "#,##0.00")
    @Column(name = "preco_promocao", nullable = false)
    private BigDecimal preco;

    @Column(name = "total_likes")
    private int likes;

    @Column(name = "data_cadastro", nullable = false)
    private LocalDateTime dataCadastro;

    @NotNull(message = "Por favor, informe a categoria da promoção.")
    @ManyToOne
    @JoinColumn(name = "categoria_fk")
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

    public String getLinkPromocao() {
        return linkPromocao;
    }

    public void setLinkPromocao(String linkPromocao) {
        this.linkPromocao = linkPromocao;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getLinkImagem() {
        return linkImagem;
    }

    public void setLinkImagem(String linkImagem) {
        this.linkImagem = linkImagem;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public LocalDateTime getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(LocalDateTime dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    @Override
    public String toString() {
        return "Promocao{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", linkPromocao='" + linkPromocao + '\'' +
                ", site='" + site + '\'' +
                ", linkImagem='" + linkImagem + '\'' +
                ", descricao='" + descricao + '\'' +
                ", preco=" + preco +
                ", likes=" + likes +
                ", dataCadastro=" + dataCadastro +
                ", categoria=" + categoria +
                '}';
    }
}
