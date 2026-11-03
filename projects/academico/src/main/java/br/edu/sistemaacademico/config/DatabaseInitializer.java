
package br.edu.sistemaacademico.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@Component
public class DatabaseInitializer implements CommandLineRunner {

    private final Connection connection;

    public DatabaseInitializer(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void run(String... args) {
        try (Statement statement = connection.createStatement()) {

            statement.execute("""
                    CREATE TABLE IF NOT EXISTS aluno (
                        registro_academico TEXT PRIMARY KEY NOT NULL,
                        nome TEXT NOT NULL,
                        email TEXT NOT NULL
                    )
                    """);

            statement.execute("""
                    CREATE TABLE IF NOT EXISTS disciplina (
                        codigo TEXT PRIMARY KEY NOT NULL,
                        nome TEXT NOT NULL,
                        carga_horaria INTEGER NOT NULL
                            CHECK (carga_horaria > 0)
                    )
                    """);

            inserirDadosIniciais();

        } catch (SQLException e) {
            throw new IllegalStateException(
                    "Erro ao inicializar o banco SQLite.", e
            );
        }
    }

    private void inserirDadosIniciais() throws SQLException {
        if (tabelaVazia("aluno")) {
            try (PreparedStatement statement =
                         connection.prepareStatement("""
                                 INSERT INTO aluno
                                 (registro_academico, nome, email)
                                 VALUES (?, ?, ?)
                                 """)) {

                statement.setString(1, "RA2026001");
                statement.setString(2, "Aluno Exemplo Um");
                statement.setString(3, "aluno1@example.com");
                statement.executeUpdate();

                statement.setString(1, "RA2026002");
                statement.setString(2, "Aluno Exemplo Dois");
                statement.setString(3, "aluno2@example.com");
                statement.executeUpdate();
            }
        }

        if (tabelaVazia("disciplina")) {
            try (PreparedStatement statement =
                         connection.prepareStatement("""
                                 INSERT INTO disciplina
                                 (codigo, nome, carga_horaria)
                                 VALUES (?, ?, ?)
                                 """)) {

                statement.setString(1, "POO");
                statement.setString(
                        2, "Programação Orientada a Objetos");
                statement.setInt(3, 80);
                statement.executeUpdate();

                statement.setString(1, "BD");
                statement.setString(2, "Banco de Dados");
                statement.setInt(3, 80);
                statement.executeUpdate();
            }
        }
    }

    private boolean tabelaVazia(String tabela) throws SQLException {
        String sql = "SELECT COUNT(*) FROM " + tabela;

        try (Statement statement = connection.createStatement();
             ResultSet resultado = statement.executeQuery(sql)) {

            return resultado.next() && resultado.getInt(1) == 0;
        }
    }
}
