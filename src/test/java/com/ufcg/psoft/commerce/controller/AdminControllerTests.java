package com.ufcg.psoft.commerce.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ufcg.psoft.commerce.dto.admin.AdminPostPutRequestDTO;
import com.ufcg.psoft.commerce.dto.admin.AdminResponseDTO;
import com.ufcg.psoft.commerce.exception.CustomErrorType;
import com.ufcg.psoft.commerce.model.Admin;
import com.ufcg.psoft.commerce.repository.AdminRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@DisplayName("Testes do controlador de administrador")
public class AdminControllerTests {

    final String URI_ADMINS = "/admins";

    @Autowired
    MockMvc driver;

    @Autowired
    AdminRepository adminRepository;

    ObjectMapper objectMapper = new ObjectMapper();

    Admin admin;

    AdminPostPutRequestDTO adminPostPutRequestDTO;

    @BeforeEach
    void setup() {
        // Object Mapper suporte para LocalDateTime
        objectMapper.registerModule(new JavaTimeModule());

        admin = adminRepository.save(Admin.builder()
                .nome("ademiro")
                .codigo("123123")
                .build());

        adminPostPutRequestDTO = AdminPostPutRequestDTO.builder()
                .nome(admin.getNome())
                .codigo(admin.getCodigo())
                .build();
    }

    @AfterEach
    void tearDown() {
        adminRepository.deleteAll();
    }

    @Nested
    @DisplayName("Conjunto de casos de verificação de nome")
    class AdminVerificacaoNome {

        @Test
        @DisplayName("Quando recuperamos um administrador com dados válidos")
        void quandoRecuperamosNomeDoAdminValido() throws Exception {

            // Act
            String responseJsonString = driver.perform(get(URI_ADMINS + "/" + admin.getId()))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            AdminResponseDTO resultado = objectMapper.readValue(responseJsonString, new TypeReference<>() {});

            // Assert
            assertEquals("ademiro", resultado.getNome());
        }

