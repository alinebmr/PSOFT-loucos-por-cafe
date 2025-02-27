package com.ufcg.psoft.commerce.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ufcg.psoft.commerce.dto.cafe.CafePostPutRequestDTO;
import com.ufcg.psoft.commerce.dto.cafe.CafeResponseDTO;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
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
                .fornecedor(fornecedor)
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

            CafeResponseDTO resultado = objectMapper.readValue(responseJsonString, CafeResponseDTO.CafeResponseDTOBuilder.class).build();

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
                            .param("idFornecedor", fornecedor.getId().toString())
                            .param("codigo", fornecedor.getCodigo())
                            .content(objectMapper.writeValueAsString(cafePostPutRequestDTO)))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CafeResponseDTO resultado = objectMapper.readValue(responseJsonString, CafeResponseDTO.CafeResponseDTOBuilder.class).build();

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
                            .param("idFornecedor", fornecedor.getId().toString())
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
                            .param("idFornecedor", fornecedor.getId().toString())
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

            CafeResponseDTO resultado = objectMapper.readValue(responseJsonString, CafeResponseDTO.CafeResponseDTOBuilder.class).build();

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
                            .param("idFornecedor", fornecedor.getId().toString())
                            .param("codigo", fornecedor.getCodigo())
                            .content(objectMapper.writeValueAsString(cafePostPutRequestDTO)))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CafeResponseDTO resultado = objectMapper.readValue(responseJsonString, CafeResponseDTO.CafeResponseDTOBuilder.class).build();

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
                            .param("idFornecedor", fornecedor.getId().toString())
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
                            .param("idFornecedor", fornecedor.getId().toString())
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

            CafeResponseDTO resultado = objectMapper.readValue(responseJsonString, CafeResponseDTO.CafeResponseDTOBuilder.class).build();

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
                            .param("idFornecedor", fornecedor.getId().toString())
                            .param("codigo", fornecedor.getCodigo())
                            .content(objectMapper.writeValueAsString(cafePostPutRequestDTO)))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CafeResponseDTO resultado = objectMapper.readValue(responseJsonString, CafeResponseDTO.CafeResponseDTOBuilder.class).build();

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
                            .param("idFornecedor", fornecedor.getId().toString())
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

            CafeResponseDTO resultado = objectMapper.readValue(responseJsonString, CafeResponseDTO.CafeResponseDTOBuilder.class).build();

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
                            .param("idFornecedor", fornecedor.getId().toString())
                            .param("codigo", fornecedor.getCodigo())
                            .content(objectMapper.writeValueAsString(cafePostPutRequestDTO)))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CafeResponseDTO resultado = objectMapper.readValue(responseJsonString, CafeResponseDTO.CafeResponseDTOBuilder.class).build();

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
                            .param("idFornecedor", fornecedor.getId().toString())
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
                            .param("idFornecedor", fornecedor.getId().toString())
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

            CafeResponseDTO resultado = objectMapper.readValue(responseJsonString, CafeResponseDTO.CafeResponseDTOBuilder.class).build();

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
                            .param("idFornecedor", fornecedor.getId().toString())
                            .param("codigo", fornecedor.getCodigo())
                            .content(objectMapper.writeValueAsString(cafePostPutRequestDTO)))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CafeResponseDTO resultado = objectMapper.readValue(responseJsonString, CafeResponseDTO.CafeResponseDTOBuilder.class).build();

            // Assert
            assertEquals(15.29, resultado.getPreco());
        }


        @Test
        @DisplayName("Quando alteramos o preco do cafe para valores negativos")
        void quandoAlteramosPrecoDoCafeNegativo() throws Exception {
            // Arrange
            cafePostPutRequestDTO.setPreco(-3);

            // Act
            String responseJsonString = driver.perform(put(URI_CAFES + "/" + cafe.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("idFornecedor", fornecedor.getId().toString())
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

    @Nested
    @DisplayName("Conjunto de casos de verificação de tamanho da embalagem")
    class CafeVerificacaoTamanhoEmbalagem {
        @Test
        @DisplayName("Quando recuperamos o tamanho da embalagem do cafe com dados válidos")
        void quandoRecuperamosTamanhoEmbalagemDoCafeValido() throws Exception {

                // Act
                String responseJsonString = driver.perform(get(URI_CAFES + "/" + cafe.getId()))
                        .andExpect(status().isOk())
                        .andDo(print())
                        .andReturn().getResponse().getContentAsString();

                CafeResponseDTO resultado = objectMapper.readValue(responseJsonString, CafeResponseDTO.CafeResponseDTOBuilder.class).build();

                // Assert
                assertEquals(35, resultado.getTamanhoEmbalagem());
        }


        @Test
        @DisplayName("Quando alteramos o tamanho da embalagem do cafe com dados válidos")
        void quandoAlteramoTamanhoEmbalagemDoCafeValido() throws Exception {
            // Arrange
            cafePostPutRequestDTO.setTamanhoEmbalagem(15);

            // Act
            String responseJsonString = driver.perform(put(URI_CAFES + "/" + cafe.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("idFornecedor", fornecedor.getId().toString())
                            .param("codigo", fornecedor.getCodigo())
                            .content(objectMapper.writeValueAsString(cafePostPutRequestDTO)))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CafeResponseDTO resultado = objectMapper.readValue(responseJsonString, CafeResponseDTO.CafeResponseDTOBuilder.class).build();

            // Assert
            assertEquals(15, resultado.getTamanhoEmbalagem());
        }

        @Test
        @DisplayName("Quando alteramos o tamanho da embalagem do cafe para valores negativos")
        void quandoAlteramosTamanhoEmbalagemoDoCafeNegativo() throws Exception {
            // Arrange
            cafePostPutRequestDTO.setTamanhoEmbalagem(-3);

            // Act
            String responseJsonString = driver.perform(put(URI_CAFES + "/" + cafe.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("idFornecedor", fornecedor.getId().toString())
                            .param("codigo", fornecedor.getCodigo())
                            .content(objectMapper.writeValueAsString(cafePostPutRequestDTO)))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertAll(
                    () -> assertEquals("Erros de validacao encontrados", resultado.getMessage()),
                    () -> assertEquals("Tamanho da embalagem deve ser maior que 0", resultado.getErrors().get(0))
            );
        }
    }

    @Nested
    @DisplayName("Conjunto de casos de verificação de qualidade")
    class CafeVerificacaoQualidade {
        @Test
        @DisplayName("Quando recuperamos a qualidade do cafe com dados válidos")
        void quandoRecuperamosQualidadeDoCafeValido() throws Exception {

                // Act
                String responseJsonString = driver.perform(get(URI_CAFES + "/" + cafe.getId()))
                        .andExpect(status().isOk())
                        .andDo(print())
                        .andReturn().getResponse().getContentAsString();

                CafeResponseDTO resultado = objectMapper.readValue(responseJsonString, CafeResponseDTO.CafeResponseDTOBuilder.class).build();

                // Assert
                assertEquals(QualidadeCafe.NORMAL, resultado.getQualidade());
        }


        @Test
        @DisplayName("Quando alteramos a qualidade do cafe com dados válidos")
        void quandoAlteramoQualidadeDoCafeValido() throws Exception {
            // Arrange
            cafePostPutRequestDTO.setQualidade(QualidadeCafe.PREMIUM);
            // Act
            String responseJsonString = driver.perform(put(URI_CAFES + "/" + cafe.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("idFornecedor", fornecedor.getId().toString())
                            .param("codigo", fornecedor.getCodigo())
                            .content(objectMapper.writeValueAsString(cafePostPutRequestDTO)))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CafeResponseDTO resultado = objectMapper.readValue(responseJsonString, CafeResponseDTO.CafeResponseDTOBuilder.class).build();

            // Assert
            assertEquals(QualidadeCafe.PREMIUM, resultado.getQualidade());
        }

        @Test
        @DisplayName("Quando alteramos a qualidade do cafe para nulo")
        void quandoAlteramosQualidadeoDoCafeNulo() throws Exception {
            // Arrange
            cafePostPutRequestDTO.setQualidade(null);

            // Act
            String responseJsonString = driver.perform(put(URI_CAFES + "/" + cafe.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("idFornecedor", fornecedor.getId().toString())
                            .param("codigo", fornecedor.getCodigo())
                            .content(objectMapper.writeValueAsString(cafePostPutRequestDTO)))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertAll(
                    () -> assertEquals("Erros de validacao encontrados", resultado.getMessage()),
                    () -> assertEquals("Qualidade obrigatoria", resultado.getErrors().get(0))
            );
        }
    }

    @Nested
    @DisplayName("Conjunto de casos de verificação de disponibilidade")
    class CafeVerificacaoDisponibilidade {
        @Test
        @DisplayName("Quando recuperamos a disponibilidade do cafe com dados válidos")
        void quandoRecuperamosDisponibilidadeCafeValido() throws Exception {

                // Act
                String responseJsonString = driver.perform(get(URI_CAFES + "/" + cafe.getId()))
                        .andExpect(status().isOk())
                        .andDo(print())
                        .andReturn().getResponse().getContentAsString();

                CafeResponseDTO resultado = objectMapper.readValue(responseJsonString, CafeResponseDTO.CafeResponseDTOBuilder.class).build();

                // Assert
                assertEquals(true, resultado.isDisponivel());
        }
    }

    @Nested
    @DisplayName("Conjunto de casos de verificação dos fluxos básicos API Rest")
    class CafeVerificacaoFluxosBasicosApiRest {

        @Test
        @DisplayName("Quando buscamos por todos cafes salvos")
        void quandoBuscamosPorTodosCafesSalvos() throws Exception {
            // Arrange
            // Vamos ter 3 clientes no banco
            Cafe cafe1 = Cafe.builder()
                    .fornecedor(fornecedor)
                    .nome("Cafe exemplo1")
                    .origem("Campina Grande Paraiba")
                    .tipo(TipoGraoCafe.CAPSULA)
                    .perfil("Frutas vermelhas")
                    .preco(19.99)
                    .qualidade(QualidadeCafe.NORMAL)
                    .tamanhoEmbalagem(10)
                    .build();
           Cafe cafe2 = Cafe.builder()
                    .fornecedor(fornecedor)
                    .nome("Cafe exemplo2")
                    .origem("Campina Grande Paraiba")
                    .tipo(TipoGraoCafe.CAPSULA)
                    .perfil("Frutas vermelhas")
                    .preco(19.99)
                    .qualidade(QualidadeCafe.NORMAL)
                    .tamanhoEmbalagem(10)
                    .build();
            cafeRepository.saveAll(Arrays.asList(cafe1, cafe2));

            // Act
            String responseJsonString = driver.perform(get(URI_CAFES)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(cafePostPutRequestDTO)))
                    .andExpect(status().isOk()) // Codigo 200
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            List<CafeResponseDTO> resultado = objectMapper.readValue(responseJsonString, new TypeReference<>() {
            });

            // Assert
            assertAll(
                    () -> assertEquals(3, resultado.size())
            );
        }

        @Test
        @DisplayName("Quando buscamos cafe por id")
        void quandoBuscamosCafePorId() throws Exception {
            // Arrange
            // Vamos ter 3 clientes no banco
            Cafe cafe1 = Cafe.builder()
                    .fornecedor(fornecedor)
                    .nome("Cafe exemplo1")
                    .origem("Campina Grande Paraiba")
                    .tipo(TipoGraoCafe.CAPSULA)
                    .perfil("Frutas vermelhas")
                    .preco(19.99)
                    .qualidade(QualidadeCafe.NORMAL)
                    .tamanhoEmbalagem(10)
                    .build();
            cafeRepository.save(cafe1);

            // Act
            String response = driver.perform(get(URI_CAFES + "/" + cafe1.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(cafePostPutRequestDTO)))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();
            CafeResponseDTO resultado = objectMapper.readValue(response, new TypeReference<>() {});

            assertAll(
                    () -> assertEquals(cafe1.getId().longValue(), resultado.getId().longValue()),
                    () -> assertEquals(cafe1.getNome(), resultado.getNome())
            );
        }

        @Test
        @DisplayName("Quando buscamos um cafe inexistente")
        void buscaPorUmCafeInexistente() throws Exception {

            String responseJsonString = driver.perform(get(URI_CAFES + "/" + 999999999)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(cafePostPutRequestDTO)))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            assertAll(
                    () -> assertEquals("O cafe consultado nao existe!", resultado.getMessage())
            );
        }

        @Test
        @DisplayName("Quando criamos um novo cafe com dados válidos")
        void criarCafeValido() throws Exception {

            String responseJsonString = driver.perform(post(URI_CAFES)
                            .param("idFornecedor", fornecedor.getId().toString())
                            .param("codigoAcesso", fornecedor.getCodigo())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(cafePostPutRequestDTO)))
                    .andExpect(status().isCreated())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CafeResponseDTO resultado = objectMapper.readValue(responseJsonString, CafeResponseDTO.CafeResponseDTOBuilder.class).build();

            assertAll(
                    () -> assertNotNull(cafe.getId()),
                    () -> assertEquals(cafePostPutRequestDTO.getNome(), resultado.getNome())
            );

        }

        @Test
        @DisplayName("Quando alteramos o cafe com dados válidos")
        void quandoAlteramosCafeValido() throws Exception {

            // Act
            String responseJsonString = driver.perform(put(URI_CAFES + "/" + cafe.getId())
                            .param("idFornecedor", fornecedor.getId().toString())
                            .param("codigo", fornecedor.getCodigo())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(cafePostPutRequestDTO)))
                    .andExpect(status().isOk()) // Codigo 200
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CafeResponseDTO resultado = objectMapper.readValue(responseJsonString, CafeResponseDTO.CafeResponseDTOBuilder.class).build();

            // Assert
            assertAll(
                    () -> assertEquals(resultado.getId().longValue(), cafe.getId()),
                    () -> assertEquals(cafePostPutRequestDTO.getNome(), resultado.getNome())
            );
        }


        @Test
        @DisplayName("Quando alteramos o cafe inexistente")
        void alteramosCafeInexistente() throws Exception {

            String response = driver.perform(put(URI_CAFES + "/" + 99999L)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("idFornecedor", fornecedor.getId().toString())
                            .param("codigo", fornecedor.getCodigo())
                            .content(objectMapper.writeValueAsString(cafePostPutRequestDTO)))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(response, CustomErrorType.class);

            assertAll(
                    () -> assertEquals("O cafe consultado nao existe!", resultado.getMessage())
            );
        }

        @Test
        @DisplayName("Quando alteramos o cafe passando id de fornecedor inválido")
        void alteraCafeIdFornecedorInvalido() throws Exception {

            Long cafeId = cafe.getId();

            String response = driver.perform(put(URI_CAFES + "/" + cafeId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("idFornecedor", "9999")
                            .param("codigo", fornecedor.getCodigo())
                            .content(objectMapper.writeValueAsString(cafePostPutRequestDTO)))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(response, CustomErrorType.class);

            // Assert
            assertAll(
                    () -> assertEquals("O fornecedor consultado nao existe!", resultado.getMessage())
            );
        }

        @Test
        @DisplayName("Quando alteramos o cafe passando codigo de acesso de fornecedor inválido")
        void alteraCafeCodigoAcessoInvalido() throws Exception {

            Long cafeId = cafe.getId();

            String response = driver.perform(put(URI_CAFES + "/" + cafeId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("idFornecedor", fornecedor.getId().toString())
                            .param("codigo", "invalido")
                            .content(objectMapper.writeValueAsString(cafePostPutRequestDTO)))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(response, CustomErrorType.class);

            // Assert
            assertAll(
                    () -> assertEquals("Codigo de acesso invalido!", resultado.getMessage())
            );
        }

        @Test
        @DisplayName("Quando excluímos um cafe salvo")
        void excluiCafeValido() throws Exception {

            String response = driver.perform(delete(URI_CAFES + "/" + cafe.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("idFornecedor", fornecedor.getId().toString())
                            .param("codigo", fornecedor.getCodigo()))
                    .andExpect(status().isNoContent())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            assertTrue(response.isBlank());
        }

        @Test
        @DisplayName("Quando excluímos um cafe inexistente")
        void excluiCafeInexistente() throws Exception {

            String response = driver.perform(delete(URI_CAFES + "/" + 999999)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("idFornecedor", fornecedor.getId().toString())
                            .param("codigo", fornecedor.getCodigo()))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(response, CustomErrorType.class);

            assertAll(
                    () -> assertEquals("O cafe consultado nao existe!", resultado.getMessage())
            );
        }

        @Test
        @DisplayName("Quando excluímos um cafe salvo passando id de fornecedor inválido")
        void excluiCafeIdFornecedorInvalido() throws Exception {

            String response = driver.perform(delete(URI_CAFES + "/" + cafe.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("idFornecedor", "9999")
                            .param("codigo", fornecedor.getCodigo()))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(response, CustomErrorType.class);

            assertAll(
                    () -> assertEquals("O fornecedor consultado nao existe!", resultado.getMessage())
            );
        }

        @Test
        @DisplayName("Quando excluímos um cafe salvo passando codigo de acesso inválido")
        void excluiCafeCodigoAcessoInvalido() throws Exception {

            String response = driver.perform(delete(URI_CAFES + "/" + cafe.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("idFornecedor", fornecedor.getId().toString())
                            .param("codigo", "invalido"))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(response, CustomErrorType.class);

            assertAll(
                    () -> assertEquals("Codigo de acesso invalido!", resultado.getMessage())
            );
        }
    }
}

