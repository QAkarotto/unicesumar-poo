-- =====================================================================
-- Loja de Jogos - estrutura do banco (MySQL 8)
-- Executar uma vez antes de subir o backend.
-- =====================================================================

CREATE DATABASE IF NOT EXISTS loja_jogos
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE loja_jogos;

-- Ordem do DROP: primeiro quem tem FK (pedido), depois as tabelas referenciadas.
DROP TABLE IF EXISTS pedido;
DROP TABLE IF EXISTS cliente;
DROP TABLE IF EXISTS jogo;

-- ---------------------------------------------------------------------
-- CLIENTE: quem compra
-- ---------------------------------------------------------------------
CREATE TABLE cliente (
                         id     BIGINT       NOT NULL AUTO_INCREMENT,
                         nome   VARCHAR(100) NOT NULL,
                         email  VARCHAR(150) NOT NULL,

                         CONSTRAINT pk_cliente       PRIMARY KEY (id),
                         CONSTRAINT uk_cliente_email UNIQUE (email)
);

-- ---------------------------------------------------------------------
-- JOGO: produto vendido (é o card que aparece no React)
-- ---------------------------------------------------------------------
CREATE TABLE jogo (
                      id          BIGINT        NOT NULL AUTO_INCREMENT,
                      titulo      VARCHAR(150)  NOT NULL,
                      plataforma  VARCHAR(50)   NOT NULL,
                      preco       DECIMAL(10,2) NOT NULL,
                      estoque     INT           NOT NULL,

                      CONSTRAINT pk_jogo           PRIMARY KEY (id),
                      CONSTRAINT ck_jogo_preco     CHECK (preco > 0),
                      CONSTRAINT ck_jogo_estoque   CHECK (estoque >= 0)
);

-- ---------------------------------------------------------------------
-- PEDIDO: liga um cliente a um jogo
--   cliente 1 ---- N pedido
--   jogo    1 ---- N pedido
-- ---------------------------------------------------------------------
CREATE TABLE pedido (
                        id           BIGINT        NOT NULL AUTO_INCREMENT,
                        cliente_id   BIGINT        NOT NULL,
                        jogo_id      BIGINT        NOT NULL,
                        quantidade   INT           NOT NULL,
                        valor_total  DECIMAL(10,2) NOT NULL,
                        status       VARCHAR(20)   NOT NULL,
                        data_pedido  DATETIME      NOT NULL,

                        CONSTRAINT pk_pedido            PRIMARY KEY (id),
                        CONSTRAINT fk_pedido_cliente    FOREIGN KEY (cliente_id) REFERENCES cliente (id),
                        CONSTRAINT fk_pedido_jogo       FOREIGN KEY (jogo_id)    REFERENCES jogo (id),
                        CONSTRAINT ck_pedido_quantidade CHECK (quantidade > 0),
                        CONSTRAINT ck_pedido_status     CHECK (status IN ('CONFIRMADO', 'CANCELADO'))
);
