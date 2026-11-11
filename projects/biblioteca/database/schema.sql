-- MySQL 8.4. Crie o banco biblioteca antes de executar este arquivo.
-- Também executado pelo Spring SQL Init, antes do Hibernate validar o mapeamento.
CREATE TABLE IF NOT EXISTS livro (
    id BIGINT NOT NULL AUTO_INCREMENT,
    titulo VARCHAR(180) NOT NULL,
    autor VARCHAR(120) NOT NULL,
    isbn VARCHAR(13) NOT NULL,
    categoria VARCHAR(60) NOT NULL,
    exemplares INT NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT uk_livro_isbn UNIQUE (isbn),
    CONSTRAINT ck_livro_exemplares CHECK (exemplares >= 1),
    CONSTRAINT ck_livro_titulo CHECK (CHAR_LENGTH(TRIM(titulo)) > 0)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS leitor (
    id BIGINT NOT NULL AUTO_INCREMENT,
    nome VARCHAR(120) NOT NULL,
    email VARCHAR(180) NOT NULL,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    PRIMARY KEY (id),
    CONSTRAINT uk_leitor_email UNIQUE (email),
    CONSTRAINT ck_leitor_nome CHECK (CHAR_LENGTH(TRIM(nome)) > 0)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS emprestimo (
    id BIGINT NOT NULL AUTO_INCREMENT,
    livro_id BIGINT NOT NULL,
    leitor_id BIGINT NOT NULL,
    emprestado_em DATE NOT NULL,
    previsto_para DATE NOT NULL,
    devolvido_em DATE NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_emprestimo_livro FOREIGN KEY (livro_id) REFERENCES livro(id),
    CONSTRAINT fk_emprestimo_leitor FOREIGN KEY (leitor_id) REFERENCES leitor(id),
    CONSTRAINT ck_emprestimo_prazo CHECK (previsto_para >= emprestado_em),
    CONSTRAINT ck_emprestimo_devolucao CHECK (devolvido_em IS NULL OR devolvido_em >= emprestado_em),
    INDEX ix_emprestimo_livro_ativo (livro_id, devolvido_em),
    INDEX ix_emprestimo_leitor_ativo (leitor_id, devolvido_em, previsto_para)
) ENGINE=InnoDB;
