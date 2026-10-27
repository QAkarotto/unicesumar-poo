package br.edu.sistemaacademico.repository;

import br.edu.sistemaacademico.domain.Disciplina;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@Repository
public class JdbcDisciplinaRepository implements DisciplinaRepository {
    private final DatabaseConnection database;

    public JdbcDisciplinaRepository(DatabaseConnection database) {
        this.database = database;
    }

    @Override
    public Disciplina salvar(Disciplina disciplina) {
        if (buscarPorCodigo(disciplina.getCodigo()).isPresent()) {
            throw new IllegalStateException("Disciplina já cadastrada: " + disciplina.getCodigo());
        }

        String sql = "INSERT INTO disciplinas (codigo, nome, carga_horaria) VALUES (?, ?, ?)";
        try (Connection connection = database.abrir();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, disciplina.getCodigo());
            statement.setString(2, disciplina.getNome());
            statement.setInt(3, disciplina.getCargaHoraria());
            statement.executeUpdate();
            return disciplina;
        } catch (SQLException exception) {
            throw new DatabaseException("Erro ao salvar disciplina.", exception);
        }
    }

    @Override
    public Optional<Disciplina> buscarPorCodigo(String codigo) {
        String sql = "SELECT codigo, nome, carga_horaria FROM disciplinas WHERE codigo = ?";
        try (Connection connection = database.abrir();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, codigo);
            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    return Optional.of(new Disciplina(
                            result.getString("codigo"),
                            result.getString("nome"),
                            result.getInt("carga_horaria")));
                }
                return Optional.empty();
            }
        } catch (SQLException exception) {
            throw new DatabaseException("Erro ao consultar disciplina.", exception);
        }
    }
}
