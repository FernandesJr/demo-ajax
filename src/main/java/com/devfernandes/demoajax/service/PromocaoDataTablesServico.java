package com.devfernandes.demoajax.service;

import com.devfernandes.demoajax.domain.Promocao;
import com.devfernandes.demoajax.repository.PromocaoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

public class PromocaoDataTablesServico {
    private String [] cols = {
            "id", "titulo", "site", "linkPromocao", "descricao", "linkImagem",
            "preco", "likes", "dataCadastro", "categoria"};

    public Map<String, Object> execute(PromocaoRepository repository, HttpServletRequest request){

        int start = Integer.parseInt(request.getParameter("start"));
        int lenght = Integer.parseInt(request.getParameter("length"));
        int draw = Integer.parseInt(request.getParameter("draw"));

        //Pagina atual
        int current = currentPage(start, lenght);

        //OrderBy por qual coluna
        String column = columnName(request);

        //Descendente ou Crescente
        Sort.Direction direction = orderBy(request);

        //Paginação
        Pageable pageable = PageRequest.of(current, lenght, direction, column);

        //Capturar o atributo search
        String search = searchBy(request);

        //Consulta ao DB
        Page<Promocao> page = queryBy(search, repository, pageable);

        Map<String, Object> json = new LinkedHashMap<>();
        json.put("draw", draw);
        json.put("recordsTotal", page.getTotalElements());
        json.put("recordsFiltered", page.getTotalElements());
        json.put("data", page.getContent());
        return json;
    }

    private Page<Promocao> queryBy(String search, PromocaoRepository repository, Pageable pageable) {
        if(search.isEmpty()){
            return repository.findAll(pageable);
        }

        //Expressão regular
        if(search.matches("^[0-9]+([.,][0-9]{2})?$")){
            search = search.replace(",",".");
            System.out.println(search);
            return repository.findPromocaoByPreco(new BigDecimal(search), pageable);
        }
        return repository.findPromocaoBySiteOrTituloOrCategoria(search, pageable);
    }

    private String searchBy(HttpServletRequest request) {
        return request.getParameter("search[value]").isEmpty()
                ? ""
                : request.getParameter("search[value]");
    }


    private Sort.Direction orderBy(HttpServletRequest request) {
        String order = request.getParameter("order[0][dir]");
        Sort.Direction sort = Sort.Direction.ASC;
        if(order.equalsIgnoreCase("desc")){
            sort = Sort.Direction.DESC;
        }
        return sort;
    }

    private String columnName(HttpServletRequest request) {
        int iCol = Integer.parseInt(request.getParameter("order[0][column]"));
        return  this.cols[iCol];
    }

    private int currentPage(int start, int lenght) {
        // 0      1       2
        //0-9   10-19   20-29
        //A dataTables envia o número da primeira linha da página, então com isso sabemos em qual página se encontra
        //O tamanho é 10 por default '10 linhas'
        return start / lenght;
    }
}
