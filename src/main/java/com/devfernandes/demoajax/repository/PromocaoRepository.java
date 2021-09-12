package com.devfernandes.demoajax.repository;

import com.devfernandes.demoajax.domain.Promocao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface PromocaoRepository extends JpaRepository<Promocao, Long> {

    //Por a anotação QUERY ser somente para leitura é necessário mudar o default da Transação
    @Transactional(readOnly = false)
    @Modifying
    @Query("update Promocao p set p.likes = p.likes + 1 where p.id = :id")
    void updateSomarLikes(@Param(value = "id") Long id);

    @Query("select p.likes from Promocao p where p.id = :id")
    int findLikesById(@Param(value = "id") Long id);;

    //distinct serve para que venha apenas um registro de cada nome encontrado
    @Query("select distinct p.site from Promocao p where p.site like %:site%")
    List<String> findSitesByTermo(@Param("site") String site);

    @Query("select p from Promocao p where p.site like %:site%")
    Page<Promocao> findPromocaoBySite(@Param("site") String site, Pageable pageable);

    @Query("select p from Promocao p where p.site like %:search% " +
            "or p.titulo like %:search% " +
            "or p.categoria like %:search% ")
    Page<Promocao> findPromocaoBySiteOrTituloOrCategoria(@Param("search") String search, Pageable pageable);

    @Query("select p from Promocao p where p.preco = :preco")
    Page<Promocao> findPromocaoByPreco(@Param("preco")BigDecimal preco, Pageable pageable);

    @Query("select p.dataCadastro from Promocao p")
    Page<LocalDateTime> findUltimaDataDePromocao(Pageable pageable);

    @Query("select count(p.id) as count max(p.dataCadastro) as lastDate" +
            "from Promocao p where p.dataCastro > :data")
    Map<String, Object> totalAndTotalDePromocaoByDataCadastro(@Param("data") LocalDateTime data);
}
