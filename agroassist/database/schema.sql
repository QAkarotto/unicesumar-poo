-- MySQL 8.4. Estrutura modelada explicitamente; Hibernate apenas valida o mapeamento.
CREATE DATABASE IF NOT EXISTS agroassist CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
USE agroassist;

CREATE TABLE IF NOT EXISTS usuarios (
    id VARCHAR(40) PRIMARY KEY,
    nome VARCHAR(120) NOT NULL,
    perfil VARCHAR(20) NOT NULL,
    telefone VARCHAR(30) NOT NULL,
    email VARCHAR(160) NOT NULL DEFAULT '',
    municipio VARCHAR(100) NOT NULL,
    consentimento_em DATETIME(6) NOT NULL,
    CONSTRAINT ck_usuario_perfil CHECK (perfil IN ('PRODUTOR', 'TECNICO', 'ADMIN')),
    CONSTRAINT ck_usuario_nome CHECK (CHAR_LENGTH(TRIM(nome)) > 0),
    CONSTRAINT ck_usuario_telefone CHECK (CHAR_LENGTH(TRIM(telefone)) > 0)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS culturas (
    id VARCHAR(40) PRIMARY KEY,
    nome VARCHAR(60) NOT NULL UNIQUE
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS propriedades (
    id VARCHAR(40) PRIMARY KEY,
    produtor_id VARCHAR(40) NOT NULL,
    nome VARCHAR(120) NOT NULL,
    municipio VARCHAR(100) NOT NULL,
    area_ha DECIMAL(12,2) NOT NULL,
    CONSTRAINT fk_propriedade_produtor FOREIGN KEY (produtor_id) REFERENCES usuarios(id),
    CONSTRAINT ck_propriedade_area CHECK (area_ha > 0),
    CONSTRAINT ck_propriedade_nome CHECK (CHAR_LENGTH(TRIM(nome)) > 0)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS propriedade_culturas (
    propriedade_id VARCHAR(40) NOT NULL,
    cultura_id VARCHAR(40) NOT NULL,
    PRIMARY KEY (propriedade_id, cultura_id),
    CONSTRAINT fk_pc_propriedade FOREIGN KEY (propriedade_id) REFERENCES propriedades(id),
    CONSTRAINT fk_pc_cultura FOREIGN KEY (cultura_id) REFERENCES culturas(id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS tecnicos (
    id VARCHAR(40) PRIMARY KEY,
    usuario_id VARCHAR(40) NOT NULL UNIQUE,
    formacao VARCHAR(120) NOT NULL,
    registro VARCHAR(60) NOT NULL UNIQUE,
    ativo BOOLEAN NOT NULL,
    CONSTRAINT fk_tecnico_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id),
    CONSTRAINT ck_tecnico_registro CHECK (CHAR_LENGTH(TRIM(registro)) > 0)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS tecnico_culturas (
    tecnico_id VARCHAR(40) NOT NULL,
    cultura_id VARCHAR(40) NOT NULL,
    PRIMARY KEY (tecnico_id, cultura_id),
    CONSTRAINT fk_tc_tecnico FOREIGN KEY (tecnico_id) REFERENCES tecnicos(id),
    CONSTRAINT fk_tc_cultura FOREIGN KEY (cultura_id) REFERENCES culturas(id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS solicitacoes (
    id VARCHAR(40) PRIMARY KEY,
    protocolo VARCHAR(40) NOT NULL UNIQUE,
    produtor_id VARCHAR(40) NOT NULL,
    propriedade_id VARCHAR(40) NOT NULL,
    cultura_id VARCHAR(40) NOT NULL,
    tecnico_id VARCHAR(40) NULL,
    estadio VARCHAR(120) NOT NULL DEFAULT '',
    descricao VARCHAR(4000) NOT NULL,
    status VARCHAR(30) NOT NULL,
    criada_em DATETIME(6) NOT NULL,
    CONSTRAINT fk_solicitacao_produtor FOREIGN KEY (produtor_id) REFERENCES usuarios(id),
    CONSTRAINT fk_solicitacao_propriedade FOREIGN KEY (propriedade_id) REFERENCES propriedades(id),
    CONSTRAINT fk_solicitacao_cultura FOREIGN KEY (cultura_id) REFERENCES culturas(id),
    CONSTRAINT fk_solicitacao_tecnico FOREIGN KEY (tecnico_id) REFERENCES tecnicos(id),
    CONSTRAINT ck_solicitacao_status CHECK (status IN ('ABERTA', 'ATRIBUIDA', 'EM_ATENDIMENTO', 'CONCLUIDA', 'AVALIADA')),
    CONSTRAINT ck_solicitacao_responsavel CHECK ((status = 'ABERTA' AND tecnico_id IS NULL) OR (status <> 'ABERTA' AND tecnico_id IS NOT NULL)),
    CONSTRAINT ck_solicitacao_descricao CHECK (CHAR_LENGTH(TRIM(descricao)) > 0)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS solicitacao_fotos (
    solicitacao_id VARCHAR(40) NOT NULL,
    ordem INT NOT NULL,
    conteudo LONGTEXT NOT NULL,
    PRIMARY KEY (solicitacao_id, ordem),
    CONSTRAINT fk_foto_solicitacao FOREIGN KEY (solicitacao_id) REFERENCES solicitacoes(id),
    CONSTRAINT ck_foto_ordem CHECK (ordem BETWEEN 0 AND 2)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS historico_solicitacao (
    solicitacao_id VARCHAR(40) NOT NULL,
    ordem INT NOT NULL,
    em DATETIME(6) NOT NULL,
    status VARCHAR(30) NOT NULL,
    autor_id VARCHAR(40) NOT NULL,
    descricao VARCHAR(255) NOT NULL,
    PRIMARY KEY (solicitacao_id, ordem),
    CONSTRAINT fk_historico_solicitacao FOREIGN KEY (solicitacao_id) REFERENCES solicitacoes(id),
    CONSTRAINT fk_historico_autor FOREIGN KEY (autor_id) REFERENCES usuarios(id),
    CONSTRAINT ck_historico_ordem CHECK (ordem >= 0),
    CONSTRAINT ck_historico_status CHECK (status IN ('ABERTA', 'ATRIBUIDA', 'EM_ATENDIMENTO', 'CONCLUIDA', 'AVALIADA'))
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS atendimentos (
    id VARCHAR(40) PRIMARY KEY,
    solicitacao_id VARCHAR(40) NOT NULL UNIQUE,
    tecnico_id VARCHAR(40) NOT NULL,
    data DATE NOT NULL,
    modalidade VARCHAR(20) NOT NULL,
    diagnostico VARCHAR(4000) NOT NULL,
    recomendacao VARCHAR(4000) NOT NULL,
    CONSTRAINT fk_atendimento_solicitacao FOREIGN KEY (solicitacao_id) REFERENCES solicitacoes(id),
    CONSTRAINT fk_atendimento_tecnico FOREIGN KEY (tecnico_id) REFERENCES tecnicos(id),
    CONSTRAINT ck_atendimento_modalidade CHECK (modalidade IN ('VISITA', 'REMOTA')),
    CONSTRAINT ck_atendimento_diagnostico CHECK (CHAR_LENGTH(TRIM(diagnostico)) > 0),
    CONSTRAINT ck_atendimento_recomendacao CHECK (CHAR_LENGTH(TRIM(recomendacao)) > 0)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS avaliacoes (
    id VARCHAR(40) PRIMARY KEY,
    solicitacao_id VARCHAR(40) NOT NULL UNIQUE,
    nota INT NOT NULL,
    comentario VARCHAR(2000) NOT NULL DEFAULT '',
    em DATETIME(6) NOT NULL,
    CONSTRAINT fk_avaliacao_solicitacao FOREIGN KEY (solicitacao_id) REFERENCES solicitacoes(id),
    CONSTRAINT ck_avaliacao_nota CHECK (nota BETWEEN 1 AND 5)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS sequencia_protocolo (
    id BIGINT PRIMARY KEY,
    ultimo BIGINT NOT NULL,
    CONSTRAINT ck_sequencia_unica CHECK (id = 1),
    CONSTRAINT ck_sequencia_positiva CHECK (ultimo >= 0)
) ENGINE=InnoDB;

INSERT IGNORE INTO sequencia_protocolo (id, ultimo) VALUES (1, 0);
