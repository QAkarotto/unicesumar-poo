package br.edu.sistemaacademico.repository.jdbc;

import java.sql.SQLException;

/**
 * Ajuda a interpretar as SQLException devolvidas pelo SQLite.
 */
final class ErrosSqlite {

    // Código numérico que o SQLite usa para "restrição violada" (SQLITE_CONSTRAINT)
    private static final int SQLITE_CONSTRAINT = 19;

    private ErrosSqlite() {
    }

    /**
     * true quando o INSERT falhou porque a chave primária já existe
     * (ex.: cadastrar duas vezes o mesmo RA).
     */
    static boolean violouChavePrimaria(SQLException e) {
        String mensagem = e.getMessage() == null ? "" : e.getMessage();
        return e.getErrorCode() == SQLITE_CONSTRAINT
                && (mensagem.contains("PRIMARY KEY") || mensagem.contains("UNIQUE"));
    }
}
