-- Dados fictícios preservados do protótipo. Aplicado automaticamente em banco vazio.
-- Para reaplicar manualmente, use apenas um banco sem os dados de demonstração.
SET NAMES utf8mb4;

INSERT INTO usuarios (
    id,
    nome,
    perfil,
    telefone,
    email,
    municipio,
    consentimento_em
) VALUES (
    'u1',
    'Alexandre Gaia',
    'PRODUTOR',
    '(42) 99812-4410',
    'ale@exemplo.com',
    'Pirai do Sul',
    '2026-09-02 10:14:00'
);

INSERT INTO usuarios (
    id,
    nome,
    perfil,
    telefone,
    email,
    municipio,
    consentimento_em
) VALUES (
    'u2',
    'Joao Guimarães',
    'PRODUTOR',
    '(42) 99907-3321',
    'joaoguima@exemplo.com',
    'Irati',
    '2026-08-21 16:40:00'
);

INSERT INTO usuarios (
    id,
    nome,
    perfil,
    telefone,
    email,
    municipio,
    consentimento_em
) VALUES (
    'u3',
    'Daniel Ciriaco',
    'TECNICO',
    '(42) 99133-0287',
    'dani.@exemplo.com',
    'Ponta Grossa',
    '2026-08-10 09:00:00'
);

INSERT INTO usuarios (
    id,
    nome,
    perfil,
    telefone,
    email,
    municipio,
    consentimento_em
) VALUES (
    'u4',
    'Gustavo Pettenom',
    'TECNICO',
    '(42) 99840-1175',
    'gusta@exemplo.com',
    'Castro',
    '2026-08-10 09:20:00'
);

INSERT INTO usuarios (
    id,
    nome,
    perfil,
    telefone,
    email,
    municipio,
    consentimento_em
) VALUES (
    'u5',
    'Joao Vitor dos Santos',
    'TECNICO',
    '(42) 99761-5502',
    'goku.o@exemplo.com',
    'Carambeí',
    '2026-08-11 14:05:00'
);

INSERT INTO usuarios (
    id,
    nome,
    perfil,
    telefone,
    email,
    municipio,
    consentimento_em
) VALUES (
    'u6',
    'Cristian da Silva',
    'ADMIN',
    '(42) 3220-1800',
    'coordenacao@exemplo.com',
    'Ponta Grossa',
    '2026-08-01 08:00:00'
);

INSERT INTO culturas (id, nome)
VALUES ('c1', 'Soja');

INSERT INTO culturas (id, nome)
VALUES ('c2', 'Milho');

INSERT INTO culturas (id, nome)
VALUES ('c3', 'Trigo');

INSERT INTO culturas (id, nome)
VALUES ('c4', 'Feijão');

INSERT INTO culturas (id, nome)
VALUES ('c5', 'Aveia');

INSERT INTO culturas (id, nome)
VALUES ('c6', 'Cevada');

INSERT INTO propriedades (
    id,
    produtor_id,
    nome,
    municipio,
    area_ha
) VALUES (
    'p1',
    'u1',
    'Sitio Machadinho',
    'Pirai do Sul',
    48
);

INSERT INTO propriedade_culturas (propriedade_id, cultura_id)
VALUES ('p1', 'c1');

INSERT INTO propriedade_culturas (propriedade_id, cultura_id)
VALUES ('p1', 'c2');

INSERT INTO propriedade_culturas (propriedade_id, cultura_id)
VALUES ('p1', 'c4');

INSERT INTO propriedades (
    id,
    produtor_id,
    nome,
    municipio,
    area_ha
) VALUES (
    'p2',
    'u1',
    'Chácara Três Pinheiros',
    'Carambeí',
    12
);

INSERT INTO propriedade_culturas (propriedade_id, cultura_id)
VALUES ('p2', 'c4');

INSERT INTO propriedades (
    id,
    produtor_id,
    nome,
    municipio,
    area_ha
) VALUES (
    'p3',
    'u2',
    'Fazenda Santa Rita',
    'Tibagi',
    210
);

INSERT INTO propriedade_culturas (propriedade_id, cultura_id)
VALUES ('p3', 'c1');

INSERT INTO propriedade_culturas (propriedade_id, cultura_id)
VALUES ('p3', 'c3');

