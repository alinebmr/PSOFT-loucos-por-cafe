package com.ufcg.psoft.commerce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ufcg.psoft.commerce.model.Entregador;

public interface EntregadorRespository extends JpaRepository<Entregador, Long> {

    List<Entregador> findByNomeContaining(String nome);
}
