package com.ufcg.psoft.commerce.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ufcg.psoft.commerce.dto.entregador.EntregadorResponseDTO;
import com.ufcg.psoft.commerce.dto.fornecedor.*;
import com.ufcg.psoft.commerce.exception.CustomErrorType;
import com.ufcg.psoft.commerce.model.Admin;
import com.ufcg.psoft.commerce.model.Entregador;
import com.ufcg.psoft.commerce.model.Fornecedor;
import com.ufcg.psoft.commerce.repository.AdminRepository;
import com.ufcg.psoft.commerce.repository.EntregadorRespository;
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
@DisplayName("Testes do controller de fornecedores")
public class FornecedorControllerTests {

    final String URI_FORNECEDORES = "/fornecedores";

    @Autowired
    MockMvc driver;

    @Autowired
    FornecedorRepository fornecedorRepository;

    @Autowired
    AdminRepository adminRepository;

    @Autowired
    EntregadorRespository entregadorRespository;

    ObjectMapper objectMapper = new ObjectMapper();

    Fornecedor fornecedor;

    FornecedorPostPutRequestDTO fornecedorPostPutRequestDTO;

    Admin admin;

    Entregador entregador;

    @BeforeEach
    void setup() {
        objectMapper.registerModule(new JavaTimeModule());

        admin = adminRepository.save(Admin.builder()
                .nome("adm")
                .codigo("222222")
                .build());

        entregador = entregadorRespository.save(Entregador.builder()
            .nome("Jose Freitas Fretes")
            .placaVeiculo("QJXDVQ")
            .tipoVeiculo("Caminhão")
            .corVeiculo("Rosa")
            .codigo("123123")
            .build());

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
    }

    @AfterEach
    void clear() {
        fornecedorRepository.deleteAll();
    }

    @Nested
    @DisplayName("Casos de verificação do nome")
    class FornecedorVerificacaoNome {
        @Test
        @DisplayName("Ao recuperar um fornecedor com dados válidos")
        void nomeFornecedorValido() throws Exception {

            String response = driver.perform(get(URI_FORNECEDORES + "/" + fornecedor.getId()))
                                .andExpect(status().isOk())
                                .andDo(print())
                                .andReturn().getResponse().getContentAsString();

            FornecedorResponseDTO resultado = objectMapper.readValue(response, FornecedorResponseDTO.FornecedorResponseDTOBuilder.class).build();

            assertEquals("MicroCoffee", resultado.getNomeEmpresa());
        }

        @Test
        @DisplayName("Alterar nome válido")
        void alteraNomeValido() throws Exception {

            fornecedorPostPutRequestDTO.setNomeEmpresa("Cofflee");

            String response = driver.perform(put(URI_FORNECEDORES + "/" + fornecedor.getId())
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .param("codigo", fornecedor.getCodigo())
                                    .content(objectMapper.writeValueAsString(fornecedorPostPutRequestDTO)))
                                .andExpect(status().isOk())
                                .andDo(print())
                                .andReturn().getResponse().getContentAsString();

            FornecedorResponseDTO resultado = objectMapper.readValue(response, FornecedorResponseDTO.FornecedorResponseDTOBuilder.class).build();

            assertEquals("Cofflee", resultado.getNomeEmpresa());
        }

        @Test
        @DisplayName("Alterar nome nulo")
        void alteraNomeNulo() throws Exception {

            fornecedorPostPutRequestDTO.setNomeEmpresa(null);

            String response = driver.perform(put(URI_FORNECEDORES + "/" + fornecedor.getId())
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .param("codigo", fornecedor.getCodigo())
                                    .content(objectMapper.writeValueAsString(fornecedorPostPutRequestDTO)))
                                .andExpect(status().isBadRequest())
                                .andDo(print())
                                .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(response, CustomErrorType.class);

            assertAll(
                ()-> assertEquals("Erros de validacao encontrados", resultado.getMessage()),
                ()-> assertEquals("Nome da empresa obrigatorio", resultado.getErrors().get(0))
            );
        }

