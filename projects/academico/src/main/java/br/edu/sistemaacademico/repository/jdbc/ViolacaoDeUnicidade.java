package br.edu.sistemaacademico.repository.jdbc;

import java.sql.SQLException;

/** Identifica erro de chave primária/UNIQUE duplicada do SQLite (código 19 = SQLITE_CONSTRAINT). */
final class ViolacaoDeUnicidade {

    private ViolacaoDeUnicidade() {
    }

    static boolean eh(SQLException e) {
        String msg = e.getMessage() == null ? "" : e.getMessage();
        return e.getErrorCode() == 19 || msg.contains("SQLITE_CONSTRAINT");
    }
}
