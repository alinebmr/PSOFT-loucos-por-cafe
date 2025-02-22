package com.ufcg.psoft.commerce.dto.cafe;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ufcg.psoft.commerce.enums.QualidadeCafe;
import com.ufcg.psoft.commerce.enums.TipoGraoCafe;
import com.ufcg.psoft.commerce.model.Cafe;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CafeResponseDTO {

    @JsonProperty("id")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @JsonProperty("idFornecedor")
    @NotBlank(message = "Id do fornecedor obrigatorio")
    private Long idFornecedor;

    @JsonProperty("nome")
    @NotBlank(message = "Nome obrigatorio")
    private String nome;

    @JsonProperty("origem")
    @NotBlank(message = "Origem obrigatoria")
    private String origem;
    
    @JsonProperty("tipo")
    @NotBlank(message = "Tipo obrigatorio")
    private TipoGraoCafe tipo;
    
    @JsonProperty("perfi")
    @NotBlank(message = "Perfil sensorial obrigatorio")
    private String perfil;

    @JsonProperty("preco")
    @NotBlank(message = "Preço obrigatorio")
    private double preco;

    @JsonProperty("qualidade")
    @NotBlank(message = "Qualidade obrigatoria")
    private QualidadeCafe qualidade;

    @JsonProperty("tamanhoEmbalagem")
    @NotBlank(message = "Nome obrigatorio")
    private Integer tamanhoEmbalagem;

    public CafeResponseDTO(Cafe cafe) {
        this.id = cafe.getId();
        this.idFornecedor = cafe.getIdFornecedor();
        this.nome = cafe.getNome();
        this.origem = cafe.getOrigem();
        this.tipo = cafe.getTipo();
        this.perfil = cafe.getPerfil();
        this.preco = cafe.getPreco();
        this.qualidade = cafe.getQualidade();
        this.tamanhoEmbalagem = cafe.getTamanhoEmbalagem();
    }
}