        @Test
        @DisplayName("Alterar nome vazio")
        void alteraNomeVazio() throws Exception {

            fornecedorPostPutRequestDTO.setNomeEmpresa("");

            String response = driver.perform(put(URI_FORNECEDORES + "/" + fornecedor.getId())
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .param("codigo", fornecedor.getCodigo())
                                    .content(objectMapper.writeValueAsString(fornecedorPostPutRequestDTO)))
                                .andExpect(status().isBadRequest())
                                .andDo(print())
                                .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(response, CustomErrorType.class);

            assertAll(
                ()-> assertEquals("Erros de validacao encontrados", resultado.getMessage()),
                ()-> assertEquals("Nome da empresa obrigatorio", resultado.getErrors().get(0))
            );
        }
    }

    @Nested
    @DisplayName("Casos de verificação do CNPJ")
    class FornecedorVerificacaoCnpj {
        @Test
        @DisplayName("Ao recuperar um fornecedor com dados válidos")
        void cnpjFornecedorValido() throws Exception {

            String response = driver.perform(get(URI_FORNECEDORES + "/" + fornecedor.getId()))
                                .andExpect(status().isOk())
                                .andDo(print())
                                .andReturn().getResponse().getContentAsString();

            FornecedorResponseDTO resultado = objectMapper.readValue(response, FornecedorResponseDTO.FornecedorResponseDTOBuilder.class).build();

            assertEquals("12.345.678/0001-22", resultado.getCnpj());
        }

        @Test
        @DisplayName("Alterar CNPJ válido")
        void alteraCnpjValido() throws Exception {

            fornecedorPostPutRequestDTO.setCnpj("12.345.678/0001-23");

            String response = driver.perform(put(URI_FORNECEDORES + "/" + fornecedor.getId())
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .param("codigo", fornecedor.getCodigo())
                                    .content(objectMapper.writeValueAsString(fornecedorPostPutRequestDTO)))
                                .andExpect(status().isOk())
                                .andDo(print())
                                .andReturn().getResponse().getContentAsString();

            FornecedorResponseDTO resultado = objectMapper.readValue(response, FornecedorResponseDTO.FornecedorResponseDTOBuilder.class).build();

            assertEquals("12.345.678/0001-23", resultado.getCnpj());
        }

        @Test
        @DisplayName("Alterar CNPJ nulo")
        void alteraCnpjNulo() throws Exception {

            fornecedorPostPutRequestDTO.setCnpj(null);

            String response = driver.perform(put(URI_FORNECEDORES + "/" + fornecedor.getId())
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .param("codigo", fornecedor.getCodigo())
                                    .content(objectMapper.writeValueAsString(fornecedorPostPutRequestDTO)))
                                .andExpect(status().isBadRequest())
                                .andDo(print())
                                .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(response, CustomErrorType.class);

            assertAll(
                ()-> assertEquals("Erros de validacao encontrados", resultado.getMessage()),
                ()-> assertEquals("CNPJ obrigatorio", resultado.getErrors().get(0))
            );
        }

        @Test
        @DisplayName("Alterar CNPJ vazio")
        void alteraCnpjVazio() throws Exception {

            fornecedorPostPutRequestDTO.setCnpj("");

            String response = driver.perform(put(URI_FORNECEDORES + "/" + fornecedor.getId())
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .param("codigo", fornecedor.getCodigo())
                                    .content(objectMapper.writeValueAsString(fornecedorPostPutRequestDTO)))
                                .andExpect(status().isBadRequest())
                                .andDo(print())
                                .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(response, CustomErrorType.class);

            assertAll(
                ()-> assertEquals("Erros de validacao encontrados", resultado.getMessage()),
                ()-> assertEquals("O CNPJ deve conter digitos e ter o seguinte formato XX.XXX.XXX/XXXX-XX", resultado.getErrors().get(0))
            );
        }

        @Test
        @DisplayName("Formato do cnpj inválido")
        void formatoCnpjInvalido() throws Exception {

            fornecedorPostPutRequestDTO.setCnpj("12345.678/0001-23");

            String response = driver.perform(put(URI_FORNECEDORES + "/" + fornecedor.getId())
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .param("codigo", fornecedor.getCodigo())
                                    .content(objectMapper.writeValueAsString(fornecedorPostPutRequestDTO)))
                                .andExpect(status().isBadRequest())
                                .andDo(print())
                                .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(response, CustomErrorType.class);

            assertAll(
                ()-> assertEquals("Erros de validacao encontrados", resultado.getMessage()),
                ()-> assertEquals("O CNPJ deve conter digitos e ter o seguinte formato XX.XXX.XXX/XXXX-XX", resultado.getErrors().get(0))
            );
        }

