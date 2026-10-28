
package br.edu.sistemaacademico.repository.jdbc;

import br.edu.sistemaacademico.domain.Disciplina;
import br.edu.sistemaacademico.repository.DisciplinaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@Repository
public class DisciplinaRepositoryJdbc implements DisciplinaRepository {

    private final Connection connection;

    public DisciplinaRepositoryJdbc(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Disciplina salvar(Disciplina disciplina) {
        String sql = """
                INSERT INTO disciplina (codigo, nome, carga_horaria)
                VALUES (?, ?, ?)
                """;

        try (PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, disciplina.getCodigo());
            statement.setString(2, disciplina.getNome());
            statement.setInt(3, disciplina.getCargaHoraria());

            statement.executeUpdate();
            return disciplina;

        } catch (SQLException e) {
            if (e.getErrorCode() == 19) {
                throw new IllegalArgumentException(
                        "Já existe uma disciplina com esse código.", e
                );
            }

            throw new IllegalStateException(
                    "Erro ao salvar a disciplina no banco de dados.", e
            );
        }
    }

    @Override
    public Optional<Disciplina> buscarPorCodigo(String codigo) {
        String sql = """
                SELECT codigo, nome, carga_horaria
                FROM disciplina
                WHERE codigo = ?
                """;

        try (PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, codigo);

            try (ResultSet resultado = statement.executeQuery()) {
                if (resultado.next()) {
                    Disciplina disciplina = new Disciplina(
                            resultado.getString("codigo"),
                            resultado.getString("nome"),
                            resultado.getInt("carga_horaria")
                    );

                    return Optional.of(disciplina);
                }

                return Optional.empty();
            }

        } catch (SQLException e) {
            throw new IllegalStateException(
                    "Erro ao consultar a disciplina no banco de dados.", e
            );
        }
    }
}
