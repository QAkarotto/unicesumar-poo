CREATE TABLE IF NOT EXISTS alunos (
    ra TEXT PRIMARY KEY,
    nome TEXT NOT NULL,
    email TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS disciplinas (
    codigo TEXT PRIMARY KEY,
    nome TEXT NOT NULL,
    carga_horaria INTEGER NOT NULL CHECK (carga_horaria > 0)
);
