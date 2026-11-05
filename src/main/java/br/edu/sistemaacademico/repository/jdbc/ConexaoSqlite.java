package br.edu.sistemaacademico.repository.jdbc;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/** Abre conexões JDBC com o SQLite. Cada operação abre e fecha a sua. */
@Component
public class ConexaoSqlite {

    private final String url;

    public ConexaoSqlite(@Value("${app.database.url}") String url) {
        this.url = url;
    }

    public Connection abrir() throws SQLException {
        return DriverManager.getConnection(url);
    }
}
