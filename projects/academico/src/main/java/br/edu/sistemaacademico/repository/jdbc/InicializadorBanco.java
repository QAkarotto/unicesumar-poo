
package br.edu.sistemaacademico.repository.jdbc;

import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;

@Component
public class InicializadorBanco {

    private final ConexaoSQLite conexao;

    public InicializadorBanco(ConexaoSQLite conexao) {
        this.conexao = conexao;
    }

    @PostConstruct
    public void iniciar() {

        String sqlAlunos = """
            CREATE TABLE IF NOT EXISTS alunos (
                registro_academico TEXT PRIMARY KEY,
                nome TEXT NOT NULL,
                email TEXT NOT NULL
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

        try (Connection connection = conexao.abrir();
             Statement statement = connection.createStatement()) {

            statement.executeUpdate(sqlAlunos);
            statement.executeUpdate(sqlDisciplinas);

        } catch (SQLException e) {
            throw new IllegalStateException(
                    "Erro ao preparar o banco SQLite.", e);
        }
    }
}
