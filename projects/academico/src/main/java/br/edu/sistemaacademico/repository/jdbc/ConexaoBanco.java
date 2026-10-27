package br.edu.sistemaacademico.repository.jdbc;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Component
public class ConexaoBanco {

    private final String url;

    public ConexaoBanco(@Value("${app.database.url}") String url) {
        this.url = url;
    }

    public Connection conectar() throws SQLException {
        return DriverManager.getConnection(url);
    }
}
