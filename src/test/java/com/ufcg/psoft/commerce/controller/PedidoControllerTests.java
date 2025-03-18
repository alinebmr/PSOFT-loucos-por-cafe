package com.ufcg.psoft.commerce.controller;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import com.ufcg.psoft.commerce.dto.entregador.EntregadorPostPutRequestDTO;
import com.ufcg.psoft.commerce.enums.*;
import com.ufcg.psoft.commerce.model.*;
import com.ufcg.psoft.commerce.repository.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ufcg.psoft.commerce.dto.EnderecoDTO;
import com.ufcg.psoft.commerce.dto.pedido.PedidoPostPutRequestDTO;
import com.ufcg.psoft.commerce.dto.pedido.PedidoResponseDTO;
import com.ufcg.psoft.commerce.exception.CustomErrorType;
import com.ufcg.psoft.commerce.service.pedido.PedidoService;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@DisplayName("Testes do controlador de Pedidos")
public class PedidoControllerTests {
    final String URI_PEDIDOS = "/pedidos";

    @Autowired
    MockMvc driver;

    @Autowired
    CafeRepository cafeRepository;

    @Autowired
    FornecedorRepository fornecedorRepository;

    @Autowired
    ClienteRepository clienteRepository;

    @Autowired
    PedidoRepository pedidoRepository;

    @Autowired
    PedidoService pedidoService;

    @Autowired
    EntregadorRespository entregadorRespository;

    ObjectMapper objectMapper = new ObjectMapper();

    Cafe cafe;
    Cafe cafePremium;
    Cafe cafeIndisponivel;

    Entregador entregador;

    Fornecedor fornecedor;
    Fornecedor fornecedorOutro;

    Cliente cliente;
    Cliente clientePremium;

    Pedido pedido;

    PedidoPostPutRequestDTO pedidoPostPutRequestDTO;

    EntregadorPostPutRequestDTO entregadorPostPutRequestDTO;

    @BeforeEach
    void setup() {
        // Object Mapper suporte para LocalDateTime
        objectMapper.registerModule(new JavaTimeModule());

        cliente = clienteRepository.save(Cliente.builder()
            .nome("José")
            .codigo("123123")
            .assinatura(TipoAssinatura.NORMAL)
            .endereco(Endereco.builder()
                .cep("12345678")
                .bairro("Um lugar aí")
                .cidade("Uma cidade aí")
                .rua("Avenida Qualquer")
                .numero("15")
                .build())
            .build());

        clientePremium = clienteRepository.save(Cliente.builder()
            .nome("Paulo")
            .codigo("123123")
            .assinatura(TipoAssinatura.PREMIUM)
            .endereco(Endereco.builder()
                .cep("44440000")
                .bairro("Centro")
                .cidade("Cidade Grande")
                .rua("Rua central")
                .numero("123")
                .build())
            .build());

        fornecedor = fornecedorRepository.save(Fornecedor.builder()
            .nomeEmpresa("MicroCoffee")
            .cnpj("12.345.678/0001-22")
            .codigo("222222")
            .build());
        fornecedor.getTiposPagamento().add(TipoPagamento.PIX);
        fornecedor.getTiposPagamento().add(TipoPagamento.DEBITO);
        fornecedor.getTiposPagamento().add(TipoPagamento.CREDITO);
        fornecedor = fornecedorRepository.save(fornecedor);

        fornecedorOutro = fornecedorRepository.save(Fornecedor.builder()
            .nomeEmpresa("Joaninha Cafe")
            .cnpj("44.000.000/4444-22")
            .codigo("123123")
            .build());

        cafe = cafeRepository.save(Cafe.builder()
            .fornecedor(fornecedor)
            .nome("Cafe Muito Bom")
            .origem("Xique-Xique Bahia")
            .tipo(TipoGraoCafe.GRAO)
            .perfil("Frutas Vermelhas")
            .preco(25.0)
            .qualidade(QualidadeCafe.NORMAL)
            .tamanhoEmbalagem(35)
            .build());

        cafePremium = cafeRepository.save(Cafe.builder()
            .fornecedor(fornecedor)
            .nome("Chococcino")
            .origem("Willy Wonka")
            .tipo(TipoGraoCafe.CAPSULA)
            .perfil("Cremoso")
            .preco(39.99)
            .qualidade(QualidadeCafe.PREMIUM)
            .tamanhoEmbalagem(35)
            .build());

        cafeIndisponivel = cafeRepository.save(Cafe.builder()
            .fornecedor(fornecedorOutro)
            .nome("Cafe raro himalaia")
            .origem("Cavernas")
            .tipo(TipoGraoCafe.GRAO)
            .perfil("Cremoso")
            .preco(39.99)
            .qualidade(QualidadeCafe.NORMAL)
            .tamanhoEmbalagem(35)
            .disponivel(false)
            .build());

        entregador = entregadorRespository.save(Entregador.builder()
                .nome("Leticia Fretes")
                .placaVeiculo("AAA-2345")
                .tipoVeiculo("Caminhão")
                .corVeiculo("Verde")
                .codigo("123123")
                .aprovado(false)
                .build()
        );

        entregadorPostPutRequestDTO = EntregadorPostPutRequestDTO.builder()
                .nome(entregador.getNome())
                .placaVeiculo(entregador.getPlacaVeiculo())
                .tipoVeiculo(entregador.getTipoVeiculo())
                .corVeiculo(entregador.getCorVeiculo())
                .codigo(entregador.getCodigo())
                .build();

        pedido = pedidoRepository.save(Pedido.builder()
            .cafe(cafe)
            .endereco(cliente.getEndereco())
            .cliente(cliente)
            .tipoPagamento(TipoPagamento.CREDITO)
            .build());

        pedidoPostPutRequestDTO = PedidoPostPutRequestDTO.builder()
            .endereco(null)
            .idCafe(cafe.getId())
            .tipoPagamento(TipoPagamento.CREDITO)
            .build();
    }

