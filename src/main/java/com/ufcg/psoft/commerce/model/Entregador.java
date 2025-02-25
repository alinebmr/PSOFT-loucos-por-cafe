package com.ufcg.psoft.commerce.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
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

public class Entregador {

    @JsonProperty("id")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @JsonProperty("nome")
    @Column(nullable = false)
    private String nome;

    @JsonProperty("placa_veiculo")
    @Column(nullable = false)
    private String placaVeiculo;

    @JsonProperty("tipo_veiculo")
    @Column(nullable = false)
    private String tipoVeiculo;

    @JsonProperty("cor_veiculo")
    @Column(nullable = false)
    private String corVeiculo;

    @JsonIgnore
    @Column(nullable = false)
    private String codigo;

    @JsonProperty("aprovado")
    @Column(nullable = false)
    @Builder.Default
    private boolean aprovado = false;
}
