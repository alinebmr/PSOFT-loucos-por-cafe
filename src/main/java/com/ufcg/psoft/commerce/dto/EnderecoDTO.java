package com.ufcg.psoft.commerce.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ufcg.psoft.commerce.model.Endereco;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EnderecoDTO {
    @JsonProperty("cep")
    @NotBlank(message = "CEP obrigatorio")
    @Pattern(regexp = "^\\d{8}$", message = "CEP deve ter exatamente 8 digitos numericos")
    private String cep;

    @JsonProperty("cidade")
    @NotBlank(message = "Cidade obrigatorio")
    private String cidade;

    @JsonProperty("bairro")
    @NotBlank(message = "Bairro obrigatorio")
    private String bairro;

    @JsonProperty("rua")
    @NotBlank(message = "Rua obrigatorio")
    private String rua;

    @JsonProperty("numero")
    @NotBlank(message = "Numero obrigatorio")
    private String numero;

    public EnderecoDTO(Endereco endereco) {
        this.cep = endereco.getCep();
        this.cidade = endereco.getCidade();
        this.bairro = endereco.getBairro();
        this.rua = endereco.getRua();
        this.numero = endereco.getNumero();
    }
}
