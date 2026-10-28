
package br.edu.sistemaacademico.repository.jdbc;

import br.edu.sistemaacademico.domain.Aluno;
import br.edu.sistemaacademico.repository.AlunoRepository;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

@Repository
public class AlunoRepositoryJdbc implements AlunoRepository {

    private final Connection connection;

    public AlunoRepositoryJdbc(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Aluno salvar(Aluno aluno) {
        String sql = """
                INSERT INTO aluno (registro_academico, nome, email)
                VALUES (?, ?, ?)
                """;

        try (PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, aluno.getRegistroAcademico());
            statement.setString(2, aluno.getNome());
            statement.setString(3, aluno.getEmail());

            statement.executeUpdate();
            return aluno;

        } catch (SQLException e) {
            if (e.getErrorCode() == 19) {
                throw new IllegalArgumentException(
                        "Já existe um aluno com esse registro acadêmico.", e
                );
            }

            throw new IllegalStateException(
                    "Erro ao salvar o aluno no banco de dados.", e
            );
        }
    }

    @Override
    public Optional<Aluno> buscarPorRegistroAcademico(
            String registroAcademico) {

        String sql = """
                SELECT registro_academico, nome, email
                FROM aluno
                WHERE registro_academico = ?
                """;

        try (PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, registroAcademico);

            try (ResultSet resultado = statement.executeQuery()) {
                if (resultado.next()) {
                    Aluno aluno = new Aluno(
                            resultado.getString("registro_academico"),
                            resultado.getString("nome"),
                            resultado.getString("email")
                    );

                    return Optional.of(aluno);
                }

                return Optional.empty();
            }

        } catch (SQLException e) {
            throw new IllegalStateException(
                    "Erro ao consultar o aluno no banco de dados.", e
            );
        }
    }
}
