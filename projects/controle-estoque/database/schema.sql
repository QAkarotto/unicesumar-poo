-- =====================================================================
--  Sistema de Controle de Estoque - Estrutura do banco (MySQL 8+)
-- ---------------------------------------------------------------------
--  Execute DEPOIS de criar e selecionar o banco:
--      CREATE DATABASE controle_estoque CHARACTER SET utf8mb4;
--      USE controle_estoque;
--  ATENÇÃO: o script remove e recria as tabelas (apaga os dados).
-- =====================================================================

DROP TABLE IF EXISTS movimentacao;
DROP TABLE IF EXISTS produto_fornecedor;
DROP TABLE IF EXISTS produto;
DROP TABLE IF EXISTS fornecedor;
DROP TABLE IF EXISTS categoria;

-- ---------------------------------------------------------------------
-- CATEGORIA (1) ---- (N) PRODUTO
-- ---------------------------------------------------------------------
CREATE TABLE categoria (
    id        BIGINT       NOT NULL AUTO_INCREMENT,
    nome      VARCHAR(80)  NOT NULL,
    descricao VARCHAR(255) NULL,
    CONSTRAINT pk_categoria      PRIMARY KEY (id),
    CONSTRAINT uk_categoria_nome UNIQUE (nome)
);

-- ---------------------------------------------------------------------
-- FORNECEDOR (N) ---- (N) PRODUTO   (tabela associativa produto_fornecedor)
-- ---------------------------------------------------------------------
CREATE TABLE fornecedor (
    id       BIGINT       NOT NULL AUTO_INCREMENT,
    nome     VARCHAR(120) NOT NULL,
    cnpj     CHAR(14)     NOT NULL,
    email    VARCHAR(120) NULL,
    telefone VARCHAR(20)  NULL,
    CONSTRAINT pk_fornecedor      PRIMARY KEY (id),
    CONSTRAINT uk_fornecedor_cnpj UNIQUE (cnpj)
);

-- ---------------------------------------------------------------------
-- PRODUTO
-- ---------------------------------------------------------------------
CREATE TABLE produto (
    id                    BIGINT        NOT NULL AUTO_INCREMENT,
    sku                   VARCHAR(30)   NOT NULL,
    nome                  VARCHAR(120)  NOT NULL,
    descricao             VARCHAR(500)  NULL,
    preco_unitario        DECIMAL(12,2) NOT NULL,
    quantidade_em_estoque INT           NOT NULL DEFAULT 0,
    estoque_minimo        INT           NOT NULL DEFAULT 0,
    ativo                 BOOLEAN       NOT NULL DEFAULT TRUE,
    categoria_id          BIGINT        NOT NULL,
    CONSTRAINT pk_produto                PRIMARY KEY (id),
    CONSTRAINT uk_produto_sku            UNIQUE (sku),
    CONSTRAINT fk_produto_categoria      FOREIGN KEY (categoria_id) REFERENCES categoria (id),
    CONSTRAINT ck_produto_preco          CHECK (preco_unitario >= 0),
    CONSTRAINT ck_produto_quantidade     CHECK (quantidade_em_estoque >= 0),
    CONSTRAINT ck_produto_estoque_minimo CHECK (estoque_minimo >= 0)
);

CREATE TABLE produto_fornecedor (
    produto_id    BIGINT NOT NULL,
    fornecedor_id BIGINT NOT NULL,
    CONSTRAINT pk_produto_fornecedor PRIMARY KEY (produto_id, fornecedor_id),
    CONSTRAINT fk_pf_produto    FOREIGN KEY (produto_id)    REFERENCES produto (id)    ON DELETE CASCADE,
    CONSTRAINT fk_pf_fornecedor FOREIGN KEY (fornecedor_id) REFERENCES fornecedor (id) ON DELETE CASCADE
);

-- ---------------------------------------------------------------------
-- MOVIMENTACAO (N) ---- (1) PRODUTO
-- MOVIMENTACAO (N) ---- (0..1) FORNECEDOR   (obrigatório apenas na ENTRADA)
-- ---------------------------------------------------------------------
CREATE TABLE movimentacao (
    id              BIGINT       NOT NULL AUTO_INCREMENT,
    produto_id      BIGINT       NOT NULL,
    fornecedor_id   BIGINT       NULL,
    tipo            VARCHAR(10)  NOT NULL,
    quantidade      INT          NOT NULL,
    saldo_anterior  INT          NOT NULL,
    saldo_posterior INT          NOT NULL,
    data_hora       DATETIME     NOT NULL,
    observacao      VARCHAR(255) NULL,
    CONSTRAINT pk_movimentacao            PRIMARY KEY (id),
    CONSTRAINT fk_movimentacao_produto    FOREIGN KEY (produto_id)    REFERENCES produto (id),
    CONSTRAINT fk_movimentacao_fornecedor FOREIGN KEY (fornecedor_id) REFERENCES fornecedor (id),
    CONSTRAINT ck_movimentacao_tipo       CHECK (tipo IN ('ENTRADA', 'SAIDA', 'AJUSTE')),
    CONSTRAINT ck_movimentacao_quantidade CHECK (quantidade > 0),
    CONSTRAINT ck_movimentacao_saldos     CHECK (saldo_anterior >= 0 AND saldo_posterior >= 0),
    CONSTRAINT ck_movimentacao_entrada    CHECK (tipo <> 'ENTRADA' OR fornecedor_id IS NOT NULL)
);

CREATE INDEX idx_movimentacao_produto_data ON movimentacao (produto_id, data_hora);
