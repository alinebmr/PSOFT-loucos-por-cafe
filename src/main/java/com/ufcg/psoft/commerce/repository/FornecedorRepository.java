package com.ufcg.psoft.commerce.repository;

import com.ufcg.psoft.commerce.model.Fornecedor;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FornecedorRepository extends JpaRepository<Fornecedor, Long> {
    
    List<Fornecedor> findByNomeContaining(String nomeEmpresa);
}