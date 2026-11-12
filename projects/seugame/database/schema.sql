-- SeuGame - modelo relacional para MySQL 8.4 (InnoDB / utf8mb4).
-- Execute uma única vez em um banco vazio, selecionado pelo cliente MySQL.
-- Sem CREATE DATABASE, DROP, dados iniciais, triggers ou desativação de FKs.
-- Requer modo SQL estrito. Credenciais são fornecidas fora deste arquivo.

SET NAMES utf8mb4 COLLATE utf8mb4_0900_as_ci;

CREATE TABLE usuarios (
    id BIGINT NOT NULL AUTO_INCREMENT,
    nome VARCHAR(100) NOT NULL,
    usuario VARCHAR(50) NOT NULL,
    email VARCHAR(150) NOT NULL,
    senha_hash VARCHAR(255) CHARACTER SET ascii COLLATE ascii_bin NOT NULL,
    CONSTRAINT pk_usuarios PRIMARY KEY (id),
    CONSTRAINT uq_usuarios_usuario UNIQUE (usuario),
    CONSTRAINT uq_usuarios_email UNIQUE (email),
    CONSTRAINT ck_usuarios_nome CHECK (CHAR_LENGTH(TRIM(nome)) BETWEEN 3 AND 100),
    CONSTRAINT ck_usuarios_usuario CHECK (
        REGEXP_LIKE(usuario, '^[a-zA-Z0-9_.-]{3,50}$', 'c')
    ),
    CONSTRAINT ck_usuarios_email CHECK (
        CHAR_LENGTH(email) >= 3
        AND NOT REGEXP_LIKE(email, '^[[:space:]]|[[:space:]]$')
    ),
    CONSTRAINT ck_usuarios_hash CHECK (CHAR_LENGTH(senha_hash) > 0)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_as_ci;

CREATE TABLE torneios (
    id BIGINT NOT NULL AUTO_INCREMENT,
    usuario_id BIGINT NOT NULL,
    nome VARCHAR(120) NOT NULL,
    descricao VARCHAR(2000) NOT NULL DEFAULT '',
    tipo VARCHAR(11) COLLATE utf8mb4_0900_as_cs NOT NULL,
    data_inicio DATE NOT NULL,
    status VARCHAR(10) COLLATE utf8mb4_0900_as_cs NOT NULL,
    campeao_id BIGINT NULL,
    criado_em DATETIME(6) NOT NULL DEFAULT (UTC_TIMESTAMP(6)),
    CONSTRAINT pk_torneios PRIMARY KEY (id),
    CONSTRAINT fk_torneios_usuario FOREIGN KEY (usuario_id)
        REFERENCES usuarios (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT ck_torneios_nome CHECK (CHAR_LENGTH(TRIM(nome)) BETWEEN 3 AND 120),
    CONSTRAINT ck_torneios_tipo CHECK (tipo IN ('TABELA', 'CHAVEAMENTO')),
    CONSTRAINT ck_torneios_status CHECK (status IN ('FUTURO', 'ANDAMENTO', 'FINALIZADO')),
    CONSTRAINT ck_torneios_campeao_status CHECK (
        (status = 'FINALIZADO' AND campeao_id IS NOT NULL)
        OR (status <> 'FINALIZADO' AND campeao_id IS NULL)
    ),
    INDEX idx_torneios_autor_criacao (usuario_id, criado_em, id),
    INDEX idx_torneios_status_data (status, data_inicio, id),
    INDEX idx_torneios_campeao (id, campeao_id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_as_ci;

CREATE TABLE participantes (
    id BIGINT NOT NULL AUTO_INCREMENT,
    torneio_id BIGINT NOT NULL,
    nome VARCHAR(100) NOT NULL,
    ordem_sorteio INT NOT NULL,
    CONSTRAINT pk_participantes PRIMARY KEY (id),
    -- Chave candidata explícita para todas as referências compostas abaixo.
    CONSTRAINT uq_participantes_torneio_id UNIQUE (torneio_id, id),
    CONSTRAINT uq_participantes_nome UNIQUE (torneio_id, nome),
    CONSTRAINT uq_participantes_ordem UNIQUE (torneio_id, ordem_sorteio),
    CONSTRAINT fk_participantes_torneio FOREIGN KEY (torneio_id)
        REFERENCES torneios (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
    -- Armazenamento canônico: o backend deve aparar espaços antes de persistir.
    -- A collation ignora caixa, preserva acentos e os CHECKs rejeitam bordas vazias.
    CONSTRAINT ck_participantes_nome CHECK (
        CHAR_LENGTH(nome) > 0
        AND NOT REGEXP_LIKE(nome, '^[[:space:]]|[[:space:]]$')
    ),
    CONSTRAINT ck_participantes_ordem CHECK (ordem_sorteio > 0)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_as_ci;

-- Resolve o ciclo Torneio -> campeão -> Participante -> Torneio.
-- O torneio é inserido sem campeão; o encerramento define status e campeão juntos.
ALTER TABLE torneios
    ADD CONSTRAINT fk_torneios_campeao FOREIGN KEY (id, campeao_id)
        REFERENCES participantes (torneio_id, id)
        ON DELETE RESTRICT ON UPDATE RESTRICT;

CREATE TABLE partidas (
    id BIGINT NOT NULL AUTO_INCREMENT,
    torneio_id BIGINT NOT NULL,
    participante1_id BIGINT NULL,
    participante2_id BIGINT NULL,
    vencedor_id BIGINT NULL,
    rodada INT NOT NULL,
    ordem INT NOT NULL,
    fase VARCHAR(5) COLLATE utf8mb4_0900_as_cs NOT NULL,
    placar1 SMALLINT NULL,
    placar2 SMALLINT NULL,
    CONSTRAINT pk_partidas PRIMARY KEY (id),
    CONSTRAINT uq_partidas_posicao UNIQUE (torneio_id, fase, rodada, ordem),
    CONSTRAINT fk_partidas_torneio FOREIGN KEY (torneio_id)
        REFERENCES torneios (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT fk_partidas_participante1 FOREIGN KEY (torneio_id, participante1_id)
        REFERENCES participantes (torneio_id, id) ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT fk_partidas_participante2 FOREIGN KEY (torneio_id, participante2_id)
        REFERENCES participantes (torneio_id, id) ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT fk_partidas_vencedor FOREIGN KEY (torneio_id, vencedor_id)
        REFERENCES participantes (torneio_id, id) ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT ck_partidas_posicao CHECK (rodada > 0 AND ordem > 0),
    CONSTRAINT ck_partidas_fase CHECK (fase IN ('LIGA', 'CHAVE')),
    CONSTRAINT ck_partidas_distintos CHECK (
        participante1_id IS NULL OR participante2_id IS NULL
        OR participante1_id <> participante2_id
    ),
    CONSTRAINT ck_partidas_vagas CHECK (
        (fase = 'LIGA' AND participante1_id IS NOT NULL AND participante2_id IS NOT NULL)
        OR (fase = 'CHAVE' AND (
            rodada > 1 OR participante1_id IS NOT NULL OR participante2_id IS NOT NULL
        ))
    ),
    CONSTRAINT ck_partidas_placares CHECK (
        (placar1 IS NULL AND placar2 IS NULL)
        OR (placar1 IS NOT NULL AND placar2 IS NOT NULL
            AND placar1 BETWEEN 0 AND 999 AND placar2 BETWEEN 0 AND 999
            AND participante1_id IS NOT NULL AND participante2_id IS NOT NULL)
    ),
    CONSTRAINT ck_partidas_resultado CHECK (
        (placar1 IS NULL AND placar2 IS NULL AND (
            vencedor_id IS NULL
            OR (fase = 'CHAVE' AND rodada = 1 AND (
                (participante1_id IS NOT NULL AND participante2_id IS NULL
                    AND vencedor_id = participante1_id)
                OR (participante2_id IS NOT NULL AND participante1_id IS NULL
                    AND vencedor_id = participante2_id)
            ))
        ))
        OR (placar1 IS NOT NULL AND placar2 IS NOT NULL AND (
            (fase = 'LIGA' AND placar1 = placar2 AND vencedor_id IS NULL)
            OR (placar1 > placar2 AND vencedor_id IS NOT NULL AND vencedor_id = participante1_id)
            OR (placar2 > placar1 AND vencedor_id IS NOT NULL AND vencedor_id = participante2_id)
        ))
    ),
    INDEX idx_partidas_participante1 (torneio_id, participante1_id),
    INDEX idx_partidas_participante2 (torneio_id, participante2_id),
    INDEX idx_partidas_vencedor (torneio_id, vencedor_id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_as_ci;

CREATE TABLE classificacao (
    id BIGINT NOT NULL AUTO_INCREMENT,
    torneio_id BIGINT NOT NULL,
    participante_id BIGINT NOT NULL,
    jogos INT NOT NULL DEFAULT 0,
    vitorias INT NOT NULL DEFAULT 0,
    empates INT NOT NULL DEFAULT 0,
    derrotas INT NOT NULL DEFAULT 0,
    gols_pro INT NOT NULL DEFAULT 0,
    gols_contra INT NOT NULL DEFAULT 0,
    saldo INT NOT NULL DEFAULT 0,
    pontos INT NOT NULL DEFAULT 0,
    CONSTRAINT pk_classificacao PRIMARY KEY (id),
    CONSTRAINT uq_classificacao_participante UNIQUE (torneio_id, participante_id),
    CONSTRAINT fk_classificacao_torneio FOREIGN KEY (torneio_id)
        REFERENCES torneios (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT fk_classificacao_participante FOREIGN KEY (torneio_id, participante_id)
        REFERENCES participantes (torneio_id, id) ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT ck_classificacao_nao_negativos CHECK (
        jogos >= 0 AND vitorias >= 0 AND empates >= 0 AND derrotas >= 0
        AND gols_pro >= 0 AND gols_contra >= 0 AND pontos >= 0
    ),
    CONSTRAINT ck_classificacao_jogos CHECK (jogos = vitorias + empates + derrotas),
    CONSTRAINT ck_classificacao_saldo CHECK (saldo = gols_pro - gols_contra),
    CONSTRAINT ck_classificacao_pontos CHECK (pontos = 3 * vitorias + empates),
    INDEX idx_classificacao_ordenacao (torneio_id, pontos DESC, vitorias DESC, saldo DESC, gols_pro DESC)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_as_ci;
