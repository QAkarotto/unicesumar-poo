package br.edu.sistemaacademico.repository;

import br.edu.sistemaacademico.domain.Aluno;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@Repository
public class JdbcAlunoRepository implements AlunoRepository {
    private final DatabaseConnection database;

    public JdbcAlunoRepository(DatabaseConnection database) {
        this.database = database;
    }

    @Override
    public Aluno salvar(Aluno aluno) {
        if (buscarPorRa(aluno.getRa()).isPresent()) {
            throw new IllegalStateException("Aluno já cadastrado: " + aluno.getRa());
        }

        String sql = "INSERT INTO alunos (ra, nome, email) VALUES (?, ?, ?)";
        try (Connection connection = database.abrir();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, aluno.getRa());
            statement.setString(2, aluno.getNome());
            statement.setString(3, aluno.getEmail());
            statement.executeUpdate();
            return aluno;
        } catch (SQLException exception) {
            throw new DatabaseException("Erro ao salvar aluno.", exception);
        }
    }

    @Override
    public Optional<Aluno> buscarPorRa(String ra) {
        String sql = "SELECT ra, nome, email FROM alunos WHERE ra = ?";
        try (Connection connection = database.abrir();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, ra);
            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    return Optional.of(new Aluno(
                            result.getString("ra"),
                            result.getString("nome"),
                            result.getString("email")));
                }
                return Optional.empty();
            }
        } catch (SQLException exception) {
            throw new DatabaseException("Erro ao consultar aluno.", exception);
        }
    }
}
