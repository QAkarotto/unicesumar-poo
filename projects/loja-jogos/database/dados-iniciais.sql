-- Dados de exemplo para a demonstração (opcional).
-- Executar depois do schema.sql.

USE loja_jogos;

INSERT INTO jogo (titulo, plataforma, preco, estoque) VALUES
                                                          ('The Legend of Zelda: Tears of the Kingdom', 'Nintendo Switch', 349.90, 5),
                                                          ('God of War Ragnarök',                       'PlayStation 5',   299.90, 3),
                                                          ('Forza Horizon 5',                           'Xbox Series',     199.90, 10),
                                                          ('Minecraft',                                 'PC',               99.90, 1);

INSERT INTO cliente (nome, email) VALUES
                                      ('Maria Souza',  'maria@email.com'),
                                      ('João Pereira', 'joao@email.com');
