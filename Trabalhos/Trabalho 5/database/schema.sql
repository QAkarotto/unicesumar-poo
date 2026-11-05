CREATE TABLE IF NOT EXISTS aluno (
    ra VARCHAR(20) PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS disciplina (
    codigo VARCHAR(20) PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    carga_horaria INT NOT NULL CHECK (carga_horaria > 0)
);

CREATE TABLE IF NOT EXISTS turma (
    codigo VARCHAR(20) PRIMARY KEY,
    ano INT NOT NULL,
    semestre VARCHAR(10) NOT NULL
);

CREATE TABLE IF NOT EXISTS oferta_disciplina (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    turma_codigo VARCHAR(20) NOT NULL,
    disciplina_codigo VARCHAR(20) NOT NULL,
    FOREIGN KEY (turma_codigo) REFERENCES turma (codigo),
    FOREIGN KEY (disciplina_codigo) REFERENCES disciplina (codigo),
    UNIQUE (turma_codigo, disciplina_codigo)
);

CREATE TABLE IF NOT EXISTS matricula (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    aluno_ra VARCHAR(20) NOT NULL,
    oferta_id BIGINT NOT NULL,
    resultado VARCHAR(20) NOT NULL,
    FOREIGN KEY (aluno_ra) REFERENCES aluno (ra),
    FOREIGN KEY (oferta_id) REFERENCES oferta_disciplina (id),
    UNIQUE (aluno_ra, oferta_id)
);