INSERT INTO propriedade_culturas (propriedade_id, cultura_id)
VALUES ('p3', 'c5');

INSERT INTO tecnicos (
    id,
    usuario_id,
    formacao,
    registro,
    ativo
) VALUES (
    't1',
    'u3',
    'Engenheiro agrônomo',
    'CREA-PR 118245/D',
    TRUE
);

INSERT INTO tecnico_culturas (tecnico_id, cultura_id)
VALUES ('t1', 'c1');

INSERT INTO tecnico_culturas (tecnico_id, cultura_id)
VALUES ('t1', 'c2');

INSERT INTO tecnico_culturas (tecnico_id, cultura_id)
VALUES ('t1', 'c4');

INSERT INTO tecnicos (
    id,
    usuario_id,
    formacao,
    registro,
    ativo
) VALUES (
    't2',
    'u4',
    'Técnico agrícola',
    'CRT 04512',
    TRUE
);

INSERT INTO tecnico_culturas (tecnico_id, cultura_id)
VALUES ('t2', 'c3');

INSERT INTO tecnico_culturas (tecnico_id, cultura_id)
VALUES ('t2', 'c5');

INSERT INTO tecnico_culturas (tecnico_id, cultura_id)
VALUES ('t2', 'c6');

INSERT INTO tecnico_culturas (tecnico_id, cultura_id)
VALUES ('t2', 'c2');

INSERT INTO tecnicos (
    id,
    usuario_id,
    formacao,
    registro,
    ativo
) VALUES (
    't3',
    'u5',
    'Engenheiro agrônomo',
    'CREA-PR 131877/D',
    FALSE
);

INSERT INTO tecnico_culturas (tecnico_id, cultura_id)
VALUES ('t3', 'c1');

INSERT INTO tecnico_culturas (tecnico_id, cultura_id)
VALUES ('t3', 'c3');

INSERT INTO solicitacoes (
    id,
    protocolo,
    produtor_id,
    propriedade_id,
    cultura_id,
    tecnico_id,
    estadio,
    descricao,
    status,
    criada_em
) VALUES (
    's1',
    'AGA-2026-0131',
    'u2',
    'p3',
    'c3',
    't2',
    'Maturação',
    'O vento forte de sábado acamou uma parte do trigo perto da estrada. Quero saber se compensa antecipar a colheita nessa área.',
    'AVALIADA',
    '2026-10-05 07:50:00'
);

INSERT INTO historico_solicitacao (
    solicitacao_id,
    ordem,
    em,
    status,
    autor_id,
    descricao
) VALUES (
    's1',
    0,
    '2026-10-05 07:50:00',
    'ABERTA',
    'u2',
    'Solicitação aberta pelo produtor'
);

INSERT INTO historico_solicitacao (
    solicitacao_id,
    ordem,
    em,
    status,
    autor_id,
    descricao
) VALUES (
    's1',
    1,
    '2026-10-05 09:12:00',
    'ATRIBUIDA',
    'u6',
    'Atribuída a Gustavo Pettenom'
);

INSERT INTO historico_solicitacao (
    solicitacao_id,
    ordem,
    em,
    status,
    autor_id,
    descricao
) VALUES (
    's1',
    2,
    '2026-10-06 08:30:00',
    'EM_ATENDIMENTO',
    'u4',
    'Atendimento iniciado'
);

INSERT INTO historico_solicitacao (
    solicitacao_id,
    ordem,
    em,
    status,
    autor_id,
    descricao
) VALUES (
    's1',
    3,
    '2026-10-06 15:05:00',
    'CONCLUIDA',
    'u4',
    'Atendimento registrado'
);

INSERT INTO historico_solicitacao (
    solicitacao_id,
    ordem,
    em,
    status,
    autor_id,
    descricao
) VALUES (
    's1',
    4,
    '2026-10-07 19:22:00',
    'AVALIADA',
    'u2',
    'Avaliação enviada (5 de 5)'
);

