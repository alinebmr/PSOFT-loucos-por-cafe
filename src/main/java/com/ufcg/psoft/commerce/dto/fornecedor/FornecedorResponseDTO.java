package com.ufcg.psoft.commerce.dto.fornecedor;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ufcg.psoft.commerce.model.Fornecedor;
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
public class FornecedorResponseDTO {

    @JsonProperty("id")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @JsonProperty("nomeEmpresa")
    @NotBlank(message = "Nome da empresa obrigatorio")
    private String nomeEmpresa;

    @JsonProperty("cnpj")
    @NotBlank(message = "CNPJ obrigatorio")
    private String cnpj;

    public FornecedorResponseDTO(Fornecedor fornecedor) {
        this.id = fornecedor.getId();
        this.nomeEmpresa = fornecedor.getNomeEmpresa();
        this.cnpj = fornecedor.getCnpj();
    }
}