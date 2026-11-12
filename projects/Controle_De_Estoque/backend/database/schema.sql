-- Schema do Controle de Estoque (MySQL 8+)
-- Execucao: mysql -u root -p < database/schema.sql

CREATE DATABASE IF NOT EXISTS controle_estoque
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE controle_estoque;

-- Categoria 1 --- N Produto
CREATE TABLE categoria (
    id   BIGINT       NOT NULL AUTO_INCREMENT,
    nome VARCHAR(80)  NOT NULL,
    CONSTRAINT pk_categoria PRIMARY KEY (id),
    CONSTRAINT uk_categoria_nome UNIQUE (nome)
);

-- Produto 1 --- N Movimentacao
CREATE TABLE produto (
    id             BIGINT       NOT NULL AUTO_INCREMENT,
    codigo         VARCHAR(30)  NOT NULL,
    nome           VARCHAR(120) NOT NULL,
    categoria_id   BIGINT       NOT NULL,
    quantidade     INT          NOT NULL DEFAULT 0,
    estoque_minimo INT          NOT NULL DEFAULT 0,
    ativo          BOOLEAN      NOT NULL DEFAULT TRUE,
    CONSTRAINT pk_produto PRIMARY KEY (id),
    CONSTRAINT uk_produto_codigo UNIQUE (codigo),
    CONSTRAINT fk_produto_categoria FOREIGN KEY (categoria_id)
        REFERENCES categoria (id) ON DELETE RESTRICT,
    CONSTRAINT ck_produto_quantidade CHECK (quantidade >= 0),
    CONSTRAINT ck_produto_estoque_minimo CHECK (estoque_minimo >= 0)
);

CREATE TABLE movimentacao (
    id         BIGINT       NOT NULL AUTO_INCREMENT,
    produto_id BIGINT       NOT NULL,
    tipo       VARCHAR(10)  NOT NULL,
    quantidade INT          NOT NULL,
    motivo     VARCHAR(200) NULL,
    data_hora  DATETIME(6)  NOT NULL,
    CONSTRAINT pk_movimentacao PRIMARY KEY (id),
    CONSTRAINT fk_movimentacao_produto FOREIGN KEY (produto_id)
        REFERENCES produto (id) ON DELETE RESTRICT,
    CONSTRAINT ck_movimentacao_tipo CHECK (tipo IN ('ENTRADA', 'SAIDA')),
    CONSTRAINT ck_movimentacao_quantidade CHECK (quantidade > 0)
);

-- Auditoria: a API so insere e consulta. Nao ha FK de proposito, para que o
-- registro continue existindo mesmo depois que a entidade for excluida.
-- A exclusao de logs so pode ser feita diretamente no banco.
CREATE TABLE log_auditoria (
    id          BIGINT       NOT NULL AUTO_INCREMENT,
    acao        VARCHAR(20)  NOT NULL,
    entidade    VARCHAR(20)  NOT NULL,
    entidade_id BIGINT       NULL,
    descricao   VARCHAR(255) NOT NULL,
    data_hora   DATETIME(6)  NOT NULL,
    CONSTRAINT pk_log_auditoria PRIMARY KEY (id)
);
