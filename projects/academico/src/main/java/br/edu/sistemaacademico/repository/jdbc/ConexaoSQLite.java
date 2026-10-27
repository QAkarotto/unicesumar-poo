
package br.edu.sistemaacademico.repository.jdbc;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Component
public class ConexaoSQLite {

    private final String url;

    public ConexaoSQLite(
            @Value("${app.database.path:database/academico.db}")
            String caminho) {

        try {
            Path arquivo = Path.of(caminho).toAbsolutePath();

            if (arquivo.getParent() != null) {
                Files.createDirectories(arquivo.getParent());
            }

            this.url = "jdbc:sqlite:" + arquivo;

        } catch (IOException e) {
            throw new IllegalStateException(
                    "Não foi possível preparar a pasta do banco.", e);
        }
    }

    public Connection abrir() throws SQLException {
        return DriverManager.getConnection(url);
    }
}
