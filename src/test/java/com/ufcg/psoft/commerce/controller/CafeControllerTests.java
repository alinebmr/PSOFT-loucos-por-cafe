package com.ufcg.psoft.commerce.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ufcg.psoft.commerce.dto.cafe.CafePostPutRequestDTO;
import com.ufcg.psoft.commerce.dto.fornecedor.FornecedorPostPutRequestDTO;
import com.ufcg.psoft.commerce.enums.QualidadeCafe;
import com.ufcg.psoft.commerce.enums.TipoGraoCafe;
import com.ufcg.psoft.commerce.exception.CustomErrorType;
import com.ufcg.psoft.commerce.model.Cafe;
import com.ufcg.psoft.commerce.model.Fornecedor;
import com.ufcg.psoft.commerce.repository.CafeRepository;
import com.ufcg.psoft.commerce.repository.FornecedorRepository;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Testes do controlador de Cafes")

public class CafeControllerTests {
    
    final String URI_CAFES = "/cafes";

    @Autowired
    MockMvc driver;

    @Autowired
    CafeRepository cafeRepository;

    @Autowired
    FornecedorRepository fornecedorRepository;

    ObjectMapper objectMapper = new ObjectMapper();

    Cafe cafe;

    Fornecedor fornecedor;

    CafePostPutRequestDTO cafePostPutRequestDTO;

    FornecedorPostPutRequestDTO fornecedorPostPutRequestDTO;

    @BeforeEach
    void setup() {

        fornecedor = fornecedorRepository.save(Fornecedor.builder()
                .nomeEmpresa("MicroCoffee")
                .cnpj("12.345.678/0001-22")
                .codigo("222222")
                .build());

        fornecedorPostPutRequestDTO = FornecedorPostPutRequestDTO.builder()
                                .nomeEmpresa(fornecedor.getNomeEmpresa())
                                .cnpj(fornecedor.getCnpj())
                                .codigo(fornecedor.getCodigo())
                                .build();

        // Object Mapper suporte para LocalDateTime
        objectMapper.registerModule(new JavaTimeModule());
        cafe = cafeRepository.save(Cafe.builder()
                .idFornecedor(1L)
                .nome("Cafe Muito Bom")
                .origem("Xique-Xique Bahia")
                .tipo(TipoGraoCafe.GRAO)
                .perfil("Frutas Vermelhas")
                .preco(24.99)
                .qualidade(QualidadeCafe.NORMAL)
                .tamanhoEmbalagem(35)
                .build()
        );
        cafePostPutRequestDTO = CafePostPutRequestDTO.builder()
                .idFornecedor(cafe.getIdFornecedor())
                .nome(cafe.getNome())
                .origem(cafe.getOrigem())
                .tipo(cafe.getTipo())
                .perfil(cafe.getPerfil())
                .preco(cafe.getPreco())
                .qualidade(cafe.getQualidade())
                .tamanhoEmbalagem(cafe.getTamanhoEmbalagem())
                .build();
    }

    @AfterEach
    void tearDown() {
        cafeRepository.deleteAll();
    }

    @Nested
    @DisplayName("Conjunto de casos de verificação de nome")
    class CafeVerificacaoNome {

        @Test
        @DisplayName("Quando recuperamos um cafe com dados válidos")
        void quandoRecuperamosNomeDoCafeValido() throws Exception {

            // Act
            String responseJsonString = driver.perform(get(URI_CAFES + "/" + cafe.getId()))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            Cafe resultado = objectMapper.readValue(responseJsonString, Cafe.CafeBuilder.class).build();

            // Assert
            assertEquals("Cafe Muito Bom", resultado.getNome());
        }
        