INSERT INTO solicitacoes (
    id,
    protocolo,
    produtor_id,
    propriedade_id,
    cultura_id,
    tecnico_id,
    estadio,
    descricao,
    status,
    criada_em
) VALUES (
    's2',
    'AGA-2026-0136',
    'u1',
    'p1',
    'c2',
    't1',
    'V5',
    'Algumas plantas com as folhas raspadas e furos no cartucho. Achei umas lagartas pequenas dentro. Parece que está aumentando de um dia pro outro.',
    'CONCLUIDA',
    '2026-10-08 06:40:00'
);

INSERT INTO solicitacao_fotos (
    solicitacao_id,
    ordem,
    conteudo
) VALUES (
    's2',
    0,
    'data:image/svg+xml;charset=utf-8,%3Csvg%20xmlns%3D''http%3A%2F%2Fwww.w3.org%2F2000%2Fsvg''%20viewBox%3D''0%200%20160%20120''%3E%3Crect%20width%3D''160''%20height%3D''120''%20fill%3D''%238a7a5c''%2F%3E%3Crect%20y%3D''74''%20width%3D''160''%20height%3D''46''%20fill%3D''%236f6147''%2F%3E%3Cpath%20d%3D''M8%2098%20C%2050%2072%2C%20110%2032%2C%20156%2014%20C%20122%2046%2C%2072%2086%2C%2012%20106%20Z''%20fill%3D''%236e9a3c''%2F%3E%3Cpath%20d%3D''M10%20102%20L%20154%2015''%20stroke%3D''%231d3b18''%20stroke-opacity%3D''.45''%20stroke-width%3D''1.5''%2F%3E%3Cellipse%20cx%3D''58''%20cy%3D''76''%20rx%3D''6''%20ry%3D''3''%20fill%3D''%238a7a5c''%2F%3E%3Cellipse%20cx%3D''90''%20cy%3D''55''%20rx%3D''5''%20ry%3D''3''%20fill%3D''%238a7a5c''%2F%3E%3Cellipse%20cx%3D''120''%20cy%3D''38''%20rx%3D''6''%20ry%3D''3''%20fill%3D''%238a7a5c''%2F%3E%3Cellipse%20cx%3D''74''%20cy%3D''66''%20rx%3D''9''%20ry%3D''3''%20fill%3D''%23d9e3a8''%2F%3E%3Cellipse%20cx%3D''104''%20cy%3D''47''%20rx%3D''8''%20ry%3D''2.5''%20fill%3D''%23d9e3a8''%2F%3E%3C%2Fsvg%3E'
);

INSERT INTO historico_solicitacao (
    solicitacao_id,
    ordem,
    em,
    status,
    autor_id,
    descricao
) VALUES (
    's2',
    0,
    '2026-10-08 06:40:00',
    'ABERTA',
    'u1',
    'Solicitação aberta pelo produtor'
);

INSERT INTO historico_solicitacao (
    solicitacao_id,
    ordem,
    em,
    status,
    autor_id,
    descricao
) VALUES (
    's2',
    1,
    '2026-10-08 08:05:00',
    'ATRIBUIDA',
    'u6',
    'Atribuída a Daniel Ciriaco'
);

INSERT INTO historico_solicitacao (
    solicitacao_id,
    ordem,
    em,
    status,
    autor_id,
    descricao
) VALUES (
    's2',
    2,
    '2026-10-09 07:45:00',
    'EM_ATENDIMENTO',
    'u3',
    'Atendimento iniciado'
);

INSERT INTO historico_solicitacao (
    solicitacao_id,
    ordem,
    em,
    status,
    autor_id,
    descricao
) VALUES (
    's2',
    3,
    '2026-10-09 11:20:00',
    'CONCLUIDA',
    'u3',
    'Atendimento registrado'
);

INSERT INTO solicitacoes (
    id,
    protocolo,
    produtor_id,
    propriedade_id,
    cultura_id,
    tecnico_id,
    estadio,
    descricao,
    status,
    criada_em
) VALUES (
    's3',
    'AGA-2026-0139',
    'u2',
    'p3',
    'c3',
    't2',
    'Enchimento de grãos',
    'Espigas com grãos manchados e algumas espigas esbranquiçadas no meio da lavoura, principalmente depois das chuvas.',
    'EM_ATENDIMENTO',
    '2026-10-13 17:10:00'
);

