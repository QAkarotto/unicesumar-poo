-- Biblioteca Universitária - schema MySQL
-- Execute após criar o banco: CREATE DATABASE biblioteca;

CREATE TABLE leitor (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome        VARCHAR(120) NOT NULL,
    email       VARCHAR(160) NOT NULL,
    CONSTRAINT uk_leitor_email UNIQUE (email)
);

CREATE TABLE livro (
    id                      BIGINT AUTO_INCREMENT PRIMARY KEY,
    isbn                    VARCHAR(20)  NOT NULL,
    titulo                  VARCHAR(200) NOT NULL,
    autor                   VARCHAR(160) NOT NULL,
    quantidade_total        INT NOT NULL,
    quantidade_disponivel   INT NOT NULL,
    CONSTRAINT uk_livro_isbn UNIQUE (isbn),
    CONSTRAINT ck_livro_quantidade_total CHECK (quantidade_total >= 0),
    CONSTRAINT ck_livro_quantidade_disponivel CHECK (quantidade_disponivel >= 0),
    CONSTRAINT ck_livro_disponivel_lte_total CHECK (quantidade_disponivel <= quantidade_total)
);

CREATE TABLE emprestimo (
    id                      BIGINT AUTO_INCREMENT PRIMARY KEY,
    leitor_id               BIGINT NOT NULL,
    livro_id                BIGINT NOT NULL,
    data_emprestimo         DATE NOT NULL,
    data_prevista_devolucao DATE NOT NULL,
    data_devolucao          DATE NULL,
    situacao                VARCHAR(20) NOT NULL,
    CONSTRAINT fk_emprestimo_leitor FOREIGN KEY (leitor_id) REFERENCES leitor (id),
    CONSTRAINT fk_emprestimo_livro  FOREIGN KEY (livro_id)  REFERENCES livro (id),
    CONSTRAINT ck_emprestimo_situacao CHECK (situacao IN ('ATIVO', 'DEVOLVIDO'))
);

CREATE INDEX idx_emprestimo_leitor ON emprestimo (leitor_id);
CREATE INDEX idx_emprestimo_livro ON emprestimo (livro_id);
CREATE INDEX idx_emprestimo_situacao ON emprestimo (situacao);
