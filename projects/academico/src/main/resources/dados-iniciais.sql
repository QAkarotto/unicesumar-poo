INSERT OR IGNORE INTO turmas (codigo, ano, semestre) VALUES
('ADSIS2S', 2026, 2),
('ADSIS3S', 2026, 2),
('ADSIS4S', 2026, 2);


INSERT OR IGNORE INTO alunos
(registro_academico, nome, email) VALUES
('RA2026001', 'Bruno Santos', 'bruno@email.com'),
('RA2026002', 'Paola Oliveira', 'paola@email.com'),
('RA2026003', 'Carlos Silva', 'carlos@email.com'),
('RA2026004', 'Mariana Souza', 'mariana@email.com');



INSERT OR IGNORE INTO disciplinas
(codigo, nome, carga_horaria) VALUES
('DISC001', 'Banco de Dados', 80),
('DISC002', 'Engenharia de Software', 80),
('DISC003', 'Programação Orientada a Objetos (POO)', 80),
('DISC004', 'Estrutura de Dados', 80),
('DISC005', 'Desenvolvimento Web', 80);



INSERT OR IGNORE INTO ofertas_disciplinas
(codigo_turma, codigo_disciplina) VALUES
('ADSIS2S', 'DISC001'),
('ADSIS2S', 'DISC002'),
('ADSIS3S', 'DISC002'),
('ADSIS3S', 'DISC003'),
('ADSIS4S', 'DISC001'),
('ADSIS4S', 'DISC003'),
('ADSIS4S', 'DISC004'),
('ADSIS4S', 'DISC005');