INSERT INTO solicitacao_fotos (
    solicitacao_id,
    ordem,
    conteudo
) VALUES (
    's3',
    0,
    'data:image/svg+xml;charset=utf-8,%3Csvg%20xmlns%3D''http%3A%2F%2Fwww.w3.org%2F2000%2Fsvg''%20viewBox%3D''0%200%20160%20120''%3E%3Crect%20width%3D''160''%20height%3D''120''%20fill%3D''%238a7a5c''%2F%3E%3Crect%20y%3D''74''%20width%3D''160''%20height%3D''46''%20fill%3D''%236f6147''%2F%3E%3Cpath%20d%3D''M8%2098%20C%2050%2072%2C%20110%2032%2C%20156%2014%20C%20122%2046%2C%2072%2086%2C%2012%20106%20Z''%20fill%3D''%23a3a05a''%2F%3E%3Cpath%20d%3D''M10%20102%20L%20154%2015''%20stroke%3D''%231d3b18''%20stroke-opacity%3D''.45''%20stroke-width%3D''1.5''%2F%3E%3Cellipse%20cx%3D''60''%20cy%3D''76''%20rx%3D''6''%20ry%3D''3''%20fill%3D''%23c27b8a''%2F%3E%3Cellipse%20cx%3D''96''%20cy%3D''52''%20rx%3D''7''%20ry%3D''3''%20fill%3D''%23e8e3c8''%2F%3E%3Cellipse%20cx%3D''124''%20cy%3D''34''%20rx%3D''5''%20ry%3D''2.5''%20fill%3D''%23c27b8a''%2F%3E%3C%2Fsvg%3E'
);

INSERT INTO historico_solicitacao (
    solicitacao_id,
    ordem,
    em,
    status,
    autor_id,
    descricao
) VALUES (
    's3',
    0,
    '2026-10-13 17:10:00',
    'ABERTA',
    'u2',
    'Solicitação aberta pelo produtor'
);

INSERT INTO historico_solicitacao (
    solicitacao_id,
    ordem,
    em,
    status,
    autor_id,
    descricao
) VALUES (
    's3',
    1,
    '2026-10-14 08:00:00',
    'ATRIBUIDA',
    'u6',
    'Atribuída a Gustavo Pettenom'
);

INSERT INTO historico_solicitacao (
    solicitacao_id,
    ordem,
    em,
    status,
    autor_id,
    descricao
) VALUES (
    's3',
    2,
    '2026-10-16 09:15:00',
    'EM_ATENDIMENTO',
    'u4',
    'Atendimento iniciado'
);

INSERT INTO solicitacoes (
    id,
    protocolo,
    produtor_id,
    propriedade_id,
    cultura_id,
    tecnico_id,
    estadio,
    descricao,
    status,
    criada_em
) VALUES (
    's4',
    'AGA-2026-0141',
    'u2',
    'p3',
    'c1',
    't1',
    'VE',
    'Falha na emergência em algumas linhas. Parece que a semente não nasceu direito na parte mais baixa do talhão.',
    'ATRIBUIDA',
    '2026-10-15 10:25:00'
);

INSERT INTO historico_solicitacao (
    solicitacao_id,
    ordem,
    em,
    status,
    autor_id,
    descricao
) VALUES (
    's4',
    0,
    '2026-10-15 10:25:00',
    'ABERTA',
    'u2',
    'Solicitação aberta pelo produtor'
);

INSERT INTO historico_solicitacao (
    solicitacao_id,
    ordem,
    em,
    status,
    autor_id,
    descricao
) VALUES (
    's4',
    1,
    '2026-10-15 13:40:00',
    'ATRIBUIDA',
    'u6',
    'Atribuída a Daniel Ciriaco'
);

INSERT INTO solicitacoes (
    id,
    protocolo,
    produtor_id,
    propriedade_id,
    cultura_id,
    tecnico_id,
    estadio,
    descricao,
    status,
    criada_em
) VALUES (
    's5',
    'AGA-2026-0142',
    'u1',
    'p2',
    'c4',
    NULL,
    'V2',
    'Plantas novas com as folhas enroladas e uns bichinhos brancos bem pequenos embaixo das folhas.',
    'ABERTA',
    '2026-10-17 18:02:00'
);

