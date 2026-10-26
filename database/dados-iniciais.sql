INSERT INTO alunos (registro_academico, nome, email)
VALUES ('RA2026001', 'Paola Oliveira', 'paola.oliveira@email.com'),
       ('RA2026002', 'Bruno Santos', 'bruno.santos@email.com')
ON CONFLICT (registro_academico) DO NOTHING;

INSERT INTO disciplinas (codigo, nome, carga_horaria)
VALUES ('POO', 'Programação Orientada a Objetos', 80),
       ('BD', 'Banco de Dados', 80)
ON CONFLICT (codigo) DO NOTHING;
