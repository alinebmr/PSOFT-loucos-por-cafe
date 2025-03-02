package com.ufcg.psoft.commerce.dto.pedido;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ufcg.psoft.commerce.dto.EnderecoDTO;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PedidoPostPutRequestDTO {
    @JsonProperty("endereco")
    @Valid
    @Nullable
    private EnderecoDTO endereco;

    @JsonProperty("idCafe")
    @NotNull(message = "Cafe obrigatorio")
    private Long idCafe;
}