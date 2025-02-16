package com.ufcg.psoft.commerce.repository;

import com.ufcg.psoft.commerce.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AdminRepository extends JpaRepository<Admin, Long>{

    List<Admin> findByNomeContaining(String nome);
}