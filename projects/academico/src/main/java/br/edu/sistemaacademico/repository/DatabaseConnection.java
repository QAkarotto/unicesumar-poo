package br.edu.sistemaacademico.repository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Component
public class DatabaseConnection {
    private final String databasePath;

    public DatabaseConnection(@Value("${app.database.path:data/academico.db}") String databasePath) {
        this.databasePath = databasePath;
    }

    public Connection abrir() throws SQLException {
        try {
            Path path = Path.of(databasePath).toAbsolutePath();
            if (path.getParent() != null) {
                Files.createDirectories(path.getParent());
            }
        } catch (IOException exception) {
            throw new DatabaseException("Não foi possível preparar o diretório do banco SQLite.", exception);
        }
        return DriverManager.getConnection("jdbc:sqlite:" + databasePath);
    }
}
