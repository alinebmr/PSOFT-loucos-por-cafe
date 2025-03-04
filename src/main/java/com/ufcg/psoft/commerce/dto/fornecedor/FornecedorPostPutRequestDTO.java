package com.ufcg.psoft.commerce.dto.fornecedor;

import java.util.HashSet;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ufcg.psoft.commerce.enums.TipoPagamento;

import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
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
public class FornecedorPostPutRequestDTO {

    @JsonProperty("nomeEmpresa")
    @NotBlank(message = "Nome da empresa obrigatorio")
    private String nomeEmpresa;

    @JsonProperty("codigo")
    @NotNull(message = "Codigo de acesso obrigatorio")
    @Pattern(regexp = "^\\d{6}$", message = "Codigo de acesso deve ter exatamente 6 digitos numericos")
    private String codigo;

    @JsonProperty("cnpj")
    @NotNull(message = "CNPJ obrigatorio")
    @Pattern(regexp = "^\\d{2}\\.\\d{3}\\.\\d{3}\\/\\d{4}\\-\\d{2}$", message = "O CNPJ deve conter digitos e ter o seguinte formato XX.XXX.XXX/XXXX-XX")
    private String cnpj;

    @JsonProperty("tipoPagamentos")
    @Nullable
    @Valid
    private HashSet<TipoPagamento> tiposPagamento;
}
