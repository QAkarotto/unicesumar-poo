package br.edu.sistemaacademico.repository.jdbc;

import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ConexaoSQLite {

    private static final String URL =
            "jdbc:sqlite:projects/academico/database/sistema-academico.db";

    public static Connection conectar() throws SQLException {
        try {
            Files.createDirectories(
                    Path.of("projects", "academico", "database")
            );
        } catch (java.io.IOException e) {
            throw new SQLException(
                    "Nao foi possivel criar a pasta do banco de dados.", e
            );
        }

        return DriverManager.getConnection(URL);
    }

    public static void criarTabelas() throws SQLException {
        String sqlAlunos = """
                CREATE TABLE IF NOT EXISTS alunos (
                    identificador_academico TEXT PRIMARY KEY,
                    nome TEXT NOT NULL,
                    email TEXT NOT NULL UNIQUE
                )
                """;

        String sqlDisciplinas = """
                CREATE TABLE IF NOT EXISTS disciplinas (
                    codigo TEXT PRIMARY KEY,
                    nome TEXT NOT NULL,
                    carga_horaria INTEGER NOT NULL
                        CHECK (carga_horaria > 0)
                )
                """;

        try (Connection conexao = conectar();
             Statement statement = conexao.createStatement()) {
            statement.execute(sqlAlunos);
            statement.execute(sqlDisciplinas);
        }
    }
}
