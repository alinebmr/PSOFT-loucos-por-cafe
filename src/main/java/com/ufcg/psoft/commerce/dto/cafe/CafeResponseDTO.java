package com.ufcg.psoft.commerce.dto.cafe;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ufcg.psoft.commerce.enums.QualidadeCafe;
import com.ufcg.psoft.commerce.enums.TipoGraoCafe;
import com.ufcg.psoft.commerce.model.Cafe;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Min;
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
public class CafeResponseDTO {

    @JsonProperty("id")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @JsonProperty("idFornecedor")
    @NotNull(message = "Id do fornecedor obrigatorio")
    private Long idFornecedor;

    @JsonProperty("nome")
    @NotBlank(message = "Nome obrigatorio")
    private String nome;

    @JsonProperty("origem")
    @NotBlank(message = "Origem obrigatoria")
    private String origem;

    @JsonProperty("tipo")
    @NotNull(message = "Tipo obrigatorio")
    private TipoGraoCafe tipo;

    @JsonProperty("perfil")
    @NotBlank(message = "Perfil sensorial obrigatorio")
    private String perfil;

    @JsonProperty("preco")
    @NotNull(message = "Preço obrigatorio")
    @Min(value = 0L, message = "Preco deve ser maior que zero")
    private double preco;

    @JsonProperty("qualidade")
    @NotNull(message = "Qualidade obrigatoria")
    private QualidadeCafe qualidade;

    @JsonProperty("tamanhoEmbalagem")
    @Min(value = 0L, message = "Tamanho da embalagem deve ser maior que 0")
    @NotBlank(message = "Tamanho da embalagem obrigatorio")
    private Integer tamanhoEmbalagem;

    @JsonProperty("disponivel")
    private boolean disponivel;

    public CafeResponseDTO(Cafe cafe) {
        this.id = cafe.getId();
        this.idFornecedor = cafe.getFornecedor().getId();
        this.nome = cafe.getNome();
        this.origem = cafe.getOrigem();
        this.tipo = cafe.getTipo();
        this.perfil = cafe.getPerfil();
        this.preco = cafe.getPreco();
        this.qualidade = cafe.getQualidade();
        this.tamanhoEmbalagem = cafe.getTamanhoEmbalagem();
        this.disponivel = cafe.isDisponivel();
    }
}