        @Test
        @DisplayName("Caracteres do cnpj inválido")
        void caracteresCnpjInvalido() throws Exception {

            fornecedorPostPutRequestDTO.setCnpj("ab.3*ç.678/0001-23");

            String response = driver.perform(put(URI_FORNECEDORES + "/" + fornecedor.getId())
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .param("codigo", fornecedor.getCodigo())
                                    .content(objectMapper.writeValueAsString(fornecedorPostPutRequestDTO)))
                                .andExpect(status().isBadRequest())
                                .andDo(print())
                                .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(response, CustomErrorType.class);

            assertAll(
                ()-> assertEquals("Erros de validacao encontrados", resultado.getMessage()),
                ()-> assertEquals("O CNPJ deve conter digitos e ter o seguinte formato XX.XXX.XXX/XXXX-XX", resultado.getErrors().get(0))
            );
        }
    }

    @Nested
    @DisplayName("Casos de verificação do código de acesso")
    class FornecedorVerificacaoCodigoAcesso {
        @Test
        @DisplayName("Alterar codigo de acesso nulo")
        void alteraCodigoNulo() throws Exception {

            fornecedorPostPutRequestDTO.setCodigo(null);

            String response = driver.perform(put(URI_FORNECEDORES + "/" + fornecedor.getId())
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .param("codigo", fornecedor.getCodigo())
                                    .content(objectMapper.writeValueAsString(fornecedorPostPutRequestDTO)))
                                .andExpect(status().isBadRequest())
                                .andDo(print())
                                .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(response, CustomErrorType.class);

            assertAll(
                ()-> assertEquals("Erros de validacao encontrados", resultado.getMessage()),
                ()-> assertEquals("Codigo de acesso obrigatorio", resultado.getErrors().get(0))
            );
        }

        @Test
        @DisplayName("Código com mais de 6 dígitos")
        void alteraCodigoMais6digitos() throws Exception {

            fornecedorPostPutRequestDTO.setCodigo("1234567");

            String response = driver.perform(put(URI_FORNECEDORES + "/" + fornecedor.getId())
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .param("codigo", fornecedor.getCodigo())
                                    .content(objectMapper.writeValueAsString(fornecedorPostPutRequestDTO)))
                                .andExpect(status().isBadRequest())
                                .andDo(print())
                                .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(response, CustomErrorType.class);

            assertAll(
                ()-> assertEquals("Erros de validacao encontrados", resultado.getMessage()),
                ()-> assertEquals("Codigo de acesso deve ter exatamente 6 digitos numericos", resultado.getErrors().get(0))
            );
        }

        @Test
        @DisplayName("Código com menos de 6 dígitos")
        void alteraCodigoMenos6digitos() throws Exception {

            fornecedorPostPutRequestDTO.setCodigo("12345");

            String response = driver.perform(put(URI_FORNECEDORES + "/" + fornecedor.getId())
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .param("codigo", fornecedor.getCodigo())
                                    .content(objectMapper.writeValueAsString(fornecedorPostPutRequestDTO)))
                                .andExpect(status().isBadRequest())
                                .andDo(print())
                                .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(response, CustomErrorType.class);

            assertAll(
                ()-> assertEquals("Erros de validacao encontrados", resultado.getMessage()),
                ()-> assertEquals("Codigo de acesso deve ter exatamente 6 digitos numericos", resultado.getErrors().get(0))
            );
        }

        @Test
        @DisplayName("Caracteres inválidos no código")
        void caracteresInvalidosCodigo() throws Exception {

            fornecedorPostPutRequestDTO.setCodigo("acj2*/");

            String response = driver.perform(put(URI_FORNECEDORES + "/" + fornecedor.getId())
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .param("codigo", fornecedor.getCodigo())
                                    .content(objectMapper.writeValueAsString(fornecedorPostPutRequestDTO)))
                                .andExpect(status().isBadRequest())
                                .andDo(print())
                                .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(response, CustomErrorType.class);

            assertAll(
                ()-> assertEquals("Erros de validacao encontrados", resultado.getMessage()),
                ()-> assertEquals("Codigo de acesso deve ter exatamente 6 digitos numericos", resultado.getErrors().get(0))
            );
        }
    }

