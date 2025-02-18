package com.ufcg.psoft.commerce.controller.Entregador;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ufcg.psoft.commerce.dto.cliente.ClienteResponseDTO;
import com.ufcg.psoft.commerce.dto.entregador.EntregadorResponseDTO;
import com.ufcg.psoft.commerce.model.Cliente;
import com.ufcg.psoft.commerce.model.Entregador;
import com.ufcg.psoft.commerce.repository.ClienteRepository;
import com.ufcg.psoft.commerce.repository.EntregadorRespository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Testes do controlador de Entregadores")
public class EntregadorTestAula {

    final String URI_ENTREGADORES = "/entregadores";

    @Autowired
    MockMvc driver;

    @Autowired
    EntregadorRespository entregadorRespository;

    ObjectMapper objectMapper = new ObjectMapper();

    List<EntregadorResponseDTO> entregadoresDTO = new ArrayList<>();

    @BeforeEach
    void setup() {
        // Object Mapper suporte para LocalDateTime
        objectMapper.registerModule(new JavaTimeModule());
        Entregador entregador1 = entregadorRespository.save(Entregador.builder()
                .nome("Entregador1")
                .placaVeiculo("OFE-1234")
                .tipoVeiculo("Moto")
                .corVeiculo("Branco")
                .codigo("123456")
                .build());

        Entregador entregador2 = entregadorRespository.save(Entregador.builder()
                .nome("Entregador2")
                .placaVeiculo("IFO-8743")
                .tipoVeiculo("Carro")
                .corVeiculo("Prata")
                .codigo("123456")
                .build());

        EntregadorResponseDTO r1 = EntregadorResponseDTO.builder()
                .nome(entregador1.getNome())
                .placaVeiculo(entregador1.getPlacaVeiculo())
                .tipoVeiculo(entregador1.getTipoVeiculo())
                .corVeiculo(entregador1.getCorVeiculo())
                .id(entregador1.getId())
                .build();

        entregadoresDTO.add(r1);
    }

    @AfterEach
    void tearDown() {
        entregadorRespository.deleteAll();
    }

    @Nested
    @DisplayName("Conjunto de casos de teste da aula")
    class EntregadorVerificacaoNome {

        @Test
        @DisplayName("Quando recuperamos entregadores")
        void quandoRecuperamosEntregadoresValidos() throws Exception {

            String stringBusca = "Entregador1";
            // Act
            String responseJsonString = driver.perform(get(URI_ENTREGADORES)
                            .param("nome", stringBusca))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            String expectedResult = objectMapper
                    .writeValueAsString(entregadoresDTO);

            System.out.println(expectedResult);
            System.out.println(responseJsonString);
            // Assert
            assertEquals(expectedResult, responseJsonString);
        }

    }
}
