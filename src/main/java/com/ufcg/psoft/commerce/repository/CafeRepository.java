package com.ufcg.psoft.commerce.repository;

import com.ufcg.psoft.commerce.model.Cafe;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CafeRepository extends JpaRepository<Cafe, Long> {

    List<Cafe> findByNomeContaining(String nome);

    List<Cafe> findByIdFornecedorContaining(Long idFornecedor);
}

