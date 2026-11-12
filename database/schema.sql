-- ANiVA - estrutura do banco de dados (MySQL 8+ / MariaDB 10.5+)
--
-- Modelo:
--   USUARIO 1---N ITEM_LISTA N---1 OBRA        (favoritos e pauta de cada pessoa)
--   USUARIO 1---N AVALIACAO  N---1 OBRA        (uma nota por pessoa em cada obra)
--   USUARIO 1---N COMENTARIO N---1 OBRA        (comentários de uma obra)
--   COMENTARIO N---N USUARIO                   (curtidas, tabela COMENTARIO_CURTIDA)
--   OBRA 1---N OBRA_TIPO / GENERO / TROPE / AVISO / FORMATO / PLATAFORMA / PERSONAGEM
--
-- O script é idempotente (IF NOT EXISTS): o Spring Boot executa este arquivo
-- a cada inicialização sem apagar dados.

CREATE TABLE IF NOT EXISTS usuario (
    id          BIGINT       NOT NULL AUTO_INCREMENT,
    nome        VARCHAR(80)  NOT NULL,
    email       VARCHAR(150) NOT NULL,
    senha_hash  VARCHAR(100) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT uk_usuario_email UNIQUE (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS obra (
    id                VARCHAR(80)  NOT NULL,
    titulo            VARCHAR(200) NOT NULL,
    tipo              VARCHAR(20)  NOT NULL,
    ano               INT          NOT NULL,
    status            VARCHAR(30)  NOT NULL,
    nota              DOUBLE       NOT NULL,
    popularidade      INT          NOT NULL,
    episodios         INT          NULL,
    estudio           VARCHAR(100) NULL,
    autor             VARCHAR(200) NULL,
    duracao           VARCHAR(100) NULL,
    plataformas_nota  VARCHAR(500) NULL,
    sinopse           TEXT         NULL,
    capa              VARCHAR(500) NULL,
    banner            VARCHAR(500) NULL,
    PRIMARY KEY (id),
    CONSTRAINT ck_obra_nota CHECK (nota BETWEEN 0 AND 10)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Listas que pertencem a uma obra. "posicao" mantém a ordem de exibição.
CREATE TABLE IF NOT EXISTS obra_tipo (
    obra_id  VARCHAR(80) NOT NULL,
    posicao  INT         NOT NULL,
    tipo     VARCHAR(20) NOT NULL,
    PRIMARY KEY (obra_id, posicao),
    CONSTRAINT fk_obra_tipo_obra FOREIGN KEY (obra_id) REFERENCES obra (id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS obra_genero (
    obra_id  VARCHAR(80) NOT NULL,
    posicao  INT         NOT NULL,
    genero   VARCHAR(50) NOT NULL,
    PRIMARY KEY (obra_id, posicao),
    CONSTRAINT fk_obra_genero_obra FOREIGN KEY (obra_id) REFERENCES obra (id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS obra_trope (
    obra_id  VARCHAR(80)  NOT NULL,
    posicao  INT          NOT NULL,
    trope    VARCHAR(100) NOT NULL,
    PRIMARY KEY (obra_id, posicao),
    CONSTRAINT fk_obra_trope_obra FOREIGN KEY (obra_id) REFERENCES obra (id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS obra_aviso (
    obra_id  VARCHAR(80)  NOT NULL,
    posicao  INT          NOT NULL,
    aviso    VARCHAR(100) NOT NULL,
    PRIMARY KEY (obra_id, posicao),
    CONSTRAINT fk_obra_aviso_obra FOREIGN KEY (obra_id) REFERENCES obra (id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS obra_formato (
    obra_id      VARCHAR(80)  NOT NULL,
    posicao      INT          NOT NULL,
    tipo         VARCHAR(20)  NOT NULL,
    extensao     VARCHAR(100) NULL,
    observacao   VARCHAR(200) NULL,
    PRIMARY KEY (obra_id, posicao),
    CONSTRAINT fk_obra_formato_obra FOREIGN KEY (obra_id) REFERENCES obra (id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS obra_plataforma (
    obra_id    VARCHAR(80)  NOT NULL,
    posicao    INT          NOT NULL,
    categoria  VARCHAR(20)  NOT NULL,
    nome       VARCHAR(100) NOT NULL,
    url        VARCHAR(500) NOT NULL,
    PRIMARY KEY (obra_id, posicao),
    CONSTRAINT fk_obra_plataforma_obra FOREIGN KEY (obra_id) REFERENCES obra (id) ON DELETE CASCADE,
    CONSTRAINT ck_obra_plataforma_categoria CHECK (categoria IN ('streaming', 'leitura'))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS obra_personagem (
    obra_id    VARCHAR(80)  NOT NULL,
    posicao    INT          NOT NULL,
    nome       VARCHAR(100) NOT NULL,
    papel      VARCHAR(100) NULL,
    descricao  VARCHAR(500) NULL,
    PRIMARY KEY (obra_id, posicao),
    CONSTRAINT fk_obra_personagem_obra FOREIGN KEY (obra_id) REFERENCES obra (id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Favoritos e pauta ("para ver/ler") de cada pessoa.
-- favorito = 1 -> está nos favoritos. status IS NOT NULL -> está na pauta.
CREATE TABLE IF NOT EXISTS item_lista (
    id          BIGINT      NOT NULL AUTO_INCREMENT,
    usuario_id  BIGINT      NOT NULL,
    obra_id     VARCHAR(80) NOT NULL,
    favorito    BOOLEAN     NOT NULL DEFAULT FALSE,
    status      VARCHAR(20) NULL,
    progresso   INT         NOT NULL DEFAULT 0,
    PRIMARY KEY (id),
    CONSTRAINT uk_item_lista_usuario_obra UNIQUE (usuario_id, obra_id),
    CONSTRAINT fk_item_lista_usuario FOREIGN KEY (usuario_id) REFERENCES usuario (id) ON DELETE CASCADE,
    CONSTRAINT fk_item_lista_obra FOREIGN KEY (obra_id) REFERENCES obra (id) ON DELETE CASCADE,
    CONSTRAINT ck_item_lista_status CHECK (status IS NULL OR status IN ('A_FAZER', 'ANDAMENTO', 'CONCLUIDO')),
    CONSTRAINT ck_item_lista_progresso CHECK (progresso BETWEEN 0 AND 100)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS avaliacao (
    id          BIGINT      NOT NULL AUTO_INCREMENT,
    usuario_id  BIGINT      NOT NULL,
    obra_id     VARCHAR(80) NOT NULL,
    nota        INT         NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT uk_avaliacao_usuario_obra UNIQUE (usuario_id, obra_id),
    CONSTRAINT fk_avaliacao_usuario FOREIGN KEY (usuario_id) REFERENCES usuario (id) ON DELETE CASCADE,
    CONSTRAINT fk_avaliacao_obra FOREIGN KEY (obra_id) REFERENCES obra (id) ON DELETE CASCADE,
    CONSTRAINT ck_avaliacao_nota CHECK (nota BETWEEN 1 AND 10)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS comentario (
    id          BIGINT        NOT NULL AUTO_INCREMENT,
    obra_id     VARCHAR(80)   NOT NULL,
    autor_id    BIGINT        NOT NULL,
    texto       VARCHAR(1000) NOT NULL,
    spoiler     BOOLEAN       NOT NULL DEFAULT FALSE,
    criado_em   DATETIME(6)   NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_comentario_obra FOREIGN KEY (obra_id) REFERENCES obra (id) ON DELETE CASCADE,
    CONSTRAINT fk_comentario_autor FOREIGN KEY (autor_id) REFERENCES usuario (id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Relação N:N entre comentário e usuário (quem curtiu).
CREATE TABLE IF NOT EXISTS comentario_curtida (
    comentario_id  BIGINT NOT NULL,
    usuario_id     BIGINT NOT NULL,
    PRIMARY KEY (comentario_id, usuario_id),
    CONSTRAINT fk_curtida_comentario FOREIGN KEY (comentario_id) REFERENCES comentario (id) ON DELETE CASCADE,
    CONSTRAINT fk_curtida_usuario FOREIGN KEY (usuario_id) REFERENCES usuario (id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
