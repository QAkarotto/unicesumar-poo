-- Esquema do banco SQLite do Sistema Acadêmico (Atividade 03 - 2BIM).
-- Pode ser executado várias vezes sem apagar dados já existentes.

CREATE TABLE IF NOT EXISTS aluno (
    registro_academico TEXT PRIMARY KEY,
    nome               TEXT NOT NULL,
    email              TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS disciplina (
    codigo        TEXT PRIMARY KEY,
    nome          TEXT NOT NULL,
    carga_horaria INTEGER NOT NULL CHECK (carga_horaria > 0)
);
