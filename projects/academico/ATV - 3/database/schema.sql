CREATE TABLE IF NOT EXISTS aluno (
    identificador_academico TEXT PRIMARY KEY,
    nome TEXT NOT NULL,
    email TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS disciplina (
    codigo TEXT PRIMARY KEY,
    nome TEXT NOT NULL,
    carga_horaria INTEGER NOT NULL
);
