package br.edu.sistemaacademico.repository.jdbc;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

@Component
public class ConexaoSQLite {
    private final String url;
    public ConexaoSQLite(@Value("${app.database.url}") String url) {
        this.url = url;
        inicializarBanco();
    }
    public Connection abrir() throws SQLException { return DriverManager.getConnection(url); }
    private void inicializarBanco() {
        try {
            if (url.startsWith("jdbc:sqlite:") && !url.equals("jdbc:sqlite::memory:")) {
                Path arquivo = Path.of(url.substring("jdbc:sqlite:".length()));
                if (arquivo.getParent() != null) Files.createDirectories(arquivo.getParent());
            }
            try (Connection c = abrir(); Statement s = c.createStatement()) {
                s.executeUpdate("CREATE TABLE IF NOT EXISTS aluno (registro_academico TEXT PRIMARY KEY NOT NULL, nome TEXT NOT NULL, email TEXT NOT NULL)");
                s.executeUpdate("CREATE TABLE IF NOT EXISTS disciplina (codigo TEXT PRIMARY KEY NOT NULL, nome TEXT NOT NULL, carga_horaria INTEGER NOT NULL CHECK (carga_horaria > 0))");
                s.executeUpdate("INSERT OR IGNORE INTO aluno (registro_academico, nome, email) VALUES ('RA2026001', 'Aluno Exemplo Um', 'aluno1@example.com')");
                s.executeUpdate("INSERT OR IGNORE INTO aluno (registro_academico, nome, email) VALUES ('RA2026002', 'Aluno Exemplo Dois', 'aluno2@example.com')");
                s.executeUpdate("INSERT OR IGNORE INTO disciplina (codigo, nome, carga_horaria) VALUES ('POO', 'Programação Orientada a Objetos', 80)");
                s.executeUpdate("INSERT OR IGNORE INTO disciplina (codigo, nome, carga_horaria) VALUES ('BD', 'Banco de Dados', 80)");
            }
        } catch (SQLException | IOException e) {
            throw new IllegalStateException("Não foi possível inicializar o banco SQLite.", e);
        }
    }
}
