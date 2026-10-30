CREATE TABLE IF NOT EXISTS turmas (
                                      codigo TEXT PRIMARY KEY,
                                      ano INTEGER NOT NULL,
                                      semestre INTEGER NOT NULL
                                      CHECK (semestre IN (1, 2))
    );

CREATE TABLE IF NOT EXISTS alunos (
                                      registro_academico TEXT PRIMARY KEY,
                                      nome TEXT NOT NULL,
                                      email TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS disciplinas (
                                           codigo TEXT PRIMARY KEY,
                                           nome TEXT NOT NULL,
                                           carga_horaria INTEGER NOT NULL
                                           CHECK (carga_horaria > 0)
    );

CREATE TABLE IF NOT EXISTS ofertas_disciplinas (
                                                   codigo_turma TEXT NOT NULL,
                                                   codigo_disciplina TEXT NOT NULL,
                                                   PRIMARY KEY (codigo_turma, codigo_disciplina),
    FOREIGN KEY (codigo_turma) REFERENCES turmas(codigo),
    FOREIGN KEY (codigo_disciplina) REFERENCES disciplinas(codigo)
    );