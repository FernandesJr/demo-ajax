package com.devfernandes.demoajax.web.controller;

import com.devfernandes.demoajax.domain.Categoria;
import com.devfernandes.demoajax.domain.Promocao;
import com.devfernandes.demoajax.repository.CategoriaRepository;
import com.devfernandes.demoajax.repository.PromocaoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/list")
    public String listPromocoes(ModelMap model){
        Sort sort = Sort.by( Sort.Direction.DESC , "dataCadastro" ); //Ordenando pela data mais recente
        PageRequest pageRequest = PageRequest.of(0,8,sort); //Paginação da JPA :0
        model.addAttribute("promocoes", promocaoRepository.findAll(pageRequest));
        return "promo-list";
    }

    @GetMapping("/list/ajax")
    public String scroll(@RequestParam(name = "page", defaultValue = "1") int page,
                         @RequestParam(name = "site", defaultValue = "") String site, ModelMap model){
        Sort sort = Sort.by( Sort.Direction.DESC , "dataCadastro" ); //Ordenando pela data mais recente
        PageRequest pageRequest = PageRequest.of(page,8,sort); //Paginação da JPA :0
        //Verificar se a requisição está vindo com ou sem filtro de pesquisa
        if(site.isEmpty()){
            model.addAttribute("promocoes", promocaoRepository.findAll(pageRequest));
        }else{
            model.addAttribute("promocoes", promocaoRepository.findPromocaoBySite(site, pageRequest));
        }
        return "promo-card";
    }

    @PostMapping("/like/{id}")
    ResponseEntity<?> adicionarLikes(@PathVariable("id") Long id){
        promocaoRepository.updateSomarLikes(id);
        int totLikes = promocaoRepository.findLikesById(id);
        return ResponseEntity.ok(totLikes);
    }

    @GetMapping("/site")
    ResponseEntity<?> autocompleteByTermo(@RequestParam("termo") String termo){
        List<String> nomesSites = promocaoRepository.findSitesByTermo(termo);
        return ResponseEntity.ok(nomesSites);
    }

    @GetMapping("/site/list")
    String filtarBySite(@RequestParam("site")String site, ModelMap model){
        Sort sort = Sort.by( Sort.Direction.DESC , "dataCadastro" ); //Ordenando pela data mais recente
        PageRequest pageRequest = PageRequest.of(0,8,sort); //Paginação da JPA :0
        model.addAttribute("promocoes", promocaoRepository.findPromocaoBySite(site, pageRequest));
        return "promo-card";
    }



    //Sempre que cair uma requisição nesse controller é adicionado esse atributo a resposta
    @ModelAttribute(name = "categorias")
    public List<Categoria> getCategorias(){
        return categoriaRepository.findAll();
    }
}
