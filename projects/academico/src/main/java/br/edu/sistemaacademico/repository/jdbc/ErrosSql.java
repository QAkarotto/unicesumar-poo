package br.edu.sistemaacademico.repository.jdbc;

import java.sql.SQLException;

final class ErrosSql {

    private ErrosSql() {
    }

    /** Indica se o erro foi causado por violação de chave primária ou UNIQUE. */
    static boolean violacaoDeUnicidade(SQLException e) {
        int codigo = e.getErrorCode();
        String mensagem = e.getMessage() == null ? "" : e.getMessage();
        return codigo == 1555 // SQLITE_CONSTRAINT_PRIMARYKEY
                || codigo == 2067 // SQLITE_CONSTRAINT_UNIQUE
                || mensagem.contains("UNIQUE constraint failed")
                || mensagem.contains("PRIMARY KEY");
    }
}
