package com.ufcg.psoft.commerce.repository;

import com.ufcg.psoft.commerce.enums.QualidadeCafe;
import com.ufcg.psoft.commerce.enums.TipoGraoCafe;
import com.ufcg.psoft.commerce.model.Cafe;
import com.ufcg.psoft.commerce.model.Fornecedor;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CafeRepository extends JpaRepository<Cafe, Long> {

    List<Cafe> findByNomeContaining(String nome);

    List<Cafe> findByFornecedor(Fornecedor fornecedor);

    List<Cafe> findByDisponivel(boolean disponivel);

    List<Cafe> findByTipoAndDisponivel(TipoGraoCafe tipo, boolean disponivel);

    List<Cafe> findByQualidadeAndDisponivel(QualidadeCafe qualidade, boolean disponivel);

    List<Cafe> findByQualidadeAndTipoAndDisponivel(QualidadeCafe qualidade, TipoGraoCafe tipo, boolean disponivel);
}

