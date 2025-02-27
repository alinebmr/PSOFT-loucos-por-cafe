package com.ufcg.psoft.commerce;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.ufcg.psoft.commerce.enums.QualidadeCafe;
import com.ufcg.psoft.commerce.enums.TipoAssinatura;
import com.ufcg.psoft.commerce.enums.TipoGraoCafe;
import com.ufcg.psoft.commerce.model.Admin;
import com.ufcg.psoft.commerce.model.Cafe;
import com.ufcg.psoft.commerce.model.Cliente;
import com.ufcg.psoft.commerce.model.Endereco;
import com.ufcg.psoft.commerce.model.Entregador;
import com.ufcg.psoft.commerce.model.Fornecedor;
import com.ufcg.psoft.commerce.repository.AdminRepository;
import com.ufcg.psoft.commerce.repository.CafeRepository;
import com.ufcg.psoft.commerce.repository.ClienteRepository;
import com.ufcg.psoft.commerce.repository.EntregadorRespository;
import com.ufcg.psoft.commerce.repository.FornecedorRepository;

// Inicia o sistema com algumas entidades, para facilitar testes manuais.
// Esses dados não são adicionados ao rodar os testes automaticos.
@Component
@Profile("!test")
public class DadosIniciais implements ApplicationRunner {
    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private CafeRepository cafeRepository;

    @Autowired
    private FornecedorRepository fornecedorRepository;

    @Autowired
    private EntregadorRespository entregadorRespository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("Inicializando dados..");

        Endereco endereco = Endereco.builder()
            .cep("123")
            .cidade("Campina grande")
            .bairro("Centro")
            .rua("Rua dos testes")
            .numero("123")
            .build();

        clienteRepository.save(Cliente.builder()
            .nome("Jose Cafe Inacio")
            .endereco(endereco)
            .codigo("123123")
            .build()
        );

        clienteRepository.save(Cliente.builder()
            .nome("Maria Macchiato")
            .endereco(endereco)
            .codigo("123123")
            .assinatura(TipoAssinatura.PREMIUM)
            .build()
        );

        adminRepository.save(Admin.builder()
            .nome("Sr Admin Silva")
            .codigo("123123")
            .build()
        );

        Fornecedor fornecedor1 = fornecedorRepository.save(Fornecedor.builder()
            .nomeEmpresa("Carlos e Caroline Cafés")
            .cnpj("10.775.134/0001-38")
            .codigo("123123")
            .build()
        );

        cafeRepository.save(Cafe.builder()
            .fornecedor(fornecedor1)
            .nome("Café preto nordestino")
            .origem("Nordeste, Brasil")
            .tipo(TipoGraoCafe.MOIDO)
            .perfil("Gosto bem forte")
            .preco(15.00)
            .qualidade(QualidadeCafe.NORMAL)
            .tamanhoEmbalagem(10)
            .build()
        );

        cafeRepository.save(Cafe.builder()
            .fornecedor(fornecedor1)
            .nome("Cafe Muito Bom")
            .origem("Xique-Xique Bahia")
            .tipo(TipoGraoCafe.GRAO)
            .perfil("Frutas Vermelhas")
            .preco(24.99)
            .qualidade(QualidadeCafe.NORMAL)
            .tamanhoEmbalagem(35)
            .build()
        );

        Fornecedor fornecedor2 = fornecedorRepository.save(Fornecedor.builder()
            .nomeEmpresa("Benjamin e Breno Padaria ME")
            .cnpj("78.225.333/0001-06")
            .codigo("123123")
            .build()
        );

        cafeRepository.save(Cafe.builder()
            .fornecedor(fornecedor2)
            .nome("Cafe Espresso")
            .origem("Campina Grande Paraiba")
            .tipo(TipoGraoCafe.CAPSULA)
            .perfil("Frutas vermelhas")
            .preco(19.99)
            .qualidade(QualidadeCafe.NORMAL)
            .tamanhoEmbalagem(10)
            .build()
        );

        cafeRepository.save(Cafe.builder()
            .fornecedor(fornecedor2)
            .nome("Cafe Puccino")
            .origem("Italia")
            .tipo(TipoGraoCafe.CAPSULA)
            .perfil("Rosas")
            .preco(59.99)
            .qualidade(QualidadeCafe.PREMIUM)
            .tamanhoEmbalagem(15)
            .build()
        );

        entregadorRespository.save(Entregador.builder()
            .nome("Leticia Fretes")
            .placaVeiculo("AAA-2345")
            .tipoVeiculo("Caminhão")
            .corVeiculo("Verde")
            .codigo("123123")
            .aprovado(false)
            .build()
        );

        entregadorRespository.save(Entregador.builder()
            .nome("Teodoro Fretes")
            .placaVeiculo("BBB-5678")
            .tipoVeiculo("Moto")
            .corVeiculo("Amarelo")
            .codigo("123123")
            .aprovado(true)
            .build()
        );
    }
}