        @Test
        @DisplayName("Quando alteramos o nome do cafe com dados válidos")
        void quandoAlteramosNomeDoCafeValido() throws Exception {
            // Arrange
            cafePostPutRequestDTO.setNome("Cafe Bom Alterado");

            // Act
            String responseJsonString = driver.perform(put(URI_CAFES + "/" + cafe.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("idFornecedor", cafe.getIdFornecedor().toString())
                            .param("codigo", fornecedor.getCodigo())
                            .content(objectMapper.writeValueAsString(cafePostPutRequestDTO)))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            Cafe resultado = objectMapper.readValue(responseJsonString, Cafe.CafeBuilder.class).build();

            // Assert
            assertEquals("Cafe Bom Alterado", resultado.getNome());
        }

        @Test
        @DisplayName("Quando alteramos o nome do cafe nulo")
        void quandoAlteramosNomeDoCafeNulo() throws Exception {
            // Arrange
            cafePostPutRequestDTO.setNome(null);

            // Act
            String responseJsonString = driver.perform(put(URI_CAFES + "/" + cafe.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("idFornecedor", cafe.getIdFornecedor().toString())
                            .param("codigo", fornecedor.getCodigo())
                            .content(objectMapper.writeValueAsString(cafePostPutRequestDTO)))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertAll(
                    () -> assertEquals("Erros de validacao encontrados", resultado.getMessage()),
                    () -> assertEquals("Nome obrigatorio", resultado.getErrors().get(0))
            );
        }

        @Test
        @DisplayName("Quando alteramos o nome do cafe vazio")
        void quandoAlteramosNomeDoClienteVazio() throws Exception {
            // Arrange
            cafePostPutRequestDTO.setNome("");

            // Act
            String responseJsonString = driver.perform(put(URI_CAFES + "/" + cafe.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("idFornecedor", cafe.getIdFornecedor().toString())
                            .param("codigo", fornecedor.getCodigo())
                            .content(objectMapper.writeValueAsString(cafePostPutRequestDTO)))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertAll(
                    () -> assertEquals("Erros de validacao encontrados", resultado.getMessage()),
                    () -> assertEquals("Nome obrigatorio", resultado.getErrors().get(0))
            );
        }
    }
    
    @Nested
    @DisplayName("Conjunto de casos de verificação de origem")
    class CafeVerificacaoOrigem {

        @Test
        @DisplayName("Quando recuperamos um cafe com dados válidos")
        void quandoRecuperamosOrigemDoCafeValido() throws Exception {

            // Act
            String responseJsonString = driver.perform(get(URI_CAFES + "/" + cafe.getId()))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            Cafe resultado = objectMapper.readValue(responseJsonString, Cafe.CafeBuilder.class).build();

            // Assert
            assertEquals("Xique-Xique Bahia", resultado.getOrigem());
        }
    

        @Test
        @DisplayName("Quando alteramos o nome do cafe com dados válidos")
        void quandoAlteramosOrigemDoCafeValido() throws Exception {
            // Arrange
            cafePostPutRequestDTO.setOrigem("Guarabira Paraiba");
            // Act
            String responseJsonString = driver.perform(put(URI_CAFES + "/" + cafe.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("idFornecedor", cafe.getIdFornecedor().toString())
                            .param("codigo", fornecedor.getCodigo())
                            .content(objectMapper.writeValueAsString(cafePostPutRequestDTO)))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            Cafe resultado = objectMapper.readValue(responseJsonString, Cafe.CafeBuilder.class).build();

            // Assert
            assertEquals("Guarabira Paraiba", resultado.getOrigem());
        }

        @Test
        @DisplayName("Quando alteramos a origem do cafe nulo")
        void quandoAlteramosOrigemDoCafeNulo() throws Exception {
            // Arrange
            cafePostPutRequestDTO.setOrigem(null);

            // Act
            String responseJsonString = driver.perform(put(URI_CAFES + "/" + cafe.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("idFornecedor", cafe.getIdFornecedor().toString())
                            .param("codigo", fornecedor.getCodigo())
                            .content(objectMapper.writeValueAsString(cafePostPutRequestDTO)))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertAll(
                    () -> assertEquals("Erros de validacao encontrados", resultado.getMessage()),
                    () -> assertEquals("Origem obrigatoria", resultado.getErrors().get(0))
            );
        }

        @Test
        @DisplayName("Quando alteramos o nome do cafe vazio")
        void quandoAlteramosOrigemDoCafeVazio() throws Exception {
            // Arrange
            cafePostPutRequestDTO.setOrigem("");

            // Act
            String responseJsonString = driver.perform(put(URI_CAFES + "/" + cafe.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("idFornecedor", cafe.getIdFornecedor().toString())
                            .param("codigo", fornecedor.getCodigo())
                            .content(objectMapper.writeValueAsString(cafePostPutRequestDTO)))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertAll(
                    () -> assertEquals("Erros de validacao encontrados", resultado.getMessage()),
                    () -> assertEquals("Origem obrigatoria", resultado.getErrors().get(0))
            );
        }
    }

    @Nested
    @DisplayName("Conjunto de casos de verificação de tipo")
    class CafeVerificacaoTipo {

        @Test
        @DisplayName("Quando recuperamos um cafe com dados válidos")
        void quandoRecuperamosTipoDoCafeValido() throws Exception {

            // Act
            String responseJsonString = driver.perform(get(URI_CAFES + "/" + cafe.getId()))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            Cafe resultado = objectMapper.readValue(responseJsonString, Cafe.CafeBuilder.class).build();

            // Assert
            assertEquals(TipoGraoCafe.GRAO, resultado.getTipo());
        }
    

        @Test
        @DisplayName("Quando alteramos o tipo do cafe com dados válidos")
        void quandoAlteramosTipoDoCafeValido() throws Exception {
            // Arrange
            cafePostPutRequestDTO.setTipo(TipoGraoCafe.CAPSULA);
            // Act
            String responseJsonString = driver.perform(put(URI_CAFES + "/" + cafe.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("idFornecedor", cafe.getIdFornecedor().toString())
                            .param("codigo", fornecedor.getCodigo())
                            .content(objectMapper.writeValueAsString(cafePostPutRequestDTO)))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            Cafe resultado = objectMapper.readValue(responseJsonString, Cafe.CafeBuilder.class).build();

            // Assert
            assertEquals(TipoGraoCafe.CAPSULA, resultado.getTipo());
        }

        @Test
        @DisplayName("Quando alteramos o tipo do cafe nulo")
        void quandoAlteramosTipoDoCafeNulo() throws Exception {
            // Arrange
            cafePostPutRequestDTO.setTipo(null);

            // Act
            String responseJsonString = driver.perform(put(URI_CAFES + "/" + cafe.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("idFornecedor", cafe.getIdFornecedor().toString())
                            .param("codigo", fornecedor.getCodigo())
                            .content(objectMapper.writeValueAsString(cafePostPutRequestDTO)))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertAll(
                    () -> assertEquals("Erros de validacao encontrados", resultado.getMessage()),
                    () -> assertEquals("Tipo obrigatorio", resultado.getErrors().get(0))
            );
        }

    }
    
    @Nested
    @DisplayName("Conjunto de casos de verificação de perfil")
    class CafeVerificacaoPerfil {

        @Test
        @DisplayName("Quando recuperamos um cafe com dados válidos")
        void quandoRecuperamosPerfilDoCafeValido() throws Exception {

            // Act
            String responseJsonString = driver.perform(get(URI_CAFES + "/" + cafe.getId()))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            Cafe resultado = objectMapper.readValue(responseJsonString, Cafe.CafeBuilder.class).build();

            // Assert
            assertEquals("Frutas Vermelhas", resultado.getPerfil());
        }
    

        @Test
        @DisplayName("Quando alteramos o perfil do cafe com dados válidos")
        void quandoAlteramosPerfilDoCafeValido() throws Exception {
            // Arrange
            cafePostPutRequestDTO.setPerfil("Achocolatado");
            // Act
            String responseJsonString = driver.perform(put(URI_CAFES + "/" + cafe.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("idFornecedor", cafe.getIdFornecedor().toString())
                            .param("codigo", fornecedor.getCodigo())
                            .content(objectMapper.writeValueAsString(cafePostPutRequestDTO)))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            Cafe resultado = objectMapper.readValue(responseJsonString, Cafe.CafeBuilder.class).build();

            // Assert
            assertEquals("Achocolatado", resultado.getPerfil());
        }

        @Test
        @DisplayName("Quando alteramos o perfil do cafe nulo")
        void quandoAlteramosPerfilDoCafeNulo() throws Exception {
            // Arrange
            cafePostPutRequestDTO.setPerfil(null);

            // Act
            String responseJsonString = driver.perform(put(URI_CAFES + "/" + cafe.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("idFornecedor", cafe.getIdFornecedor().toString())
                            .param("codigo", fornecedor.getCodigo())
                            .content(objectMapper.writeValueAsString(cafePostPutRequestDTO)))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertAll(
                    () -> assertEquals("Erros de validacao encontrados", resultado.getMessage()),
                    () -> assertEquals("Perfil sensorial obrigatorio", resultado.getErrors().get(0))
            );
        }

        @Test
        @DisplayName("Quando alteramos o perfil do cafe vazio")
        void quandoAlteramosPerfilDoCafeVazio() throws Exception {
            // Arrange
            cafePostPutRequestDTO.setPerfil("");

            // Act
            String responseJsonString = driver.perform(put(URI_CAFES + "/" + cafe.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("idFornecedor", cafe.getIdFornecedor().toString())
                            .param("codigo", fornecedor.getCodigo())
                            .content(objectMapper.writeValueAsString(cafePostPutRequestDTO)))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertAll(
                    () -> assertEquals("Erros de validacao encontrados", resultado.getMessage()),
                    () -> assertEquals("Perfil sensorial obrigatorio", resultado.getErrors().get(0))
            );
        }
    }

    @Nested
    @DisplayName("Conjunto de casos de verificação de preço")
    class CafeVerificacaoPreco {

        @Test
        @DisplayName("Quando recuperamos um cafe com dados válidos")
        void quandoRecuperamosPrecoDoCafeValido() throws Exception {

            // Act
            String responseJsonString = driver.perform(get(URI_CAFES + "/" + cafe.getId()))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            Cafe resultado = objectMapper.readValue(responseJsonString, Cafe.CafeBuilder.class).build();

            // Assert
            assertEquals(24.99, resultado.getPreco());
        }
    

        @Test
        @DisplayName("Quando alteramos o preco do cafe com dados válidos")
        void quandoAlteramosPrecoDoCafeValido() throws Exception {
            // Arrange
            cafePostPutRequestDTO.setPreco(15.29);
            // Act
            String responseJsonString = driver.perform(put(URI_CAFES + "/" + cafe.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("idFornecedor", cafe.getIdFornecedor().toString())
                            .param("codigo", fornecedor.getCodigo())
                            .content(objectMapper.writeValueAsString(cafePostPutRequestDTO)))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            Cafe resultado = objectMapper.readValue(responseJsonString, Cafe.CafeBuilder.class).build();

            // Assert
            assertEquals(15.29, resultado.getPreco());
        }

        /*@Test
        @DisplayName("Quando alteramos o preco do cafe nulo")
        void quandoAlteramosPrecoDoCafeNulo() throws Exception {
            // Arrange
            cafePostPutRequestDTO.setPreco(null);

            // Act
            String responseJsonString = driver.perform(put(URI_CAFES + "/" + cafe.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("idFornecedor", cafe.getIdFornecedor().toString())
                            .param("codigo", fornecedor.getCodigo())
                            .content(objectMapper.writeValueAsString(cafePostPutRequestDTO)))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertAll(
                    () -> assertEquals("Erros de validacao encontrados", resultado.getMessage()),
                    () -> assertEquals("Preço obrigatorio", resultado.getErrors().get(0))
            );
        }*/

        @Test
        @DisplayName("Quando alteramos o preco do cafe para valores negativos")
        void quandoAlteramosPrecoDoCafeNegativo() throws Exception {
            // Arrange
            cafePostPutRequestDTO.setPreco(-3);

            // Act
            String responseJsonString = driver.perform(put(URI_CAFES + "/" + cafe.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("idFornecedor", cafe.getIdFornecedor().toString())
                            .param("codigo", fornecedor.getCodigo())
                            .content(objectMapper.writeValueAsString(cafePostPutRequestDTO)))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertAll(
                    () -> assertEquals("Erros de validacao encontrados", resultado.getMessage()),
                    () -> assertEquals("Preco deve ser maior que 0", resultado.getErrors().get(0))
            );
        }
    }
}
