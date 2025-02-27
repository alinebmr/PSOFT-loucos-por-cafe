package com.ufcg.psoft.commerce.dto.pedido;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ufcg.psoft.commerce.dto.EnderecoDTO;
import com.ufcg.psoft.commerce.model.Cafe;
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
    private EnderecoDTO endereco;

    @JsonProperty("cafe")
    @NotNull(message = "Cafe obrigatorio")
    private Cafe cafe;
}
