package com.ufcg.psoft.commerce.model;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class Endereco {
    private String cep;
    private String cidade;
    private String bairro;
    private String rua;
    private String numero;
}
