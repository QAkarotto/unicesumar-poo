
package br.edu.sistemaacademico.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Configuration
public class DatabaseConfig {

    @Value("${app.database.path}")
    private String databasePath;

    @Bean
    public Connection connection() throws SQLException {
        try {
            Path caminho = Path.of(databasePath).toAbsolutePath();
            Files.createDirectories(caminho.getParent());

            return DriverManager.getConnection(
                    "jdbc:sqlite:" + caminho
            );
        } catch (Exception e) {
            throw new SQLException(
                    "Não foi possível conectar ao banco SQLite.", e
            );
        }
    }
}
