package br.edu.sistemaacademico.persistencia;

import java.sql.SQLException;
import org.sqlite.SQLiteErrorCode;
import org.sqlite.SQLiteException;

final class ErrosSqlite {
    private ErrosSqlite() {
    }

    // so a violacao de chave primaria conta como cadastro duplicado. NOT NULL e CHECK sao outro problema
    static boolean chaveDuplicada(SQLException erro) {
        return erro instanceof SQLiteException sqlite
                && sqlite.getResultCode() == SQLiteErrorCode.SQLITE_CONSTRAINT_PRIMARYKEY;
    }
}