    @AfterEach
    void tearDown() {
        pedidoRepository.deleteAll();
        cafeRepository.deleteAll();
        fornecedorRepository.deleteAll();
        clienteRepository.deleteAll();
    }

    @Nested
    @DisplayName("Conjunto de casos de verificação do fluxo API Rest")
    class PedidoFluxosApi {
        @Test
        @DisplayName("Quando buscamos todos os pedidos de um cliente")
        void listarPedidosCliente() throws Exception {
            // Act
            String responseJsonString = driver.perform(get(URI_PEDIDOS)
                    .param("id", cliente.getId().toString())
                    .param("codigoAcesso", "123123")
                    .param("isFornecedor", "false"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

            List<PedidoResponseDTO> resultado = objectMapper.readValue(responseJsonString, new TypeReference<>() {});

            // Assert
            assertEquals(1, resultado.size());
        }

        @Test
        @DisplayName("Quando buscamos todos os pedidos de um fornecedor")
        void listarPedidosFornecedor() throws Exception {
            // Act
            String responseJsonString = driver.perform(get(URI_PEDIDOS)
                    .param("id", fornecedor.getId().toString())
                    .param("codigoAcesso", "222222")
                    .param("isFornecedor", "true"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

            List<PedidoResponseDTO> resultado = objectMapper.readValue(responseJsonString, new TypeReference<>() {});

            // Assert
            assertEquals(1, resultado.size());
        }

        @Test
        @DisplayName("Quando recuperamos um pedido")
        void recuperarPedido() throws Exception {
            // Act
            String responseJsonString = driver.perform(get(URI_PEDIDOS + "/" + pedido.getId())
                    .param("id", cliente.getId().toString())
                    .param("codigoAcesso", cliente.getCodigo())
                    .param("isFornecedor", "false"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

            PedidoResponseDTO resultado = objectMapper.readValue(responseJsonString, new TypeReference<>() {});

            // Assert
            assertEquals(pedido.getCafe().getId(), resultado.getCafe().getId());
        }

        @Test
        @DisplayName("Quando recuperamos um pedido invalido")
        void recuperarPedidoInvalido() throws Exception {
            // Act
            String responseJsonString = driver.perform(get(URI_PEDIDOS + "/99999")
                    .param("id", cliente.getId().toString())
                    .param("codigoAcesso", cliente.getCodigo())
                    .param("isFornecedor", "false"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertEquals("O pedido consultado nao existe!", resultado.getMessage());
        }

        @Test
        @DisplayName("Quando criamos um pedido novo")
        void criarPedidoValido() throws Exception {
            // Act
            String responseJsonString = driver.perform(post(URI_PEDIDOS)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(pedidoPostPutRequestDTO))
                    .param("idCliente", clientePremium.getId().toString())
                    .param("codigoAcesso", clientePremium.getCodigo()))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

            PedidoResponseDTO resultado = objectMapper.readValue(responseJsonString, new TypeReference<>(){});

            // Assert
            EnderecoDTO enderecoCliente = new EnderecoDTO(clientePremium.getEndereco());
            assertAll(
                () -> assertNotNull(resultado.getId()),
                () -> assertEquals(clientePremium.getId(), resultado.getCliente().getId()),
                () -> assertEquals(pedidoPostPutRequestDTO.getIdCafe(), resultado.getCafe().getId()),
                () -> assertEquals(enderecoCliente, resultado.getEndereco())
            );
        }

        @Test
        @DisplayName("Quando criamos um pedido novo com endereço especifico")
        void criarPedidoValidoEnderecoEspecifico() throws Exception {
            // Arrange
            pedidoPostPutRequestDTO.setEndereco(EnderecoDTO.builder()
                .cep("12312300")
                .bairro("Bairro")
                .cidade("Cidade nova")
                .rua("Rua A")
                .numero("455")
                .build());

            // Act
            String responseJsonString = driver.perform(post(URI_PEDIDOS)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(pedidoPostPutRequestDTO))
                    .param("idCliente", clientePremium.getId().toString())
                    .param("codigoAcesso", clientePremium.getCodigo()))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

            PedidoResponseDTO resultado = objectMapper.readValue(responseJsonString, new TypeReference<>(){});

            // Assert
            EnderecoDTO enderecoCliente = new EnderecoDTO(clientePremium.getEndereco());
            assertAll(
                () -> assertNotEquals(enderecoCliente, resultado.getEndereco()),
                () -> assertEquals(pedidoPostPutRequestDTO.getEndereco(), resultado.getEndereco())
            );
        }

        @Test
        @DisplayName("Quando criamos um pedido com cliente inexistente")
        void criarPedidoClienteInexistente() throws Exception {
            // Act
            String responseJsonString = driver.perform(post(URI_PEDIDOS)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(pedidoPostPutRequestDTO))
                    .param("idCliente", "9999999999")
                    .param("codigoAcesso", clientePremium.getCodigo()))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertEquals("O cliente consultado nao existe!", resultado.getMessage());
        }

        @Test
        @DisplayName("Quando criamos um pedido com codigo de acesso invalido")
        void criarPedidoCodigoAccessoInvalido() throws Exception {
            // Act
            String responseJsonString = driver.perform(post(URI_PEDIDOS)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(pedidoPostPutRequestDTO))
                    .param("idCliente", cliente.getId().toString())
                    .param("codigoAcesso", "invalido"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertEquals("Codigo de acesso invalido!", resultado.getMessage());
        }

        @Test
        @DisplayName("Quando criamos um pedido com café indisponivel")
        void criarPedidoCafeIndisponivel() throws Exception {
            // Arrange
            pedidoPostPutRequestDTO.setIdCafe(cafeIndisponivel.getId());

            // Act
            String responseJsonString = driver.perform(post(URI_PEDIDOS)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(pedidoPostPutRequestDTO))
                    .param("idCliente", cliente.getId().toString())
                    .param("codigoAcesso", cliente.getCodigo()))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertEquals("Cafe indisponivel!", resultado.getMessage());
        }

        @Test
        @DisplayName("Quando criamos um pedido com assinatura errada")
        void criarPedidoCafePremium() throws Exception {
            // Arrange
            pedidoPostPutRequestDTO.setIdCafe(cafePremium.getId());

            // Act
            String responseJsonString = driver.perform(post(URI_PEDIDOS)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(pedidoPostPutRequestDTO))
                    .param("idCliente", cliente.getId().toString())
                    .param("codigoAcesso", cliente.getCodigo()))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertEquals("Assinatura invalida para este cafe!", resultado.getMessage());
        }

        @Test
        @DisplayName("Quando criamos um pedido com café inexistente")
        void criarPedidoCafeInexistente() throws Exception {
            // Arrange
            pedidoPostPutRequestDTO.setIdCafe(9999L);

            // Act
            String responseJsonString = driver.perform(post(URI_PEDIDOS)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(pedidoPostPutRequestDTO))
                    .param("idCliente", cliente.getId().toString())
                    .param("codigoAcesso", cliente.getCodigo()))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertEquals("O cafe consultado nao existe!", resultado.getMessage());
        }

        @Test
        @DisplayName("Quando criamos um pedido com tipo de pagamento não aceito")
        void criarPedidoPagamentoNaoAceito() throws Exception {
            // Arrange
            pedidoPostPutRequestDTO.setTipoPagamento(TipoPagamento.PIX);
            fornecedor.getTiposPagamento().remove(TipoPagamento.PIX);
            fornecedor = fornecedorRepository.save(fornecedor);

            // Act
            String responseJsonString = driver.perform(post(URI_PEDIDOS)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(pedidoPostPutRequestDTO))
                    .param("idCliente", cliente.getId().toString())
                    .param("codigoAcesso", cliente.getCodigo()))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertEquals("Fornecedor nao aceita esse tipo de pagamento!", resultado.getMessage());
        }

        @Test
        @DisplayName("Quando atualizamos um pedido")
        void atualizarPedidoValido() throws Exception {
            // Arrange
            pedidoPostPutRequestDTO.setEndereco(EnderecoDTO.builder()
                .cep("44440000")
                .bairro("Lagoinhas")
                .cidade("Cidade Grande")
                .rua("Rua central")
                .numero("123")
                .build());

            // Act
            String responseJsonString = driver.perform(put(URI_PEDIDOS + "/" + pedido.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(pedidoPostPutRequestDTO))
                    .param("id", pedido.getCliente().getId().toString())
                    .param("codigo", pedido.getCliente().getCodigo())
                    .param("isFornecedor", "false"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

            PedidoResponseDTO resultado = objectMapper.readValue(responseJsonString, new TypeReference<>(){});

            // Assert
            assertAll(
                () -> assertEquals("Lagoinhas", resultado.getEndereco().getBairro())
            );
        }

        @Test
        @DisplayName("Quando atualizamos um pedido pelo fornecedor")
        void atualizarPedidoValidoPeloFornecedor() throws Exception {
            // Arrange
            pedidoPostPutRequestDTO.setEndereco(EnderecoDTO.builder()
                .cep("44440000")
                .bairro("Lagoinhas")
                .cidade("Cidade Grande")
                .rua("Rua central")
                .numero("123")
                .build());

            // Act
            String responseJsonString = driver.perform(put(URI_PEDIDOS + "/" + pedido.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(pedidoPostPutRequestDTO))
                    .param("id", fornecedor.getId().toString())
                    .param("codigo", fornecedor.getCodigo())
                    .param("isFornecedor", "true"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

            PedidoResponseDTO resultado = objectMapper.readValue(responseJsonString, new TypeReference<>(){});

            // Assert
            assertAll(
                () -> assertEquals("Lagoinhas", resultado.getEndereco().getBairro())
            );
        }

        @Test
        @DisplayName("Quando atualizamos um pedido inexistente")
        void atualizarPedidoInexistente() throws Exception {
            // Act
            String responseJsonString = driver.perform(put(URI_PEDIDOS + "/99999")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(pedidoPostPutRequestDTO))
                    .param("id", pedido.getCliente().getId().toString())
                    .param("codigo", pedido.getCliente().getCodigo())
                    .param("isFornecedor", "false"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertEquals("O pedido consultado nao existe!", resultado.getMessage());
        }

        @Test
        @DisplayName("Quando atualizamos um pedido com café inexistente")
        void atualizarPedidoCafeInexistente() throws Exception {
            // Arrange
            pedidoPostPutRequestDTO.setIdCafe(99999L);

            // Act
            String responseJsonString = driver.perform(put(URI_PEDIDOS + "/" + pedido.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(pedidoPostPutRequestDTO))
                    .param("id", pedido.getCliente().getId().toString())
                    .param("codigo", pedido.getCliente().getCodigo())
                    .param("isFornecedor", "false"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertEquals("O cafe consultado nao existe!", resultado.getMessage());
        }

        @Test
        @DisplayName("Quando atualizamos um pedido com cliente errado")
        void atualizarPedidoClienteErrado() throws Exception {
            // Act
            String responseJsonString = driver.perform(put(URI_PEDIDOS + "/" + pedido.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(pedidoPostPutRequestDTO))
                    .param("id", clientePremium.getId().toString())
                    .param("codigo", clientePremium.getCodigo())
                    .param("isFornecedor", "false"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertEquals("Cliente invalido!", resultado.getMessage());
        }

        @Test
        @DisplayName("Quando atualizamos um pedido com fornecedor errado")
        void atualizarPedidoFornecedorErrado() throws Exception {
            // Act
            String responseJsonString = driver.perform(put(URI_PEDIDOS + "/" + pedido.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(pedidoPostPutRequestDTO))
                    .param("id", fornecedorOutro.getId().toString())
                    .param("codigo", fornecedorOutro.getCodigo())
                    .param("isFornecedor", "true"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertEquals("Fornecedor invalido!", resultado.getMessage());
        }

        @Test
        @DisplayName("Quando atualizamos um pedido com café inexistente")
        void atualizarPedidoPagamentoNaoAceito() throws Exception {
            // Arrange
            pedidoPostPutRequestDTO.setTipoPagamento(TipoPagamento.PIX);
            fornecedor.getTiposPagamento().remove(TipoPagamento.PIX);
            fornecedor = fornecedorRepository.save(fornecedor);

            // Act
            String responseJsonString = driver.perform(put(URI_PEDIDOS + "/" + pedido.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(pedidoPostPutRequestDTO))
                    .param("id", pedido.getCliente().getId().toString())
                    .param("codigo", pedido.getCliente().getCodigo())
                    .param("isFornecedor", "false"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertEquals("Fornecedor nao aceita esse tipo de pagamento!", resultado.getMessage());
        }

        @Test
        @DisplayName("Quando excluimos um pedido")
        void excluirPedidoValido() throws Exception {
            // Act
            String responseJsonString = driver.perform(delete(URI_PEDIDOS + "/" + pedido.getId())
                    .param("id", pedido.getCliente().getId().toString())
                    .param("codigo", pedido.getCliente().getCodigo())
                    .param("isFornecedor", "false"))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andReturn().getResponse().getContentAsString();

            // Assert
            assertTrue(responseJsonString.isBlank());
        }

        @Test
        @DisplayName("Quando excluimos um pedido pelo fornecedor")
        void excluirPedidoValidoPeloFornecedor() throws Exception {
            // Act
            String responseJsonString = driver.perform(delete(URI_PEDIDOS + "/" + pedido.getId())
                    .param("id", fornecedor.getId().toString())
                    .param("codigo", fornecedor.getCodigo())
                    .param("isFornecedor", "true"))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andReturn().getResponse().getContentAsString();

            // Assert
            assertTrue(responseJsonString.isBlank());
        }

        @Test
        @DisplayName("Quando excluimos um pedido inexistente")
        void excluirPedidoInexistente() throws Exception {
            // Act
            String responseJsonString = driver.perform(delete(URI_PEDIDOS + "/99999")
                    .param("id", pedido.getCliente().getId().toString())
                    .param("codigo", pedido.getCliente().getCodigo())
                    .param("isFornecedor", "false"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertEquals("O pedido consultado nao existe!", resultado.getMessage());
        }

        @Test
        @DisplayName("Quando excluimos um pedido com cliente errado")
        void excluirPedidoClienteErrado() throws Exception {
            // Act
            String responseJsonString = driver.perform(delete(URI_PEDIDOS + "/" + pedido.getId())
                    .param("id", clientePremium.getId().toString())
                    .param("codigo", clientePremium.getCodigo())
                    .param("isFornecedor", "false"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertEquals("Cliente invalido!", resultado.getMessage());
        }

        @Test
        @DisplayName("Quando excluimos um pedido com fornecedor errado")
        void excluirPedidoFornecedorErrado() throws Exception {
            // Act
            String responseJsonString = driver.perform(delete(URI_PEDIDOS + "/" + pedido.getId())
                    .param("id", fornecedorOutro.getId().toString())
                    .param("codigo", fornecedorOutro.getCodigo())
                    .param("isFornecedor", "true"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertEquals("Fornecedor invalido!", resultado.getMessage());
        }

        @Test
        @DisplayName("Quando pagamos um pedido valido")
        void pagarPedidoValido() throws Exception {
            // Act
            String responseJsonString = driver.perform(patch(URI_PEDIDOS + "/" + pedido.getId() + "/pagar")
                    .param("idCliente", cliente.getId().toString())
                    .param("codigoAcesso", cliente.getCodigo()))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

            PedidoResponseDTO resultado = objectMapper.readValue(responseJsonString, new TypeReference<>(){});

            // Assert
            assertAll(
                () -> assertTrue(resultado.isPago())
            );
        }

        @Test
        @DisplayName("Quando pagamos um pedido já pago")
        void pagarPedidoJaPago() throws Exception {
            // Arrange
            pedidoService.confirmarPagamento(pedido.getId(), cliente.getId(), cliente.getCodigo());

            // Act
            String responseJsonString = driver.perform(patch(URI_PEDIDOS + "/" + pedido.getId() + "/pagar")
                    .param("idCliente", cliente.getId().toString())
                    .param("codigoAcesso", cliente.getCodigo()))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertEquals("Pedido ja foi pago!", resultado.getMessage());
        }

        @Test
        @DisplayName("Quando pagamos um pedido com cliente errado")
        void pagarPedidoClienteErrado() throws Exception {
            // Act
            String responseJsonString = driver.perform(patch(URI_PEDIDOS + "/" + pedido.getId() + "/pagar")
                    .param("idCliente", clientePremium.getId().toString())
                    .param("codigoAcesso", clientePremium.getCodigo()))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertEquals("Cliente invalido!", resultado.getMessage());
        }
    }

    @Nested
    @DisplayName("Conjunto de casos de verificação dos tipos de pagamento")
    class PedidoVerificacaoTiposPagamento {
        @Test
        @DisplayName("Quando calculamos o valor de um pedido via credito")
        void verificarValorCredito() throws Exception {
            // Arrange
            pedido.setTipoPagamento(TipoPagamento.CREDITO);
            pedidoRepository.save(pedido);

            // Act
            String responseJsonString = driver.perform(get(URI_PEDIDOS + "/" + pedido.getId())
                    .param("id", cliente.getId().toString())
                    .param("codigoAcesso", cliente.getCodigo())
                    .param("isFornecedor", "false"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

            PedidoResponseDTO resultado = objectMapper.readValue(responseJsonString, new TypeReference<>() {});

            // Assert
            assertEquals(25.0, resultado.getValor());
        }

        @Test
        @DisplayName("Quando calculamos o valor de um pedido via debito")
        void verificarValorDebito() throws Exception {
            // Arrange
            pedido.setTipoPagamento(TipoPagamento.DEBITO);
            pedidoRepository.save(pedido);

            // Act
            String responseJsonString = driver.perform(get(URI_PEDIDOS + "/" + pedido.getId())
                    .param("id", cliente.getId().toString())
                    .param("codigoAcesso", cliente.getCodigo())
                    .param("isFornecedor", "false"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

            PedidoResponseDTO resultado = objectMapper.readValue(responseJsonString, new TypeReference<>() {});

            // Assert
            assertEquals(25.0 * 0.975, resultado.getValor());
        }

        @Test
        @DisplayName("Quando calculamos o valor de um pedido via pix")
        void verificarValorPix() throws Exception {
            // Arrange
            pedido.setTipoPagamento(TipoPagamento.PIX);
            pedidoRepository.save(pedido);

            // Act
            String responseJsonString = driver.perform(get(URI_PEDIDOS + "/" + pedido.getId())
                    .param("id", cliente.getId().toString())
                    .param("codigoAcesso", cliente.getCodigo())
                    .param("isFornecedor", "false"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

            PedidoResponseDTO resultado = objectMapper.readValue(responseJsonString, new TypeReference<>() {});

            // Assert
            assertEquals(25.0 * 0.95, resultado.getValor());
        }
    }

    @Nested
    @DisplayName("Conjunto de casos de verificação do status do pedido")
    class PedidoVerificacaoStatus {
        @Test
        @DisplayName("Quando confirmamos a entrega de um pedido")
        void pedidoEntregue() throws Exception {
            Pedido pedido1 = pedidoRepository.save(Pedido.builder()
                .cafe(cafe)
                .endereco(cliente.getEndereco())
                .cliente(cliente)
                .status(StatusPedidoEnum.EM_ENTREGA)
                .pago(true)
                .tipoPagamento(TipoPagamento.CREDITO)
                .build());

            String responseJsonString = driver.perform(patch(URI_PEDIDOS + "/" + pedido1.getId() + "/confirmarEntrega")
                    .param("idCliente", cliente.getId().toString())
                    .param("codigoAcesso", cliente.getCodigo()))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

            PedidoResponseDTO resultado = objectMapper.readValue(responseJsonString, new TypeReference<>() {});

            assertEquals(StatusPedidoEnum.ENTREGUE, resultado.getStatus());
        }

        @Test
        @DisplayName("Quando confirmamos a entrega de um pedido que está com um status inválido")
        void pedidoEntregueInvalido() throws Exception {

            String responseJsonString = driver.perform(patch(URI_PEDIDOS + "/" + pedido.getId() + "/confirmarEntrega")
                            .param("idCliente", cliente.getId().toString())
                            .param("codigoAcesso", cliente.getCodigo()))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            assertEquals("Status do pedido invalido para esta operacao", resultado.getMessage());
        }
    }

    @Test
    @DisplayName("Quando colocamos um pedido em processo de entrega")
    void pedidoEmRota() throws Exception{
        Pedido pedido1 = pedidoRepository.save(Pedido.builder()
                .cafe(cafe)
                .endereco(cliente.getEndereco())
                .cliente(cliente)
                .status(StatusPedidoEnum.PRONTO)
                .entregador(entregador)
                .pago(true)
                .tipoPagamento(TipoPagamento.CREDITO)
                .build());

        String responseJsonString = driver.perform(patch(URI_PEDIDOS + "/" + pedido1.getId() + "/pedidoEmEntrega")
                        .param("idCliente", cliente.getId().toString())
                        .param("codigoAcesso", cliente.getCodigo()))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        PedidoResponseDTO resultado = objectMapper.readValue(responseJsonString, new TypeReference<>() {});

        assertEquals(StatusPedidoEnum.EM_ENTREGA, resultado.getStatus());
    }

    @Test
    @DisplayName("Quando colocamos um pedido com status inválido em processo de entrega")
    void pedidoEmRotaInvalido() throws Exception {

        String responseJsonString = driver.perform(patch(URI_PEDIDOS + "/" + pedido.getId() + "/pedidoEmEntrega")
                        .param("idCliente", cliente.getId().toString())
                        .param("codigoAcesso", cliente.getCodigo()))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

        assertEquals("Status do pedido invalido para esta operacao", resultado.getMessage());
    }
}
