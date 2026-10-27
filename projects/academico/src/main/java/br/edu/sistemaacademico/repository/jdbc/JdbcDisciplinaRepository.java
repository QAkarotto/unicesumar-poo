
package br.edu.sistemaacademico.repository.jdbc;

import br.edu.sistemaacademico.domain.Disciplina;
import br.edu.sistemaacademico.repository.DisciplinaRepository;
import br.edu.sistemaacademico.repository.PersistenciaException;
import br.edu.sistemaacademico.repository.RegistroDuplicadoException;

import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@Repository
public class JdbcDisciplinaRepository
        implements DisciplinaRepository {

    private final ConexaoSQLite conexao;

    public JdbcDisciplinaRepository(ConexaoSQLite conexao) {
        this.conexao = conexao;
    }

    @Override
    public Disciplina salvar(Disciplina disciplina) {

        String sql = """
            INSERT INTO disciplinas
            (codigo, nome, carga_horaria)
            VALUES (?, ?, ?)
            """;

        try (Connection connection = conexao.abrir();
             PreparedStatement ps =
                     connection.prepareStatement(sql)) {

            ps.setString(1, disciplina.getCodigo());
            ps.setString(2, disciplina.getNome());
            ps.setInt(3, disciplina.getCargaHoraria());

            ps.executeUpdate();

            return disciplina;

        } catch (SQLException e) {

            if (e.getErrorCode() == 19
                    && e.getMessage() != null
                    && e.getMessage().contains("UNIQUE constraint failed")) {
                throw new RegistroDuplicadoException(
                        "Disciplina com esse código já cadastrada.", e);
            }

            throw new PersistenciaException(
                    "Falha ao salvar disciplina.", e);
        }
    }

    @Override
    public Optional<Disciplina> buscarPorCodigo(String codigo) {

        String sql = """
            SELECT codigo, nome, carga_horaria
            FROM disciplinas
            WHERE codigo = ?
            """;

        try (Connection connection = conexao.abrir();
             PreparedStatement ps =
                     connection.prepareStatement(sql)) {

            ps.setString(1, codigo);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    Disciplina disciplina = new Disciplina(
                            rs.getString("codigo"),
                            rs.getString("nome"),
                            rs.getInt("carga_horaria")
                    );

                    return Optional.of(disciplina);
                }

                return Optional.empty();
            }

        } catch (SQLException e) {
            throw new PersistenciaException(
                    "Falha ao consultar disciplina.", e);
        }
    }
}
