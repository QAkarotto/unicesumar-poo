-- ===================================================================
-- Script DML: Carga de Dados Iniciais (MySQL)
-- Atividade Final - 2BIM: Sistema Acadêmico Full Stack
-- ===================================================================

USE sistema_academico;

-- 1. Alunos iniciais
INSERT INTO aluno (registro_academico, nome, email) VALUES
    ('RA2026001', 'Aluno Exemplo Um', 'aluno1@example.com'),
    ('RA2026002', 'Aluno Exemplo Dois', 'aluno2@example.com'),
    ('RA2026003', 'Carlos Eduardo Silva', 'carlos.silva@example.com'),
    ('RA2026004', 'Mariana Santos', 'mariana.santos@example.com');

-- 2. Disciplinas iniciais
INSERT INTO disciplina (codigo, nome, carga_horaria) VALUES
    ('POO', 'Programação Orientada a Objetos', 80),
    ('BD', 'Banco de Dados', 80),
    ('WEB', 'Desenvolvimento Web com React', 60),
    ('ES', 'Engenharia de Software', 80);

-- 3. Turmas
INSERT INTO turma (codigo, ano, semestre) VALUES
    ('ESOFT4S-NA', 2026, 'SEGUNDO'),
    ('ADSIS4S-NB', 2026, 'SEGUNDO');

-- 4. Ofertas de Disciplinas
INSERT INTO oferta_disciplina (codigo, disciplina_id, turma_id) VALUES
    ('POO-2026-2', 1, 1),
    ('BD-2026-2', 2, 1),
    ('WEB-2026-2', 3, 1),
    ('ES-2026-2', 4, 2);

-- 5. Matrículas iniciais
INSERT INTO matricula (codigo, aluno_ra, oferta_id, situacao, resultado) VALUES
    ('MAT-001', 'RA2026001', 1, 'ATIVA', NULL);
