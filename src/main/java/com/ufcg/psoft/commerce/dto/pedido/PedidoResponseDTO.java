package com.ufcg.psoft.commerce.dto.pedido;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ufcg.psoft.commerce.dto.EnderecoDTO;
import com.ufcg.psoft.commerce.dto.cafe.CafeResponseDTO;
import com.ufcg.psoft.commerce.dto.cliente.ClienteResponseDTO;
import com.ufcg.psoft.commerce.dto.fornecedor.FornecedorResponseDTO;
import com.ufcg.psoft.commerce.enums.TipoAssinatura;
import com.ufcg.psoft.commerce.model.Pedido;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PedidoResponseDTO {
    @JsonProperty("id")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @JsonProperty("fornecedor")
    @NotNull(message = "Fornecedor obrigatorio")
    private FornecedorResponseDTO fornecedor;

    @JsonProperty("cliente")
    @NotNull(message = "Cliente obrigatorio")
    private ClienteResponseDTO cliente;

    @JsonProperty("endereco")
    private EnderecoDTO endereco;

    @JsonProperty("cafe")
    @NotBlank(message = "Cafe obrigatorio")
    private CafeResponseDTO cafe;

    @JsonProperty("pago")
    private boolean pago;

    @JsonProperty("assinatura")
    private TipoAssinatura assinatura;

    @JsonProperty("valor")
    private double valor;

    public PedidoResponseDTO(Pedido pedido) {
        this.id = pedido.getId();
        this.fornecedor = new FornecedorResponseDTO(pedido.getCafe().getFornecedor());
        this.cliente = new ClienteResponseDTO(pedido.getCliente());
        this.cafe = new CafeResponseDTO(pedido.getCafe());
        this.endereco = new EnderecoDTO(pedido.getEndereco() != null ? pedido.getEndereco() : pedido.getCliente().getEndereco());
        this.pago = pedido.isPago();
        this.assinatura = pedido.getAssinatura();
        this.valor = pedido.getValor();
    }
}