    @Nested
    @DisplayName("Casos de fluxos básicos da API Rest")
    class FornecedorVerificacaoApi {
        @Test
        @DisplayName("Quando buscamos por todos fornecedores salvos")
        void buscaTodosFornecedoresSalvos() throws Exception {

            Fornecedor fornecedor1 = Fornecedor.builder()
                    .nomeEmpresa("Cofflee")
                    .cnpj("12.395.678/0001-23")
                    .codigo("246810")
                    .build();
            Fornecedor fornecedor2 = Fornecedor.builder()
                    .nomeEmpresa("NesCoffee")
                    .cnpj("21.345.678/0001-23")
                    .codigo("135790")
                    .build();
            fornecedorRepository.saveAll(Arrays.asList(fornecedor1, fornecedor2));

            String response = driver.perform(get(URI_FORNECEDORES))
                    .andExpect(status().isOk()) // Codigo 200
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            List<FornecedorResponseDTO> resultado = objectMapper.readValue(response, new TypeReference<>() {});

            assertAll(
                    () -> assertEquals(3, resultado.size())
            );
        }

        @Test
        @DisplayName("Quando buscamos um fornecedor pelo nome")
        void buscaFornecedoresNome() throws Exception {

            Fornecedor fornecedor1 = Fornecedor.builder()
                    .nomeEmpresa("Cofflee")
                    .cnpj("12.395.678/0001-23")
                    .codigo("246810")
                    .build();
            Fornecedor fornecedor2 = Fornecedor.builder()
                    .nomeEmpresa("NesCoffee")
                    .cnpj("21.345.678/0001-23")
                    .codigo("135790")
                    .build();
            fornecedorRepository.saveAll(Arrays.asList(fornecedor1, fornecedor2));

            String response = driver.perform(get(URI_FORNECEDORES)
                            .param("nomeEmpresa", "nesCoff"))
                    .andExpect(status().isOk()) // Codigo 200
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            List<FornecedorResponseDTO> resultado = objectMapper.readValue(response, new TypeReference<>() {});

            assertAll(
                    () -> assertEquals(1, resultado.size())
            );
        }

        @Test
        @DisplayName("Quando buscamos um fornecedor salvo pelo id")
        void buscaUmFornecedorSalvo() throws Exception {

            String response = driver.perform(get(URI_FORNECEDORES + "/" + fornecedor.getId()))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            FornecedorResponseDTO resultado = objectMapper.readValue(response, new TypeReference<>() {});

            assertAll(
                    () -> assertEquals(fornecedor.getId().longValue(), resultado.getId().longValue()),
                    () -> assertEquals(fornecedor.getNomeEmpresa(), resultado.getNomeEmpresa())
            );
        }

        @Test
        @DisplayName("Quando buscamos um fornecedor inexistente")
        void buscaPorUmFornecedorInexistente() throws Exception {

            String responseJsonString = driver.perform(get(URI_FORNECEDORES + "/" + 999999999))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            assertAll(
                    () -> assertEquals("O fornecedor consultado nao existe!", resultado.getMessage())
            );
        }

        @Test
        @DisplayName("Quando criamos um novo fornecedor com dados válidos")
        void criarFornecedorValido() throws Exception {

            String responseJsonString = driver.perform(post(URI_FORNECEDORES)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("adminId", String.valueOf(admin.getId()))
                            .content(objectMapper.writeValueAsString(fornecedorPostPutRequestDTO)))
                    .andExpect(status().isCreated())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            FornecedorResponseDTO resultado = objectMapper.readValue(responseJsonString, FornecedorResponseDTO.FornecedorResponseDTOBuilder.class).build();

            assertAll(
                    () -> assertNotNull(resultado.getId()),
                    () -> assertEquals(fornecedorPostPutRequestDTO.getNomeEmpresa(), resultado.getNomeEmpresa())
            );

        }

