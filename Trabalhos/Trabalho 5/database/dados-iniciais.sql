INSERT IGNORE INTO aluno (ra, nome, email) VALUES
    ('RA2026001', 'Paola Oliveira', 'paola.oliveira@email.com'),
    ('RA2026002', 'Bruno Santos', 'bruno.santos@email.com'),
    ('RA2026003', 'Jonathan Domingues', 'jonathan.domingues@email.com');

INSERT IGNORE INTO disciplina (codigo, nome, carga_horaria) VALUES
    ('POO', 'Programação Orientada a Objetos', 80),
    ('BD', 'Banco de Dados', 80),
    ('GPT', 'Gestão de Projetos Tecnológicos', 80);

INSERT IGNORE INTO turma (codigo, ano, semestre) VALUES
    ('ESOFT4S-NB', 2026, 'QUARTO'),
    ('ADSIS4S', 2026, 'QUARTO'),
    ('ADSIS6S', 2026, 'SEXTO');

INSERT IGNORE INTO oferta_disciplina (id, turma_codigo, disciplina_codigo) VALUES
    (1, 'ESOFT4S-NB', 'POO'),
    (2, 'ESOFT4S-NB', 'BD'),
    (3, 'ADSIS4S', 'POO'),
    (4, 'ADSIS6S', 'GPT');
