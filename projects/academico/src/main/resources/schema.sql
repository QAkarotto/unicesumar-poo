BEGIN TRANSACTION;
DROP TABLE IF EXISTS alunos;
DROP TABLE IF EXISTS disciplina;

CREATE TABLE alunos (
    registro_academico TEXT PRIMARY KEY NOT NULL,
    nome TEXT NOT NULL,
    email TEXT NOT NULL
);

CREATE TABLE disciplina (
    codigo TEXT PRIMARY KEY NOT NULL,
    nome TEXT NOT NULL,
    carga_horaria INTEGER NOT NULL CHECK (carga_horaria > 0)
);
COMMIT;