INSERT INTO solicitacao_fotos (
    solicitacao_id,
    ordem,
    conteudo
) VALUES (
    's5',
    0,
    'data:image/svg+xml;charset=utf-8,%3Csvg%20xmlns%3D''http%3A%2F%2Fwww.w3.org%2F2000%2Fsvg''%20viewBox%3D''0%200%20160%20120''%3E%3Crect%20width%3D''160''%20height%3D''120''%20fill%3D''%238a7a5c''%2F%3E%3Crect%20y%3D''74''%20width%3D''160''%20height%3D''46''%20fill%3D''%236f6147''%2F%3E%3Cpath%20d%3D''M80%20110%20C%2028%20102%2C%2016%2052%2C%2046%2026%20C%2064%2012%2C%2098%2012%2C%20116%2026%20C%20146%2052%2C%20132%20102%2C%2080%20110%20Z''%20fill%3D''%234f8a3a''%2F%3E%3Cpath%20d%3D''M80%20110%20L%2080%2020''%20stroke%3D''%231d3b18''%20stroke-opacity%3D''.45''%20stroke-width%3D''1.5''%2F%3E%3Cellipse%20cx%3D''64''%20cy%3D''50''%20rx%3D''1.8''%20ry%3D''1.8''%20fill%3D''%23f4f4ee''%2F%3E%3Cellipse%20cx%3D''70''%20cy%3D''56''%20rx%3D''1.6''%20ry%3D''1.6''%20fill%3D''%23f4f4ee''%2F%3E%3Cellipse%20cx%3D''92''%20cy%3D''48''%20rx%3D''1.8''%20ry%3D''1.8''%20fill%3D''%23f4f4ee''%2F%3E%3Cellipse%20cx%3D''86''%20cy%3D''70''%20rx%3D''1.6''%20ry%3D''1.6''%20fill%3D''%23f4f4ee''%2F%3E%3Cellipse%20cx%3D''60''%20cy%3D''78''%20rx%3D''1.8''%20ry%3D''1.8''%20fill%3D''%23f4f4ee''%2F%3E%3Cellipse%20cx%3D''100''%20cy%3D''82''%20rx%3D''1.6''%20ry%3D''1.6''%20fill%3D''%23f4f4ee''%2F%3E%3Cellipse%20cx%3D''96''%20cy%3D''60''%20rx%3D''1.7''%20ry%3D''1.7''%20fill%3D''%23f4f4ee''%2F%3E%3C%2Fsvg%3E'
);

INSERT INTO historico_solicitacao (
    solicitacao_id,
    ordem,
    em,
    status,
    autor_id,
    descricao
) VALUES (
    's5',
    0,
    '2026-10-17 18:02:00',
    'ABERTA',
    'u1',
    'Solicitação aberta pelo produtor'
);

INSERT INTO atendimentos (
    id,
    solicitacao_id,
    tecnico_id,
    data,
    modalidade,
    diagnostico,
    recomendacao
) VALUES (
    'a1',
    's1',
    't2',
    '2026-10-06',
    'VISITA',
    'Acamamento em cerca de 4 ha na bordadura próxima à estrada, grãos já em maturação fisiológica.',
    'Colher primeiro a área acamada assim que a umidade permitir, com velocidade reduzida da colhedora para diminuir perdas.'
);

INSERT INTO atendimentos (
    id,
    solicitacao_id,
    tecnico_id,
    data,
    modalidade,
    diagnostico,
    recomendacao
) VALUES (
    'a2',
    's2',
    't1',
    '2026-10-09',
    'VISITA',
    'Ataque de lagarta-do-cartucho em reboleiras, com cerca de 15% das plantas com folhas raspadas.',
    'Monitorar 5 pontos da lavoura nesta semana. Se passar do nível de controle, seguir o receituário agronômico emitido na visita.'
);

INSERT INTO avaliacoes (
    id,
    solicitacao_id,
    nota,
    comentario,
    em
) VALUES (
    'v1',
    's1',
    5,
    'Veio no dia seguinte e explicou bem o que fazer. Resolveu.',
    '2026-10-07 19:22:00'
);

UPDATE sequencia_protocolo SET ultimo = 142 WHERE id = 1;