        @Test
        @DisplayName("Quando alteramos o nome do administrador com dados válidos")
        void quandoAlteramosNomeDoAdminValido() throws Exception {
            // Arrange
            adminPostPutRequestDTO.setNome("ad");

            // Act
            String responseJsonString = driver.perform(put(URI_ADMINS + "/" + admin.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigo", admin.getCodigo())
                            .content(objectMapper.writeValueAsString(adminPostPutRequestDTO)))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            AdminResponseDTO resultado = objectMapper.readValue(responseJsonString, new TypeReference<>() {});

            // Assert
            assertEquals("ad", resultado.getNome());
        }

        @Test
        @DisplayName("Quando alteramos o nome do administrador nulo")
        void quandoAlteramosNomeDoAdminNulo() throws Exception {
            // Arrange
            adminPostPutRequestDTO.setNome(null);

            // Act
            String responseJsonString = driver.perform(put(URI_ADMINS + "/" + admin.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigo", admin.getCodigo())
                            .content(objectMapper.writeValueAsString(adminPostPutRequestDTO)))
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
        @DisplayName("Quando alteramos o nome do administrador vazio")
        void quandoAlteramosNomeDoAdminVazio() throws Exception {
            // Arrange
            adminPostPutRequestDTO.setNome("");

            // Act
            String responseJsonString = driver.perform(put(URI_ADMINS + "/" + admin.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigo", admin.getCodigo())
                            .content(objectMapper.writeValueAsString(adminPostPutRequestDTO)))
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
    @DisplayName("Conjunto de casos de verificação do código de acesso")
    class AdminVerificacaoCodigoAcesso {

        @Test
        @DisplayName("Quando alteramos o código de acesso do administrador nulo")
        void quandoAlteramosCodigoAcessoDoAdminNulo() throws Exception {

            adminPostPutRequestDTO.setCodigo(null);

            String responseJsonString = driver.perform(put(URI_ADMINS + "/" + admin.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigo", admin.getCodigo())
                            .content(objectMapper.writeValueAsString(adminPostPutRequestDTO)))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertAll(
                    () -> assertEquals("Erros de validacao encontrados", resultado.getMessage()),
                    () -> assertEquals("Codigo de acesso obrigatorio", resultado.getErrors().get(0))
            );
        }

        @Test
        @DisplayName("Quando alteramos o código de acesso do administrador mais de 6 digitos")
        void quandoAlteramosCodigoAcessoDoAdminMaisDe6Digitos() throws Exception {

            adminPostPutRequestDTO.setCodigo("1234567");

            String responseJsonString = driver.perform(put(URI_ADMINS + "/" + admin.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigo", admin.getCodigo())
                            .content(objectMapper.writeValueAsString(adminPostPutRequestDTO)))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertAll(
                    () -> assertEquals("Erros de validacao encontrados", resultado.getMessage()),
                    () -> assertEquals("Codigo de acesso deve ter exatamente 6 digitos numericos", resultado.getErrors().get(0))
            );
        }

        @Test
        @DisplayName("Quando alteramos o código de acesso do administrador menos de 6 digitos")
        void quandoAlteramosCodigoAcessoDoAdminMenosDe6Digitos() throws Exception {

            adminPostPutRequestDTO.setCodigo("12345");

            String responseJsonString = driver.perform(put(URI_ADMINS + "/" + admin.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigo", admin.getCodigo())
                            .content(objectMapper.writeValueAsString(adminPostPutRequestDTO)))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertAll(
                    () -> assertEquals("Erros de validacao encontrados", resultado.getMessage()),
                    () -> assertEquals("Codigo de acesso deve ter exatamente 6 digitos numericos", resultado.getErrors().get(0))
            );
        }

        @Test
        @DisplayName("Quando alteramos o código de acesso do administrador caracteres não numéricos")
        void quandoAlteramosCodigoAcessoDoAdminCaracteresNaoNumericos() throws Exception {

            adminPostPutRequestDTO.setCodigo("a*c4e@");

            String responseJsonString = driver.perform(put(URI_ADMINS + "/" + admin.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigo", admin.getCodigo())
                            .content(objectMapper.writeValueAsString(adminPostPutRequestDTO)))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertAll(
                    () -> assertEquals("Erros de validacao encontrados", resultado.getMessage()),
                    () -> assertEquals("Codigo de acesso deve ter exatamente 6 digitos numericos", resultado.getErrors().get(0))
            );
        }
    }

    @Nested
    @DisplayName("Conjunto de casos de verificação dos fluxos básicos API Rest")
    class AdminVerificacaoFluxosBasicosApiRest {

        @Test
        @DisplayName("Quando buscamos por todos administradores salvos")
        void quandoBuscamosPorTodosAdminSalvos() throws Exception {
            adminRepository.save(Admin.builder()
                    .nome("ad")
                    .codigo("123123")
                    .build());

            
            String responseJsonString = driver.perform(get(URI_ADMINS)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(adminPostPutRequestDTO)))
                    .andExpect(status().isOk()) // Codigo 200
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            List<Admin> resultado = objectMapper.readValue(responseJsonString, new TypeReference<>() {
            });

            // Assert
            assertAll(
                    () -> assertEquals(2, resultado.size())
            );
        }

        @Test
        @DisplayName("Quando buscamos um administrador por nome")
        void quandoBuscamosAdminsPorNome() throws Exception {
            adminRepository.save(Admin.builder()
                    .nome("ad")
                    .codigo("123123")
                    .build());

            // Act
            String responseJsonString = driver.perform(get(URI_ADMINS)
                            .param("nome", "ad")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(adminPostPutRequestDTO)))
                    .andExpect(status().isOk()) // Codigo 200
                    .andReturn().getResponse().getContentAsString();

            List<Admin> resultado = objectMapper.readValue(responseJsonString, new TypeReference<>() {});

            // Assert
            assertAll(
                    () -> assertEquals(2, resultado.size())
            );
        }

        @Test
        @DisplayName("Quando buscamos administradores por nome inexistente")
        void quandoBuscamosAdminsPorNomeInexistente() throws Exception {

            String responseJsonString = driver.perform(get(URI_ADMINS)
                            .param("nome", "Marcos")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(adminPostPutRequestDTO)))
                    .andExpect(status().isOk()) // Codigo 200
                    .andReturn().getResponse().getContentAsString();

            List<Admin> resultado = objectMapper.readValue(responseJsonString, new TypeReference<>() {});

            // Assert
            assertAll(
                    () -> assertEquals(0, resultado.size())
            );
        }

        @Test
        @DisplayName("Quando buscamos um administrador salvo pelo id")
        void quandoBuscamosPorUmAdminSalvo() throws Exception {

            String responseJsonString = driver.perform(get(URI_ADMINS + "/" + admin.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(adminPostPutRequestDTO)))
                    .andExpect(status().isOk()) // Codigo 200
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            AdminResponseDTO resultado = objectMapper.readValue(responseJsonString, new TypeReference<>() {});

            // Assert
            assertAll(
                    () -> assertEquals(admin.getId().longValue(), resultado.getId().longValue()),
                    () -> assertEquals(admin.getNome(), resultado.getNome())
            );
        }

        @Test
        @DisplayName("Quando buscamos um administrador inexistente")
        void quandoBuscamosPorUmAdminInexistente() throws Exception {

            String responseJsonString = driver.perform(get(URI_ADMINS + "/" + 999999999)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(adminPostPutRequestDTO)))
                    .andExpect(status().isBadRequest()) // Codigo 400
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertAll(
                    () -> assertEquals("O administrador consultado nao existe!", resultado.getMessage())
            );
        }

        @Test
        @DisplayName("Quando criamos um novo administrador com dados válidos")
        void quandoCriarAdminValido() throws Exception {

            String responseJsonString = driver.perform(post(URI_ADMINS)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(adminPostPutRequestDTO)))
                    .andExpect(status().isCreated()) // Codigo 201
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            AdminResponseDTO resultado = objectMapper.readValue(responseJsonString, new TypeReference<>() {});

            // Assert
            assertAll(
                    () -> assertNotNull(resultado.getId()),
                    () -> assertEquals(adminPostPutRequestDTO.getNome(), resultado.getNome())
            );
        }

        @Test
        @DisplayName("Quando alteramos o administrador com dados válidos")
        void quandoAlteramosAdminValido() throws Exception {

            Long adminId = admin.getId();

            String responseJsonString = driver.perform(put(URI_ADMINS + "/" + admin.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigo", admin.getCodigo())
                            .content(objectMapper.writeValueAsString(adminPostPutRequestDTO)))
                    .andExpect(status().isOk()) // Codigo 200
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            AdminResponseDTO resultado = objectMapper.readValue(responseJsonString, new TypeReference<>() {});

            // Assert
            assertAll(
                    () -> assertEquals(resultado.getId().longValue(), adminId),
                    () -> assertEquals(adminPostPutRequestDTO.getNome(), resultado.getNome())
            );
        }

        @Test
        @DisplayName("Quando alteramos o admin inexistente")
        void quandoAlteramosAdminInexistente() throws Exception {

            String responseJsonString = driver.perform(put(URI_ADMINS + "/" + 99999L)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigo", admin.getCodigo())
                            .content(objectMapper.writeValueAsString(adminPostPutRequestDTO)))
                    .andExpect(status().isBadRequest()) // Codigo 400
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertAll(
                    () -> assertEquals("O administrador consultado nao existe!", resultado.getMessage())
            );
        }

        @Test
        @DisplayName("Quando alteramos o administrador passando código de acesso inválido")
        void quandoAlteramosAdminCodigoAcessoInvalido() throws Exception {

            String responseJsonString = driver.perform(put(URI_ADMINS + "/" + admin.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigo", "invalido")
                            .content(objectMapper.writeValueAsString(adminPostPutRequestDTO)))
                    .andExpect(status().isBadRequest()) // Codigo 400
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertAll(
                    () -> assertEquals("Codigo de acesso invalido!", resultado.getMessage())
            );
        }

        @Test
        @DisplayName("Quando excluímos um administrador salvo")
        void quandoExcluimosAdminValido() throws Exception {

            String responseJsonString = driver.perform(delete(URI_ADMINS + "/" + admin.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigo", admin.getCodigo()))
                    .andExpect(status().isNoContent()) // Codigo 204
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            // Assert
            assertTrue(responseJsonString.isBlank());
        }

        @Test
        @DisplayName("Quando excluímos um administrador inexistente")
        void quandoExcluimosAdminInexistente() throws Exception {

            String responseJsonString = driver.perform(delete(URI_ADMINS + "/" + 999999)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigo", admin.getCodigo()))
                    .andExpect(status().isBadRequest()) // Codigo 400
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertAll(
                    () -> assertEquals("O administrador consultado nao existe!", resultado.getMessage())
            );
        }

        @Test
        @DisplayName("Quando excluímos um administrador salvo passando código de acesso inválido")
        void quandoExcluimosAdminCodigoAcessoInvalido() throws Exception {

            String responseJsonString = driver.perform(delete(URI_ADMINS + "/" + admin.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigo", "invalido"))
                    .andExpect(status().isBadRequest()) // Codigo 400
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertAll(
                    () -> assertEquals("Codigo de acesso invalido!", resultado.getMessage())
            );
        }
    }
}