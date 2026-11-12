CREATE DATABASE IF NOT EXISTS vetagenda
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE vetagenda;

CREATE TABLE IF NOT EXISTS especialidade (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL UNIQUE,
    descricao VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS tutor (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(120) NOT NULL,
    cpf VARCHAR(14) NOT NULL UNIQUE,
    telefone VARCHAR(20) NOT NULL,
    email VARCHAR(120)
);

CREATE TABLE IF NOT EXISTS veterinario (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(120) NOT NULL,
    crmv VARCHAR(30) NOT NULL UNIQUE,
    telefone VARCHAR(20),
    especialidade_id BIGINT NOT NULL,
    CONSTRAINT fk_veterinario_especialidade
      FOREIGN KEY (especialidade_id) REFERENCES especialidade(id)
);

CREATE TABLE IF NOT EXISTS animal (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    especie VARCHAR(60) NOT NULL,
    raca VARCHAR(80),
    sexo VARCHAR(20),
    data_nascimento DATE,
    peso DECIMAL(6,2),
    tutor_id BIGINT NOT NULL,
    CONSTRAINT fk_animal_tutor
      FOREIGN KEY (tutor_id) REFERENCES tutor(id)
);

CREATE TABLE IF NOT EXISTS agendamento (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    data_hora DATETIME NOT NULL,
    motivo VARCHAR(255) NOT NULL,
    observacoes VARCHAR(500),
    status VARCHAR(20) NOT NULL,
    animal_id BIGINT NOT NULL,
    veterinario_id BIGINT NOT NULL,
    CONSTRAINT fk_agendamento_animal
      FOREIGN KEY (animal_id) REFERENCES animal(id),
    CONSTRAINT fk_agendamento_veterinario
      FOREIGN KEY (veterinario_id) REFERENCES veterinario(id),
    INDEX idx_agendamento_data_veterinario (data_hora, veterinario_id, status)
);

CREATE TABLE IF NOT EXISTS atendimento (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    diagnostico TEXT NOT NULL,
    procedimento TEXT,
    observacoes TEXT,
    agendamento_id BIGINT NOT NULL UNIQUE,
    CONSTRAINT fk_atendimento_agendamento
      FOREIGN KEY (agendamento_id) REFERENCES agendamento(id)
);
