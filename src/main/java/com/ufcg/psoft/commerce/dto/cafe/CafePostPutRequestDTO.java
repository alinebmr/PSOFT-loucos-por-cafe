package com.ufcg.psoft.commerce.dto.cafe;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ufcg.psoft.commerce.enums.QualidadeCafe;
import com.ufcg.psoft.commerce.enums.TipoGraoCafe;

import jakarta.validation.Valid;
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
public class CafePostPutRequestDTO {

    @JsonProperty("idFornecedor")
    @NotBlank(message = "Id do fornecedor obrigatorio")
    private Long idFornecedor;

    @JsonProperty("nome")
    @NotBlank(message = "Nome obrigatorio")
    private String nome;

    @JsonProperty("origem")
    @Valid
    @NotNull(message = "Origem obrigatoria")
    private String origem;
    
    @JsonProperty("tipo")
    @Valid
    @NotNull(message = "Tipo obrigatorio")
    private TipoGraoCafe tipo;
    
    @JsonProperty("perfi")
    @Valid
    @NotNull(message = "Perfil sensorial obrigatorio")
    private String perfil;

    @JsonProperty("preco")
    @Valid
    @NotNull(message = "Preço obrigatorio")
    private double preco;
    
    @JsonProperty("qualidade")
    @Valid
    @NotNull(message = "Qualidade obrigatorio")
    private QualidadeCafe qualidade;

    @JsonProperty("tamanhoEmbalagem")
    @Valid
    @NotNull(message = "Tamanho da embalagem obrigatorio")
    private Integer tamanhoEmbalagem;
}
