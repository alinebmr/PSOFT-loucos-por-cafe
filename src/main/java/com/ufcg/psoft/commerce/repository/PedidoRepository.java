package com.ufcg.psoft.commerce.repository;

import com.ufcg.psoft.commerce.enums.StatusPedidoEnum;
import com.ufcg.psoft.commerce.model.Cliente;
import com.ufcg.psoft.commerce.model.Fornecedor;
import com.ufcg.psoft.commerce.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    List<Pedido> findByCliente(Cliente cliente);

    List<Pedido> findByCafeFornecedor(Fornecedor fornecedor);

    List<Pedido> findByStatus(StatusPedidoEnum status);

    @Query("SELECT p FROM Pedido p WHERE p.status = :status AND p.cliente = :cliente ORDER BY p.createdAt ASC")
    List<Pedido> findByStatusAndClienteOrderedByCreatedAt(StatusPedidoEnum status, Cliente cliente);

}