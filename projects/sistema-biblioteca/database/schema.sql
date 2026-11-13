
CREATE DATABASE IF NOT EXISTS biblioteca
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

USE biblioteca;

CREATE TABLE IF NOT EXISTS livro (
    id BIGINT NOT NULL AUTO_INCREMENT,
    titulo VARCHAR(200) NOT NULL,
    autor VARCHAR(150) NOT NULL,
    isbn VARCHAR(20) NOT NULL,
    quantidade_total INT NOT NULL,
    quantidade_disponivel INT NOT NULL,

    PRIMARY KEY (id),
    CONSTRAINT uk_livro_isbn UNIQUE (isbn),
    CONSTRAINT chk_livro_quantidade_total
        CHECK (quantidade_total >= 0),
    CONSTRAINT chk_livro_quantidade_disponivel
        CHECK (
            quantidade_disponivel >= 0
            AND quantidade_disponivel <= quantidade_total
        )
);

CREATE TABLE IF NOT EXISTS leitor (
    id BIGINT NOT NULL AUTO_INCREMENT,
    nome VARCHAR(150) NOT NULL,
    email VARCHAR(150) NOT NULL,
    telefone VARCHAR(20),

    PRIMARY KEY (id),
    CONSTRAINT uk_leitor_email UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS emprestimo (
    id BIGINT NOT NULL AUTO_INCREMENT,
    livro_id BIGINT NOT NULL,
    leitor_id BIGINT NOT NULL,
    data_emprestimo DATE NOT NULL,
    data_prevista_devolucao DATE NOT NULL,
    data_devolucao DATE,
    situacao VARCHAR(20) NOT NULL,

    PRIMARY KEY (id),

    CONSTRAINT fk_emprestimo_livro
        FOREIGN KEY (livro_id)
        REFERENCES livro(id),

    CONSTRAINT fk_emprestimo_leitor
        FOREIGN KEY (leitor_id)
        REFERENCES leitor(id),

    CONSTRAINT chk_emprestimo_situacao
        CHECK (situacao IN ('ATIVO', 'DEVOLVIDO')),

    CONSTRAINT chk_emprestimo_datas
        CHECK (
            data_prevista_devolucao >= data_emprestimo
            AND (
                data_devolucao IS NULL
                OR data_devolucao >= data_emprestimo
            )
        ),

    CONSTRAINT chk_emprestimo_devolucao
        CHECK (
            (situacao = 'ATIVO' AND data_devolucao IS NULL)
            OR
            (situacao = 'DEVOLVIDO' AND data_devolucao IS NOT NULL)
        )
);