        @Test
        @DisplayName("Quando alteramos o fornecedor com dados válidos")
        void alteraFornecedorValido() throws Exception {

            Long fornecedorId = fornecedor.getId();
            fornecedorPostPutRequestDTO.setNomeEmpresa("MicroCoffee2");

            String response = driver.perform(put(URI_FORNECEDORES + "/" + fornecedor.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigo", fornecedor.getCodigo())
                            .content(objectMapper.writeValueAsString(fornecedorPostPutRequestDTO)))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            FornecedorResponseDTO resultado = objectMapper.readValue(response, FornecedorResponseDTO.FornecedorResponseDTOBuilder.class).build();

            assertAll(
                    () -> assertEquals(resultado.getId().longValue(), fornecedorId),
                    () -> assertEquals(fornecedorPostPutRequestDTO.getNomeEmpresa(), resultado.getNomeEmpresa())
            );
        }

        @Test
        @DisplayName("Quando alteramos o fornecedor inexistente")
        void alteramosFornecedorInexistente() throws Exception {

            String response = driver.perform(put(URI_FORNECEDORES + "/" + 99999L)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigo", fornecedor.getCodigo())
                            .content(objectMapper.writeValueAsString(fornecedorPostPutRequestDTO)))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(response, CustomErrorType.class);

            assertAll(
                    () -> assertEquals("O fornecedor consultado nao existe!", resultado.getMessage())
            );
        }

        @Test
        @DisplayName("Quando alteramos o forncedor passando código de acesso inválido")
        void alteraFornecedorCodigoAcessoInvalido() throws Exception {

            Long fornecedorId = fornecedor.getId();

            String response = driver.perform(put(URI_FORNECEDORES + "/" + fornecedorId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigo", "invalido")
                            .content(objectMapper.writeValueAsString(fornecedorPostPutRequestDTO)))
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
        @DisplayName("Quando excluímos um fornecedor salvo")
        void excluiFornecedorValido() throws Exception {

            String response = driver.perform(delete(URI_FORNECEDORES + "/" + fornecedor.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigo", fornecedor.getCodigo()))
                    .andExpect(status().isNoContent())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            assertTrue(response.isBlank());
        }

        @Test
        @DisplayName("Quando excluímos um fornecedor inexistente")
        void excluiFornecedorInexistente() throws Exception {

            String response = driver.perform(delete(URI_FORNECEDORES + "/" + 999999)
                            .contentType(MediaType.APPLICATION_JSON)
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
        @DisplayName("Quando excluímos um fornecedor salvo passando código de acesso inválido")
        void excluiFornecedorCodigoAcessoInvalido() throws Exception {
            String response = driver.perform(delete(URI_FORNECEDORES + "/" + fornecedor.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigo", "invalido"))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(response, CustomErrorType.class);

            assertAll(
                    () -> assertEquals("Codigo de acesso invalido!", resultado.getMessage())
            );
        }

        @Test
        @DisplayName("Quando o fornecedor aprova um entregador valido")
        void aprovaEntregadorValido() throws Exception {
            String response = driver.perform(patch(URI_FORNECEDORES + "/" + fornecedor.getId() + "/aprovaEntregador")
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigo", fornecedor.getCodigo())
                            .param("entregadorId", entregador.getId().toString())
                            .param("aprovado", "true"))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            EntregadorResponseDTO resultado = objectMapper.readValue(response,
                EntregadorResponseDTO.EntregadorResponseDTOBuilder.class).build();

            assertAll(
                () -> assertEquals(true, resultado.isAprovado())
            );
        }

        @Test
        @DisplayName("Quando o fornecedor aprova um entregador invalido")
        void aprovaEntregadorInvalido() throws Exception {
            String response = driver.perform(patch(URI_FORNECEDORES + "/" + fornecedor.getId() + "/aprovaEntregador")
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigo", fornecedor.getCodigo())
                            .param("entregadorId", "9999")
                            .param("aprovado", "true"))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(response, CustomErrorType.class);

            assertAll(
                () -> assertEquals("O entregador consultado nao existe!", resultado.getMessage())
            );
        }

        @Test
        @DisplayName("Quando o fornecedor aprova um entregador com codigo invalido")
        void aprovaEntregadorCodigoAcessoInvalido() throws Exception {
            String response = driver.perform(patch(URI_FORNECEDORES + "/" + fornecedor.getId() + "/aprovaEntregador")
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigo", "invalido")
                            .param("entregadorId", entregador.getId().toString())
                            .param("aprovado", "true"))
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