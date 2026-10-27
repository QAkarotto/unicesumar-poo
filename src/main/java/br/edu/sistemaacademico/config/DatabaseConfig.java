package br.edu.sistemaacademico.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

@Configuration
public class DatabaseConfig {
    @Bean
    public Database database(@Value("${app.database.url:jdbc:sqlite:database/sistema-academico.db}") String url) {
        try {
            if (url.startsWith("jdbc:sqlite:") && !url.equals("jdbc:sqlite::memory:")) {
                Path file = Path.of(url.substring("jdbc:sqlite:".length()));
                if (file.getParent() != null) Files.createDirectories(file.getParent());
            }
            Database db = new Database(url);
            db.initialize();
            return db;
        } catch (Exception e) {
            throw new IllegalStateException("Não foi possível inicializar o banco SQLite.", e);
        }
    }

    public static class Database {
        private final String url;
        public Database(String url) { this.url = url; }
        public Connection getConnection() throws SQLException { return DriverManager.getConnection(url); }
        private void initialize() throws SQLException {
            try (Connection c = getConnection(); Statement st = c.createStatement()) {
                st.executeUpdate("CREATE TABLE IF NOT EXISTS alunos (ra TEXT PRIMARY KEY COLLATE NOCASE, nome TEXT NOT NULL, email TEXT NOT NULL)");
                st.executeUpdate("CREATE TABLE IF NOT EXISTS disciplinas (codigo TEXT PRIMARY KEY COLLATE NOCASE, nome TEXT NOT NULL, carga_horaria INTEGER NOT NULL CHECK(carga_horaria > 0))");
                st.executeUpdate("INSERT OR IGNORE INTO alunos (ra, nome, email) VALUES ('RA2026001', 'Paola Oliveira', 'paola.oliveira@email.com')");
                st.executeUpdate("INSERT OR IGNORE INTO alunos (ra, nome, email) VALUES ('RA2026002', 'Bruno Santos', 'bruno.santos@email.com')");
                st.executeUpdate("INSERT OR IGNORE INTO disciplinas (codigo, nome, carga_horaria) VALUES ('POO', 'Programação Orientada a Objetos', 80)");
                st.executeUpdate("INSERT OR IGNORE INTO disciplinas (codigo, nome, carga_horaria) VALUES ('BD', 'Banco de Dados', 80)");
            }
        }
    }
}
