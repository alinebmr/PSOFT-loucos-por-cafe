package com.ufcg.psoft.commerce.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ufcg.psoft.commerce.enums.TipoGraoCafe;
import com.ufcg.psoft.commerce.enums.QualidadeCafe;

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

public class Cafe {

    @JsonProperty("id")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @JsonProperty("idFornecedor")
    @Column(nullable = false)
    private Long idFornecedor;

    @JsonProperty("nome")
    @Column(nullable = false)
    private String nome;

    @JsonProperty("origem")
    @Column(nullable = false)
    private String origem;

    @JsonProperty("tipo")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoGraoCafe tipo;

    @JsonProperty("perfil")
    @Column(nullable = false)
    private String perfil;

    @JsonProperty("preco")
    @Column(nullable = false)
    private double preco;

    @JsonProperty("disponivel")
    @Column(nullable = false)
    @Builder.Default
    private boolean disponivel = true;

    @JsonProperty("qualidade")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private QualidadeCafe qualidade;
    
    @JsonProperty("tamanhoEmbalagem")
    @Column(nullable = false)
    private Integer tamanhoEmbalagem;
}
