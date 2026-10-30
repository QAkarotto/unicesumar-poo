package br.edu.sistemaacademico.repository;

import java.sql.SQLException;

/** Ajuda a interpretar SQLException do SQLite. */
final class ErrosSqlite {

    private static final int SQLITE_CONSTRAINT = 19;
    private static final int SQLITE_CONSTRAINT_PRIMARYKEY = 1555;
    private static final int SQLITE_CONSTRAINT_UNIQUE = 2067;

    private ErrosSqlite() {
    }

    /** true se a falha foi violação de chave primária/unicidade (registro duplicado). */
    static boolean violacaoDeUnicidade(SQLException e) {
        int codigo = e.getErrorCode();
        if (codigo == SQLITE_CONSTRAINT_PRIMARYKEY || codigo == SQLITE_CONSTRAINT_UNIQUE) {
            return true;
        }
        var mensagem = e.getMessage() == null ? "" : e.getMessage();
        return codigo == SQLITE_CONSTRAINT
                && (mensagem.contains("UNIQUE constraint failed") || mensagem.contains("PRIMARY KEY"));
    }
}
