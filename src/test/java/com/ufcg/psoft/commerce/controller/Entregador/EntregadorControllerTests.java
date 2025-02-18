package com.ufcg.psoft.commerce.controller.Entregador;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import com.ufcg.psoft.commerce.dto.entregador.EntregadorResponseDTO;
import com.ufcg.psoft.commerce.exception.CustomErrorType;
import com.ufcg.psoft.commerce.model.Cliente;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ufcg.psoft.commerce.dto.entregador.EntregadorPostPutRequestDTO;
import com.ufcg.psoft.commerce.model.Entregador;
import com.ufcg.psoft.commerce.repository.EntregadorRespository;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Testes do controlador de Entregadores")
public class EntregadorControllerTests {

    final String URI_ENTREGADORES = "/entregadores";

    @Autowired
    MockMvc driver;

    @Autowired
    EntregadorRespository entregadorRespository;

    ObjectMapper objectMapper = new ObjectMapper();

    Entregador entregador;

    EntregadorPostPutRequestDTO entregadorPostPutRequestDTO;

    @BeforeEach
    void setup() {
        objectMapper.registerModule(new JavaTimeModule());
        entregador = entregadorRespository.save(Entregador.builder()
                .nome("Jose Farias")
                .codigo("123456")
                .placaVeiculo("EFI-2345")
                .tipoVeiculo("Carro")
                .corVeiculo("Branco")
                .build());
        entregadorPostPutRequestDTO = EntregadorPostPutRequestDTO.builder()
                .nome(entregador.getNome())
                .codigo(entregador.getCodigo())
                .placaVeiculo(entregador.getPlacaVeiculo())
                .tipoVeiculo(entregador.getTipoVeiculo())
                .corVeiculo(entregador.getCorVeiculo())
                .build();
    }

    @AfterEach
    void tearDown() {
        entregadorRespository.deleteAll();
    }

    @Nested
    @DisplayName("Conjunto de casos de verificação de nome")
    class EntregadorVerificacaoNome {

        @Test
        @DisplayName("Quando recuperamos um entregador com dados válidos")
        void quandoRecuperamosNomeDoEntregadorValido() throws Exception {

            String responseJsonString = driver.perform(get(URI_ENTREGADORES + "/" + entregador.getId()))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn()
                    .getResponse()
                    .getContentAsString();

            Entregador resultado = objectMapper.readValue(responseJsonString, Entregador.EntregadorBuilder.class)
                    .build();

            assertEquals("Jose Farias", resultado.getNome());

        }

