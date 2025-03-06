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

    List<Cafe> findByDisponivelAndOrigemContainingIgnoreCaseAndPerfilContainingIgnoreCaseAndTipo(boolean disponivel, String origem, String perfil, TipoGraoCafe tipo);

    List<Cafe> findByDisponivelAndOrigemContainingIgnoreCaseAndPerfilContainingIgnoreCase(boolean disponivel, String origem, String perfil);

    List<Cafe> findByDisponivelAndOrigemContainingIgnoreCaseAndTipo(boolean disponivel, String origem, TipoGraoCafe tipo);

    List<Cafe> findByDisponivelAndPerfilContainingIgnoreCaseAndTipo(boolean disponivel, String perfil, TipoGraoCafe tipo);

    List<Cafe> findByDisponivelAndOrigemContainingIgnoreCase(boolean disponivel, String origem);

    List<Cafe> findByDisponivelAndPerfilContainingIgnoreCase(boolean disponivel, String perfil);

    List<Cafe> findByQualidadeAndDisponivel(QualidadeCafe qualidade, boolean disponivel);

    List<Cafe> findByQualidadeAndTipoAndDisponivel(QualidadeCafe qualidade, TipoGraoCafe tipo, boolean disponivel);

    List<Cafe> findByQualidadeAndDisponivelAndOrigemContainingIgnoreCaseAndPerfilContainingIgnoreCaseAndTipo(QualidadeCafe qualidade, boolean disponivel, String origem, String perfil, TipoGraoCafe tipo);

    List<Cafe> findByQualidadeAndDisponivelAndOrigemContainingIgnoreCaseAndPerfilContainingIgnoreCase(QualidadeCafe qualidade, boolean disponivel, String origem, String perfil);

    List<Cafe> findByQualidadeAndDisponivelAndOrigemContainingIgnoreCaseAndTipo(QualidadeCafe qualidade, boolean disponivel, String origem, TipoGraoCafe tipo);

    List<Cafe> findByQualidadeAndDisponivelAndPerfilContainingIgnoreCaseAndTipo(QualidadeCafe qualidade, boolean disponivel, String perfil, TipoGraoCafe tipo);

    List<Cafe> findByQualidadeAndDisponivelAndPerfilContainingIgnoreCase(QualidadeCafe qualidade, boolean disponivel, String perfil);

    List<Cafe> findByQualidadeAndDisponivelAndOrigemContainingIgnoreCase(QualidadeCafe qualidade, boolean disponivel, String origem);
}