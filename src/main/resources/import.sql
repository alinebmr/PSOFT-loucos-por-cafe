SHOW TABLES;
INSERT INTO cliente (id, nome, codigo, assinatura, endereco_cep, endereco_cidade, endereco_bairro, endereco_rua, endereco_numero) VALUES (10001, 'sicrano', '123456', 'NORMAL', '12345000', 'Campina Grande', 'Centro', 'Rua Jose Dantas', '123');
INSERT INTO cliente (id, nome, codigo, assinatura, endereco_cep, endereco_cidade, endereco_bairro, endereco_rua, endereco_numero) VALUES (10, 'Monopoly', '111111', 'PREMIUM', '51239874', 'Cidade Bacana', 'Esquina', 'Rua Parabéns', '29');
INSERT INTO admin (id, nome, codigo) VALUES (1, 'adm', '111111');
INSERT INTO fornecedor (id, nome_empresa, codigo, cnpj) VALUES (100, 'MicroCoffee', '222222', '53.901.420/2038-86');
INSERT INTO cafe (id, fornecedor_id, nome, origem, tipo, perfil, preco, qualidade, tamanho_embalagem, disponivel) VALUES (10, 100, 'Capuccino', 'Algum lugar', 'MOIDO', 'cremoso', 55, 'PREMIUM', 33, true);
INSERT INTO cafe (id, fornecedor_id, nome, origem, tipo, perfil, preco, qualidade, tamanho_embalagem, disponivel) VALUES (30, 100, 'Chocoffee', 'Willy Wonka', 'CAPSULA', 'doce como diabete amarga', 23, 'NORMAL', 10, true);
INSERT INTO cafe (id, fornecedor_id, nome, origem, tipo, perfil, preco, qualidade, tamanho_embalagem, disponivel) VALUES (20, 100, 'Leite com café', 'Caicó', 'MOIDO', 'leitoso', 5, 'NORMAL', 12, true);