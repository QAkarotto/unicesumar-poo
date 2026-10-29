
package br.edu.sistemaacademico.repository.jdbc;

import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ConexaoSQLite {

    private static final Path CAMINHO_BANCO =
            Path.of("database", "academico.db");

    public static Connection conectar() throws SQLException {

        try {
            Files.createDirectories(CAMINHO_BANCO.toAbsolutePath().getParent());
        } catch (IOException e) {
            throw new SQLException(
                    "Não foi possível criar a pasta do banco de dados.", e);
        }

        String url = "jdbc:sqlite:" +
                CAMINHO_BANCO.toAbsolutePath();

        return DriverManager.getConnection(url);
    }

    public static void inicializarBanco() {

        String sqlAluno = """
                CREATE TABLE IF NOT EXISTS aluno (
                    registro_academico TEXT PRIMARY KEY NOT NULL,
                    nome TEXT NOT NULL,
                    email TEXT NOT NULL
                )
                """;

        String sqlDisciplina = """
                CREATE TABLE IF NOT EXISTS disciplina (
                    codigo TEXT PRIMARY KEY NOT NULL,
                    nome TEXT NOT NULL,
                    carga_horaria INTEGER NOT NULL
                    CHECK (carga_horaria > 0)
                )
                """;

        try (Connection conexao = conectar();
             Statement comando = conexao.createStatement()) {

            comando.execute(sqlAluno);
            comando.execute(sqlDisciplina);

            System.out.println("Banco SQLite inicializado com sucesso!");
            System.out.println("Local do banco: " +
                    CAMINHO_BANCO.toAbsolutePath());

        } catch (SQLException e) {
            throw new IllegalStateException(
                    "Erro ao inicializar o banco SQLite.", e);
        }
    }
}
