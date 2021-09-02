package com.devfernandes.demoajax.web.controller;

import com.devfernandes.demoajax.domain.Categoria;
import com.devfernandes.demoajax.domain.Promocao;
import com.devfernandes.demoajax.repository.CategoriaRepository;
import com.devfernandes.demoajax.repository.PromocaoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/promocao")
public class PromocaoController {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private PromocaoRepository promocaoRepository;

    private static Logger log = LoggerFactory.getLogger(PromocaoController.class);

    @GetMapping("/add")
    public String abrirCadastro(){
        return "promo-add";
    }

    @PostMapping("/save")
    public ResponseEntity<?> salvarPromocao(@Valid Promocao promocao, BindingResult result){

        if(result.hasErrors()){
            Map<String, String> errors = new HashMap<>(); //Json
            for (FieldError error: result.getFieldErrors()){
                errors.put(error.getField(), error.getDefaultMessage());
            }
            System.out.println(errors);
            return ResponseEntity.unprocessableEntity().body(errors);
        }

        //Requisição Ajax o própio SpringBoot identifica os atributos do json e Instancia o promoção
        promocao.setDataCadastro(LocalDateTime.now());
        log.info("Promocao {}", promocao.toString());
        promocaoRepository.save(promocao);
        return ResponseEntity.ok().build();
    }

    //Sempre que cair uma requisição nesse controller é adicionado esse atributo a resposta
    @ModelAttribute(name = "categorias")
    public List<Categoria> getCategorias(){
        return categoriaRepository.findAll();
    }
}
