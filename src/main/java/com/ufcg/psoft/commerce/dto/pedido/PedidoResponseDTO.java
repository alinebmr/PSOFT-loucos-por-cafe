package com.ufcg.psoft.commerce.dto.pedido;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ufcg.psoft.commerce.model.Cafe;
import com.ufcg.psoft.commerce.model.Endereco;
import com.ufcg.psoft.commerce.model.Pedido;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class PedidoResponseDTO {
    @JsonProperty("id")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @JsonProperty("idFornecedor")
    @NotNull(message = "Id do fornecedor obrigatorio")
    private Long idFornecedor;

    @JsonProperty("idCliente")
    @NotNull(message = "Id do cliente obrigatorio")
    private Long idCliente;

    @JsonProperty("endereco")
    private Endereco endereco;

    @JsonProperty("cafe")
    @NotBlank(message = "Cafe obrigatorio")
    private Cafe cafe;

    @JsonProperty("statusPagamento")
    private boolean pago;

    public PedidoResponseDTO(Pedido pedido) {
        this.id = pedido.getId();
        this.idFornecedor = pedido.getCafe().getFornecedor().getId();
        this.idCliente = pedido.getCliente().getId();
        this.cafe = pedido.getCafe();
        this.endereco = pedido.getEndereco() != null ? pedido.getEndereco() : pedido.getCliente().getEndereco();
        this.pago = pedido.isPago();
    }
}