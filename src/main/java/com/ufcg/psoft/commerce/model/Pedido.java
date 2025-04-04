package com.ufcg.psoft.commerce.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ufcg.psoft.commerce.enums.StatusPedidoEnum;
import com.ufcg.psoft.commerce.enums.TipoAssinatura;
import com.ufcg.psoft.commerce.enums.TipoPagamento;
import com.ufcg.psoft.commerce.model.pedido.PedidoEmEntrega;
import com.ufcg.psoft.commerce.model.pedido.PedidoEntregue;
import com.ufcg.psoft.commerce.model.pedido.PedidoPreparacao;
import com.ufcg.psoft.commerce.model.pedido.PedidoPronto;
import com.ufcg.psoft.commerce.model.pedido.PedidoRecebido;
import com.ufcg.psoft.commerce.model.pedido.PedidoState;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class Pedido {
    @JsonProperty("id")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne
    private Cafe cafe;

    @JsonProperty("endereco")
    @Embedded
    private Endereco endereco;

    @ManyToOne
    private Cliente cliente;

    @JsonProperty("pago")
    @Column(nullable = false)
    @Builder.Default
    private boolean pago = false;

    @JsonProperty("tipoPagamento")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoPagamento tipoPagamento;

    @JsonProperty("assinatura")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private TipoAssinatura assinatura = TipoAssinatura.NORMAL;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private StatusPedidoEnum status = StatusPedidoEnum.RECEBIDO;

    @ManyToOne(optional = true)
    @Builder.Default
    private Entregador entregador = null;

    public double getValor() {
        return this.cafe.getPreco() * this.tipoPagamento.getDesconto();
    }

    public void nextState() {
        this.getStateObject().nextState();
    }

    private PedidoState getStateObject() {
        switch (status) {
            case RECEBIDO:
                return new PedidoRecebido(this);
            case PREPARACAO:
                return new PedidoPreparacao(this);
            case PRONTO:
                return new PedidoPronto(this);
            case EM_ENTREGA:
                return new PedidoEmEntrega(this);
            case ENTREGUE:
                return new PedidoEntregue();
            default:
                return null;
        }
    }
}