package br.edu.sistemaacademico.repository.jdbc;

import java.sql.SQLException;

final class ErrosSqlite {

    private static final int SQLITE_CONSTRAINT = 19;

    private ErrosSqlite() {
    }

    static boolean violouRestricao(SQLException e) {
        return (e.getErrorCode() & 0xFF) == SQLITE_CONSTRAINT;
    }
}
