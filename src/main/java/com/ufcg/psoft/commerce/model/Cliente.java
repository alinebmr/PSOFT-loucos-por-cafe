package com.ufcg.psoft.commerce.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ufcg.psoft.commerce.enums.TipoAssinatura;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Cliente {
    @JsonProperty("id")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @JsonProperty("nome")
    @Column(nullable = false)
    private String nome;

    @JsonProperty("endereco")
    @Embedded
    private Endereco endereco;

    @JsonIgnore
    @Column(nullable = false)
    private String codigo;

    @JsonProperty("assinatura")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private TipoAssinatura assinatura = TipoAssinatura.NORMAL;

    @JsonProperty("cafesDeInteresse")
    @Builder.Default
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name="interesse_cliente_cafe")
    private List<Cafe> cafesDeInteresse = new ArrayList<Cafe>();

    public void notificaCafeDisponivel(Cafe cafe) {
        System.out.println("Cliente " + this.getNome() + ", o cafe " + cafe.getNome() + " voltou ao estoque.");
    }

    public void notificaPedidoEmRota(Pedido pedido){
        System.out.println("O pedido: " + pedido.toString() + " entrou em processo de entrega\n" +
                "Entregador: " + pedido.getEntregador().getNome() + "\n" +
                "Veículo: [Placa: " + pedido.getEntregador().getPlacaVeiculo() + ", Cor : " + pedido.getEntregador().getCorVeiculo() + ", Tipo: " + pedido.getEntregador().getTipoVeiculo() + "]");

    }

}
