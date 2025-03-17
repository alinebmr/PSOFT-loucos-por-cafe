package com.ufcg.psoft.commerce.model;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ufcg.psoft.commerce.enums.TipoPagamento;

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
public class Fornecedor {
    @JsonProperty("id")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @JsonProperty("nomeEmpresa")
    @Column(nullable = false)
    private String nomeEmpresa;

    @JsonIgnore
    @Column(nullable = false)
    private String codigo;

    @JsonProperty("cnpj")
    @Column(nullable = false)
    private String cnpj;

    @JsonProperty("tiposPagamento")
    @Builder.Default
    @ElementCollection(targetClass = TipoPagamento.class)
    @Enumerated(EnumType.STRING)
    private Set<TipoPagamento> tiposPagamento = new HashSet<>();
}
