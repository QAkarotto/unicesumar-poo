package br.edu.sistemaacademico.repository.jdbc;

import java.sql.SQLException;
import org.sqlite.SQLiteErrorCode;
import org.sqlite.SQLiteException;

/**
 * Interpretação de erros específicos do SQLite.
 * <p>
 * Fica aqui, e não no Controller, porque o código de erro depende do banco:
 * outro banco informaria a chave duplicada de outra forma.
 */
final class ErrosSqlite {

    private ErrosSqlite() {
    }

    static boolean violouChavePrimaria(SQLException erro) {
        return erro instanceof SQLiteException sqlite
                && sqlite.getResultCode() == SQLiteErrorCode.SQLITE_CONSTRAINT_PRIMARYKEY;
    }
}
