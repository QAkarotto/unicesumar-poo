-- ===================================================================
-- Script DDL: Modelagem do Banco de Dados Relacional (MySQL)
-- Atividade Final - 2BIM: Sistema Acadêmico Full Stack
-- ===================================================================

CREATE DATABASE IF NOT EXISTS sistema_academico CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE sistema_academico;

-- 1. Tabela: aluno
CREATE TABLE IF NOT EXISTS aluno (
    registro_academico VARCHAR(30) NOT NULL,
    nome VARCHAR(150) NOT NULL,
    email VARCHAR(150) NOT NULL,
    CONSTRAINT pk_aluno PRIMARY KEY (registro_academico),
    CONSTRAINT chk_aluno_email CHECK (email LIKE '%@%.%')
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 2. Tabela: disciplina
CREATE TABLE IF NOT EXISTS disciplina (
    id BIGINT AUTO_INCREMENT NOT NULL,
    codigo VARCHAR(30) NOT NULL,
    nome VARCHAR(150) NOT NULL,
    carga_horaria INT NOT NULL,
    CONSTRAINT pk_disciplina PRIMARY KEY (id),
    CONSTRAINT uq_disciplina_codigo UNIQUE (codigo),
    CONSTRAINT chk_carga_horaria CHECK (carga_horaria > 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 3. Tabela: turma
CREATE TABLE IF NOT EXISTS turma (
    id BIGINT AUTO_INCREMENT NOT NULL,
    codigo VARCHAR(50) NOT NULL,
    ano INT NOT NULL,
    semestre VARCHAR(20) NOT NULL,
    CONSTRAINT pk_turma PRIMARY KEY (id),
    CONSTRAINT chk_turma_ano CHECK (ano > 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 4. Tabela: oferta_disciplina
CREATE TABLE IF NOT EXISTS oferta_disciplina (
    id BIGINT AUTO_INCREMENT NOT NULL,
    codigo VARCHAR(50) NOT NULL,
    disciplina_id BIGINT NOT NULL,
    turma_id BIGINT NOT NULL,
    CONSTRAINT pk_oferta_disciplina PRIMARY KEY (id),
    CONSTRAINT uq_oferta_codigo UNIQUE (codigo),
    CONSTRAINT fk_oferta_disciplina FOREIGN KEY (disciplina_id) REFERENCES disciplina(id) ON DELETE RESTRICT,
    CONSTRAINT fk_oferta_turma FOREIGN KEY (turma_id) REFERENCES turma(id) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 5. Tabela: matricula
CREATE TABLE IF NOT EXISTS matricula (
    id BIGINT AUTO_INCREMENT NOT NULL,
    codigo VARCHAR(50) NOT NULL,
    aluno_ra VARCHAR(30) NOT NULL,
    oferta_id BIGINT NOT NULL,
    situacao VARCHAR(30) NOT NULL,
    resultado VARCHAR(30) NULL,
    data_matricula DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT pk_matricula PRIMARY KEY (id),
    CONSTRAINT uq_matricula_codigo UNIQUE (codigo),
    CONSTRAINT uq_matricula_aluno_oferta UNIQUE (aluno_ra, oferta_id),
    CONSTRAINT fk_matricula_aluno FOREIGN KEY (aluno_ra) REFERENCES aluno(registro_academico) ON DELETE RESTRICT,
    CONSTRAINT fk_matricula_oferta FOREIGN KEY (oferta_id) REFERENCES oferta_disciplina(id) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
