package br.edu.sistemaacademico.repository.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Abre conexões com o banco usando a URL configurada em
 * {@code app.database.url} (application.properties).
 * <p>
 * Cada operação abre a própria conexão e a fecha ao final (try-with-resources).
 */
@Component
public class FabricaConexoes {
    private final String url;

    public FabricaConexoes(@Value("${app.database.url}") String url) {
        this.url = url;
    }

    public Connection abrir() throws SQLException {
        return DriverManager.getConnection(url);
    }
}
