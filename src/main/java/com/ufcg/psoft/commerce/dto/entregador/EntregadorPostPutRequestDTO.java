package com.ufcg.psoft.commerce.dto.entregador;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EntregadorPostPutRequestDTO {

    @JsonProperty("nome")
    @NotBlank(message = "Nome obrigatorio")
    private String nome;

    @JsonProperty("placa_veiculo")
    @NotBlank(message = "Placa do veiculo obrigatoria")
    private String placaVeiculo;

    @JsonProperty("tipo_veiculo")
    @NotBlank(message = "Tipo do veiculo obrigatorio")
    private String tipoVeiculo;

    @JsonProperty("cor_veiculo")
    @NotBlank(message = "Cor do veiculo obrigatoria")
    private String corVeiculo;

    @JsonProperty("codigo")
    @NotNull(message = "Codigo de acesso obrigatorio")
    @Pattern(regexp = "^\\d{6}$", message = "Codigo de acesso deve ter exatamente 6 digitos numericos")
    private String codigo;
}
