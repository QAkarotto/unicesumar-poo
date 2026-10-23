BEGIN TRANSACTION;
INSERT INTO aluno (registro_academico, nome, email) VALUES
    ('RA2026001', 'Aluno Exemplo Um', 'aluno1@example.com'),
    ('RA2026002', 'Aluno Exemplo Dois', 'aluno2@example.com');

INSERT INTO disciplina (codigo, nome, carga_horaria) VALUES
    ('POO', 'Programação Orientada a Objetos', 80),
    ('BD', 'Banco de Dados', 80);
COMMIT;
