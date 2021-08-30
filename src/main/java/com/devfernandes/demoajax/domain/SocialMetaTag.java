package com.devfernandes.demoajax.domain;

import java.io.Serializable;

public class SocialMetaTag implements Serializable {
    private String site;
    private String title;
    private String url;
    private String imagem;

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }

    @Override
    public String toString() {
        return "SocialMetaTag{" +
                "site='" + site + '\'' +
                ", title='" + title + '\'' +
                ", url='" + url + '\'' +
                ", imagem='" + imagem + '\'' +
                '}';
    }
}
