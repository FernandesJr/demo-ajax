package com.devfernandes.demoajax.service;

import com.devfernandes.demoajax.domain.SocialMetaTag;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class SocialMetaTagService {

    private static Logger log = LoggerFactory.getLogger(SocialMetaTagService.class);

    public SocialMetaTag getSocialMetaTagByUrl(String url){

        //Irá verificar se a Url da requisição contém as metas tags
        //via OpenGraph ou Twitter
        SocialMetaTag twitter = this.getTwitterCardByUrl(url);
        if (!isEmpty(twitter)){
            return twitter;
        }

        SocialMetaTag og = this.getOpenGraphByUrl(url);
        if (!isEmpty(og)){
            return og;
        }
        System.out.println("NÃO ACHEI AS META TAGS");
        return null;
    }

    private SocialMetaTag getOpenGraphByUrl(String url){
        SocialMetaTag tag = new SocialMetaTag();
        try {
            Document doc = Jsoup.connect(url).get();
            tag.setTitle(doc.head().select("meta[property=og:title]").attr("content"));
            tag.setSite(doc.head().select("meta[property=og:site_name]").attr("content"));
            tag.setImagem(doc.head().select("meta[property=og:image]").attr("content"));
            tag.setUrl(doc.head().select("meta[property=og:url]").attr("content"));
            log.info("OpenGraph: {} ", tag);
        } catch (IOException e) {
            System.out.println("CAIU NO CATCH NA URL INVÁLIDA");
            log.error(e.getMessage(), e.getCause());
        }

        return tag;
    }

    private SocialMetaTag getTwitterCardByUrl(String url){
        SocialMetaTag tag = new SocialMetaTag();
        try {
            Document doc = Jsoup.connect(url).get();
            tag.setTitle(doc.head().select("meta[name=twitter:title]").attr("content"));
            tag.setSite(doc.head().select("meta[name=twitter:site]").attr("content"));
            tag.setImagem(doc.head().select("meta[name=twitter:image]").attr("content"));
            tag.setUrl(doc.head().select("meta[name=twitter:url]").attr("content"));
            log.info("Twitter: {} ", tag);
        } catch (IOException e) {
            System.out.println("CAIU NO CATCH NA URL INVÁLIDA");
            log.error(e.getMessage(), e.getCause());
        }

        return tag;
    }

    private boolean isEmpty(SocialMetaTag tag){
        if(tag.getTitle().isEmpty()) return true;
        if(tag.getSite().isEmpty()) return true;
        if(tag.getImagem().isEmpty()) return true;
        if(tag.getUrl().isEmpty()) return true;
        return false;
    }

}
