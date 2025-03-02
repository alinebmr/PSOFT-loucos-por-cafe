package com.ufcg.psoft.commerce.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ufcg.psoft.commerce.enums.TipoAssinatura;
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

    @JsonProperty("assinatura")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private TipoAssinatura assinatura = TipoAssinatura.NORMAL;
}