        @Test
        @DisplayName("Quando alteramos o nome do entregador com dados válidos")
        void quandoAlteramosONomeDoEntregadorValido() throws Exception{
            entregadorPostPutRequestDTO.setNome("Jose Farias Alterado");

            // Act
            String responseJsonString = driver.perform(put(URI_ENTREGADORES + "/" + entregador.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigo", entregador.getCodigo())
                            .content(objectMapper.writeValueAsString(entregadorPostPutRequestDTO)))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            Entregador resultado = objectMapper.readValue(responseJsonString, Entregador.EntregadorBuilder.class)
                    .build();

            // Assert
            assertEquals("Jose Farias Alterado", resultado.getNome());
        }

        @Test
        @DisplayName("Quando alteramos o nome do entregador nulo")
        void quandoAlteramosNomeDoEntregadorNulo() throws Exception {
            // Arrange
            entregadorPostPutRequestDTO.setNome(null);

            // Act
            String responseJsonString = driver.perform(put(URI_ENTREGADORES + "/" + entregador.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigo", entregador.getCodigo())
                            .content(objectMapper.writeValueAsString(entregadorPostPutRequestDTO)))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertAll(
                    () -> assertEquals("Erros de validacao encontrados", resultado.getMessage()),
                    () -> assertEquals("Nome obrigatorio", resultado.getErrors().get(0)));
        }

        @Test
        @DisplayName("Quando alteramos o nome do entregador vazio")
        void quandoAlteramosNomeDoEntregadorVazio() throws Exception {
            // Arrange
            entregadorPostPutRequestDTO.setNome("");

            // Act
            String responseJsonString = driver.perform(put(URI_ENTREGADORES + "/" + entregador.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigo", entregador.getCodigo())
                            .content(objectMapper.writeValueAsString(entregadorPostPutRequestDTO)))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertAll(
                    () -> assertEquals("Erros de validacao encontrados", resultado.getMessage()),
                    () -> assertEquals("Nome obrigatorio", resultado.getErrors().get(0)));
        }
    }

    @Nested
    @DisplayName("Conjunto de casos de verificação da placa do veículo")
    class ClienteVerificacaoPlacaVeiculo {

        @Test
        @DisplayName("Quando alteramos a placa do veiculo do entregador com dados válidos")
        void quandoAlteramosPlacaVeiculoEntregadorValido() throws Exception {
            // Arrange
            entregadorPostPutRequestDTO.setPlacaVeiculo("OFI-1234");

            // Act
            String responseJsonString = driver.perform(put(URI_ENTREGADORES + "/" + entregador.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigo", entregador.getCodigo())
                            .content(objectMapper.writeValueAsString(entregadorPostPutRequestDTO)))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            EntregadorResponseDTO resultado = objectMapper.readValue(responseJsonString,
                    EntregadorResponseDTO.EntregadorResponseDTOBuilder.class).build();

            // Assert
            assertEquals("OFI-1234", resultado.getPlacaVeiculo());
        }

        @Test
        @DisplayName("Quando alteramos a placa do veiculo do entregador nulo")
        void quandoAlteramosPlacaVeiculoDoNulo() throws Exception {
            // Arrange
            entregadorPostPutRequestDTO.setPlacaVeiculo(null);

            // Act
            String responseJsonString = driver.perform(put(URI_ENTREGADORES + "/" + entregador.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigo", entregador.getCodigo())
                            .content(objectMapper.writeValueAsString(entregadorPostPutRequestDTO)))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertAll(
                    () -> assertEquals("Erros de validacao encontrados", resultado.getMessage()),
                    () -> assertEquals("Placa do veiculo obrigatoria", resultado.getErrors().get(0)));
        }

        @Test
        @DisplayName("Quando alteramos a placa do veiculo do entregador vazio")
        void quandoAlteramosPlacaVeiculoDoVazio() throws Exception {
            // Arrange
            entregadorPostPutRequestDTO.setPlacaVeiculo("");

            // Act
            String responseJsonString = driver.perform(put(URI_ENTREGADORES + "/" + entregador.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigo", entregador.getCodigo())
                            .content(objectMapper.writeValueAsString(entregadorPostPutRequestDTO)))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertAll(
                    () -> assertEquals("Erros de validacao encontrados", resultado.getMessage()),
                    () -> assertEquals("Placa do veiculo obrigatoria", resultado.getErrors().get(0)));
        }
  }

    @Nested
    @DisplayName("Conjunto de casos de verificação do tipo do veiculo")
    class ClienteVerificacaoTipoVeiculo {

        @Test
        @DisplayName("Quando alteramos o tipo do veiculo do entregador com dados válidos")
        void quandoAlteramosPlacaVeiculoEntregadorValido() throws Exception {
            // Arrange
            entregadorPostPutRequestDTO.setTipoVeiculo("Carro");

            // Act
            String responseJsonString = driver.perform(put(URI_ENTREGADORES + "/" + entregador.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigo", entregador.getCodigo())
                            .content(objectMapper.writeValueAsString(entregadorPostPutRequestDTO)))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            EntregadorResponseDTO resultado = objectMapper.readValue(responseJsonString,
                    EntregadorResponseDTO.EntregadorResponseDTOBuilder.class).build();

            // Assert
            assertEquals("Carro", resultado.getTipoVeiculo());
        }


        @Test
        @DisplayName("Quando alteramos o tipo do veiculo do entregador nulo")
        void quandoAlteramosTipoVeiculoDoNulo() throws Exception {
            // Arrange
            entregadorPostPutRequestDTO.setTipoVeiculo(null);

            // Act
            String responseJsonString = driver.perform(put(URI_ENTREGADORES + "/" + entregador.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigo", entregador.getCodigo())
                            .content(objectMapper.writeValueAsString(entregadorPostPutRequestDTO)))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertAll(
                    () -> assertEquals("Erros de validacao encontrados", resultado.getMessage()),
                    () -> assertEquals("Tipo do veiculo obrigatorio", resultado.getErrors().get(0)));
        }

        @Test
        @DisplayName("Quando alteramos o tipo do veiculo do entregador vazio")
        void quandoAlteramosTipoVeiculoDoVazio() throws Exception {
            // Arrange
            entregadorPostPutRequestDTO.setTipoVeiculo("");

            // Act
            String responseJsonString = driver.perform(put(URI_ENTREGADORES + "/" + entregador.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigo", entregador.getCodigo())
                            .content(objectMapper.writeValueAsString(entregadorPostPutRequestDTO)))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertAll(
                    () -> assertEquals("Erros de validacao encontrados", resultado.getMessage()),
                    () -> assertEquals("Tipo do veiculo obrigatorio", resultado.getErrors().get(0)));
        }
    }

    @Nested
    @DisplayName("Conjunto de casos de verificação da cor do veiculo")
    class ClienteVerificacaoCorVeiculo {

        @Test
        @DisplayName("Quando alteramos o tipo do veiculo do entregador com dados válidos")
        void quandoAlteramosCorVeiculoEntregadorValido() throws Exception {
            // Arrange
            entregadorPostPutRequestDTO.setCorVeiculo("Branco");

            // Act
            String responseJsonString = driver.perform(put(URI_ENTREGADORES + "/" + entregador.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigo", entregador.getCodigo())
                            .content(objectMapper.writeValueAsString(entregadorPostPutRequestDTO)))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            EntregadorResponseDTO resultado = objectMapper.readValue(responseJsonString,
                    EntregadorResponseDTO.EntregadorResponseDTOBuilder.class).build();

            // Assert
            assertEquals("Branco", resultado.getCorVeiculo());
        }


        @Test
        @DisplayName("Quando alteramos a cor do veiculo do entregador nulo")
        void quandoAlteramosCorVeiculoDoNulo() throws Exception {
            // Arrange
            entregadorPostPutRequestDTO.setCorVeiculo(null);

            // Act
            String responseJsonString = driver.perform(put(URI_ENTREGADORES + "/" + entregador.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigo", entregador.getCodigo())
                            .content(objectMapper.writeValueAsString(entregadorPostPutRequestDTO)))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertAll(
                    () -> assertEquals("Erros de validacao encontrados", resultado.getMessage()),
                    () -> assertEquals("Cor do veiculo obrigatoria", resultado.getErrors().get(0)));
        }

        @Test
        @DisplayName("Quando alteramos a cor do veiculo do entregador vazio")
        void quandoAlteramosCorVeiculoDoVazio() throws Exception {
            // Arrange
            entregadorPostPutRequestDTO.setCorVeiculo("");

            // Act
            String responseJsonString = driver.perform(put(URI_ENTREGADORES + "/" + entregador.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigo", entregador.getCodigo())
                            .content(objectMapper.writeValueAsString(entregadorPostPutRequestDTO)))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertAll(
                    () -> assertEquals("Erros de validacao encontrados", resultado.getMessage()),
                    () -> assertEquals("Cor do veiculo obrigatoria", resultado.getErrors().get(0)));
        }
    }




    @Nested
    @DisplayName("Conjunto de casos de verificação do código de acesso")
    class EntregadorVerificacaoCodigoAcesso {

        @Test
        @DisplayName("Quando alteramos o código de acesso do entregador nulo")
        void quandoAlteramosCodigoAcessoDoEntregadorNulo() throws Exception {
            // Arrange
            entregadorPostPutRequestDTO.setCodigo(null);

            // Act
            String responseJsonString = driver.perform(put(URI_ENTREGADORES + "/" + entregador.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigo", entregador.getCodigo())
                            .content(objectMapper.writeValueAsString(entregadorPostPutRequestDTO)))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertAll(
                    () -> assertEquals("Erros de validacao encontrados", resultado.getMessage()),
                    () -> assertEquals("Codigo de acesso obrigatorio",
                            resultado.getErrors().get(0)));
        }

        @Test
        @DisplayName("Quando alteramos o código de acesso do entregador mais de 6 digitos")
        void quandoAlteramosCodigoAcessoDoEntregadorMaisDe6Digitos() throws Exception {
            // Arrange
            entregadorPostPutRequestDTO.setCodigo("1234567");

            // Act
            String responseJsonString = driver.perform(put(URI_ENTREGADORES + "/" + entregador.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigo", entregador.getCodigo())
                            .content(objectMapper.writeValueAsString(entregadorPostPutRequestDTO)))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertAll(
                    () -> assertEquals("Erros de validacao encontrados", resultado.getMessage()),
                    () -> assertEquals("Codigo de acesso deve ter exatamente 6 digitos numericos",
                            resultado.getErrors().get(0)));
        }

        @Test
        @DisplayName("Quando alteramos o código de acesso do entregador menos de 6 digitos")
        void quandoAlteramosCodigoAcessoDoEntregadorMenosDe6Digitos() throws Exception {
            // Arrange
            entregadorPostPutRequestDTO.setCodigo("12345");

            // Act
            String responseJsonString = driver.perform(put(URI_ENTREGADORES + "/" + entregador.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigo", entregador.getCodigo())
                            .content(objectMapper.writeValueAsString(entregadorPostPutRequestDTO)))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertAll(
                    () -> assertEquals("Erros de validacao encontrados", resultado.getMessage()),
                    () -> assertEquals("Codigo de acesso deve ter exatamente 6 digitos numericos",
                            resultado.getErrors().get(0)));
        }

        @Test
        @DisplayName("Quando alteramos o código de acesso do entregador caracteres não numéricos")
        void quandoAlteramosCodigoAcessoDoEntregadorCaracteresNaoNumericos() throws Exception {
            // Arrange
            entregadorPostPutRequestDTO.setCodigo("a*c4e@");

            // Act
            String responseJsonString = driver.perform(put(URI_ENTREGADORES + "/" + entregador.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigo", entregador.getCodigo())
                            .content(objectMapper.writeValueAsString(entregadorPostPutRequestDTO)))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertAll(
                    () -> assertEquals("Erros de validacao encontrados", resultado.getMessage()),
                    () -> assertEquals("Codigo de acesso deve ter exatamente 6 digitos numericos",
                            resultado.getErrors().get(0)));
        }

    }

    @Nested
    @DisplayName("Conjunto de casos de verificação dos fluxos básicos API Rest")
    class EntregadorVerificacaoFluxosBasicosApiRest {

        @Test
        @DisplayName("Quando buscamos por todos entregadores salvos")
        void quandoBuscamosPorTodosEntregadoresSalvos() throws Exception {
            // Arrange
            // Vamos ter 3 entregadores no banco
            Entregador entregador1 = Entregador.builder()
                    .nome("Entregador Dois Almeida")
                    .placaVeiculo("QSL-1234")
                    .tipoVeiculo("Carro")
                    .corVeiculo("Azul")
                    .codigo("246810")
                    .build();
            Entregador entregador2 = Entregador.builder()
                    .nome("Entregador Dois Almeida")
                    .placaVeiculo("ASA-2345")
                    .tipoVeiculo("Moto")
                    .corVeiculo("Preto")
                    .codigo("102340")
                    .build();
            entregadorRespository.saveAll(Arrays.asList(entregador1, entregador2));

            // Act
            String responseJsonString = driver.perform(get(URI_ENTREGADORES)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(entregadorPostPutRequestDTO)))
                    .andExpect(status().isOk()) // Codigo 200
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            List<Entregador> resultado = objectMapper.readValue(responseJsonString, new TypeReference<>() {
            });

            // Assert
            assertAll(
                    () -> assertEquals(3, resultado.size()));
        }

        @Test
        @DisplayName("Quando buscamos um entregador salvo pelo id")
        void quandoBuscamosPorUmEntregadorSalvo() throws Exception {
            // Arrange
            // nenhuma necessidade além do setup()

            // Act
            String responseJsonString = driver.perform(get(URI_ENTREGADORES + "/" + entregador.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(entregadorPostPutRequestDTO)))
                    .andExpect(status().isOk()) // Codigo 200
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            EntregadorResponseDTO resultado = objectMapper.readValue(responseJsonString,
                    new TypeReference<>() {
                    });

            // Assert
            assertAll(
                    () -> assertEquals(entregador.getId().longValue(), resultado.getId().longValue()),
                    () -> assertEquals(entregador.getNome(), resultado.getNome()));
        }

        @Test
        @DisplayName("Quando buscamos um entregador inexistente")
        void quandoBuscamosPorUmEntregadorInexistente() throws Exception {
            // Arrange
            // nenhuma necessidade além do setup()

            // Act
            String responseJsonString = driver.perform(get(URI_ENTREGADORES + "/" + 999999999)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(entregadorPostPutRequestDTO)))
                    .andExpect(status().isBadRequest()) // Codigo 400
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertAll(
                    () -> assertEquals("O entregador consultado nao existe!", resultado.getMessage()));
        }

        @Test
        @DisplayName("Quando criamos um novo entregador com dados válidos")
        void quandoCriarEntregadorValido() throws Exception {
            // Arrange
            // nenhuma necessidade além do setup()

            // Act
            String responseJsonString = driver.perform(post(URI_ENTREGADORES)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(entregadorPostPutRequestDTO)))
                    .andExpect(status().isCreated()) // Codigo 201
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            Entregador resultado = objectMapper.readValue(responseJsonString, Entregador.EntregadorBuilder.class)
                    .build();

            // Assert
            assertAll(
                    () -> assertNotNull(resultado.getId()),
                    () -> assertEquals(entregadorPostPutRequestDTO.getNome(), resultado.getNome()));

        }

        @Test
        @DisplayName("Quando alteramos o entregador com dados válidos")
        void quandoAlteramosEntregadorValido() throws Exception {
            // Arrange
            Long entregadorId = entregador.getId();

            // Act
            String responseJsonString = driver.perform(put(URI_ENTREGADORES + "/" + entregador.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigo", entregador.getCodigo())
                            .content(objectMapper.writeValueAsString(entregadorPostPutRequestDTO)))
                    .andExpect(status().isOk()) // Codigo 200
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            Entregador resultado = objectMapper.readValue(responseJsonString, Entregador.EntregadorBuilder.class)
                    .build();

            // Assert
            assertAll(
                    () -> assertEquals(resultado.getId().longValue(), entregadorId),
                    () -> assertEquals(entregadorPostPutRequestDTO.getNome(), resultado.getNome()));
        }

        @Test
        @DisplayName("Quando alteramos o entregador inexistente")
        void quandoAlteramosEntregadorInexistente() throws Exception {
            // Arrange
            // nenhuma necessidade além do setup()

            // Act
            String responseJsonString = driver.perform(put(URI_ENTREGADORES + "/" + 99999L)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigo", entregador.getCodigo())
                            .content(objectMapper.writeValueAsString(entregadorPostPutRequestDTO)))
                    .andExpect(status().isBadRequest()) // Codigo 400
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertAll(
                    () -> assertEquals("O entregador consultado nao existe!", resultado.getMessage()));
        }

        @Test
        @DisplayName("Quando alteramos o entregador passando código de acesso inválido")
        void quandoAlteramosEntregadorCodigoAcessoInvalido() throws Exception {
            // Arrange
            Long entregadorId = entregador.getId();

            // Act
            String responseJsonString = driver.perform(put(URI_ENTREGADORES + "/" + entregadorId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigo", "invalido")
                            .content(objectMapper.writeValueAsString(entregadorPostPutRequestDTO)))
                    .andExpect(status().isBadRequest()) // Codigo 400
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertAll(
                    () -> assertEquals("Codigo de acesso invalido!", resultado.getMessage()));
        }

        @Test
        @DisplayName("Quando excluímos um entregador salvo")
        void quandoExcluimosEntregadorValido() throws Exception {
            // Arrange
            // nenhuma necessidade além do setup()

            // Act
            String responseJsonString = driver.perform(delete(URI_ENTREGADORES + "/" + entregador.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigo", entregador.getCodigo()))
                    .andExpect(status().isNoContent()) // Codigo 204
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            // Assert
            assertTrue(responseJsonString.isBlank());
        }

        @Test
        @DisplayName("Quando excluímos um entregador inexistente")
        void quandoExcluimosEntregadorInexistente() throws Exception {
            // Arrange
            // nenhuma necessidade além do setup()

            // Act
            String responseJsonString = driver.perform(delete(URI_ENTREGADORES + "/" + 999999)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigo", entregador.getCodigo()))
                    .andExpect(status().isBadRequest()) // Codigo 400
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertAll(
                    () -> assertEquals("O entregador consultado nao existe!", resultado.getMessage()));
        }

        @Test
        @DisplayName("Quando excluímos um entregador salvo passando código de acesso inválido")
        void quandoExcluimosEntregadorCodigoAcessoInvalido() throws Exception {
            // Arrange
            // nenhuma necessidade além do setup()

            // Act
            String responseJsonString = driver.perform(delete(URI_ENTREGADORES + "/" + entregador.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigo", "invalido"))
                    .andExpect(status().isBadRequest()) // Codigo 400
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertAll(
                    () -> assertEquals("Codigo de acesso invalido!", resultado.getMessage()));
        }
    }
}
