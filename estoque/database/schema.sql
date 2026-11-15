CREATE DATABASE IF NOT EXISTS estoque CHARACTER SET utf8mb4;
USE estoque;

CREATE TABLE categoria (
    id   BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(80) NOT NULL UNIQUE
);

CREATE TABLE produto (
    id             BIGINT AUTO_INCREMENT PRIMARY KEY,
    sku            VARCHAR(40)   NOT NULL UNIQUE,
    nome           VARCHAR(120)  NOT NULL,
    preco          DECIMAL(10,2) NOT NULL,
    quantidade     INT           NOT NULL DEFAULT 0,
    estoque_minimo INT           NOT NULL DEFAULT 0,
    ativo          BOOLEAN       NOT NULL DEFAULT TRUE,
    categoria_id   BIGINT        NOT NULL,
    CONSTRAINT ck_produto_preco CHECK (preco >= 0),
    CONSTRAINT ck_produto_qtd CHECK (quantidade >= 0),
    CONSTRAINT ck_produto_min CHECK (estoque_minimo >= 0),
    CONSTRAINT fk_produto_categoria FOREIGN KEY (categoria_id) REFERENCES categoria (id)
);

CREATE TABLE movimentacao (
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    produto_id BIGINT      NOT NULL,
    tipo       VARCHAR(10) NOT NULL,
    quantidade INT         NOT NULL,
    data_hora  DATETIME(6) NOT NULL,
    CONSTRAINT ck_mov_tipo CHECK (tipo IN ('ENTRADA', 'SAIDA')),
    CONSTRAINT ck_mov_qtd CHECK (quantidade > 0),
    CONSTRAINT fk_mov_produto FOREIGN KEY (produto_id) REFERENCES produto (id)
);
