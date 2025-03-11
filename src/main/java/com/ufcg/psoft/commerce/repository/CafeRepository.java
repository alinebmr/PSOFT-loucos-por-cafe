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

    List<Cafe> findByTipo(TipoGraoCafe tipo);

    List<Cafe> findByOrigemContainingIgnoreCaseAndPerfilContainingIgnoreCaseAndTipo(String origem, String perfil, TipoGraoCafe tipo);

    List<Cafe> findByOrigemContainingIgnoreCaseAndPerfilContainingIgnoreCase(String origem, String perfil);

    List<Cafe> findByOrigemContainingIgnoreCaseAndTipo(String origem, TipoGraoCafe tipo);

    List<Cafe> findByPerfilContainingIgnoreCaseAndTipo(String perfil, TipoGraoCafe tipo);

    List<Cafe> findByOrigemContainingIgnoreCase(String origem);

    List<Cafe> findByPerfilContainingIgnoreCase(String perfil);

    List<Cafe> findByQualidade(QualidadeCafe qualidade);

    List<Cafe> findByQualidadeAndTipo(QualidadeCafe qualidade, TipoGraoCafe tipo);

    List<Cafe> findByQualidadeAndOrigemContainingIgnoreCaseAndPerfilContainingIgnoreCaseAndTipo(QualidadeCafe qualidade, String origem, String perfil, TipoGraoCafe tipo);

    List<Cafe> findByQualidadeAndOrigemContainingIgnoreCaseAndPerfilContainingIgnoreCase(QualidadeCafe qualidade, String origem, String perfil);

    List<Cafe> findByQualidadeAndOrigemContainingIgnoreCaseAndTipo(QualidadeCafe qualidade, String origem, TipoGraoCafe tipo);

    List<Cafe> findByQualidadeAndPerfilContainingIgnoreCaseAndTipo(QualidadeCafe qualidade, String perfil, TipoGraoCafe tipo);

    List<Cafe> findByQualidadeAndPerfilContainingIgnoreCase(QualidadeCafe qualidade, String perfil);

    List<Cafe> findByQualidadeAndOrigemContainingIgnoreCase(QualidadeCafe qualidade, String origem);
}