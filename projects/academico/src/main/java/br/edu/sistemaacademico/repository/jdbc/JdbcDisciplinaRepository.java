package br.edu.sistemaacademico.repository.jdbc;

import br.edu.sistemaacademico.domain.Disciplina;
import br.edu.sistemaacademico.repository.DisciplinaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcDisciplinaRepository implements DisciplinaRepository {

    @Override
    public Disciplina salvar(Disciplina disciplina) {
        String sql = """
                INSERT INTO disciplinas (codigo, nome, carga_horaria)
                VALUES (?, ?, ?)
                """;

        try (Connection conexao = ConexaoSQLite.conectar();
             PreparedStatement statement = conexao.prepareStatement(sql)) {

            statement.setString(1, disciplina.getCodigo());
            statement.setString(2, disciplina.getNome());
            statement.setInt(3, disciplina.getCargaHoraria());
            statement.executeUpdate();

            return disciplina;
        } catch (SQLException e) {
            throw new IllegalStateException(
                    "Erro ao salvar disciplina no banco de dados.", e
            );
        }
    }

    @Override
    public Optional<Disciplina> buscarPorCodigo(String codigo) {
        String sql = """
                SELECT codigo, nome, carga_horaria
                FROM disciplinas
                WHERE codigo = ?
                """;

        try (Connection conexao = ConexaoSQLite.conectar();
             PreparedStatement statement = conexao.prepareStatement(sql)) {

            statement.setString(1, codigo);

            try (ResultSet resultado = statement.executeQuery()) {
                if (resultado.next()) {
                    return Optional.of(new Disciplina(
                            resultado.getString("codigo"),
                            resultado.getString("nome"),
                            resultado.getInt("carga_horaria")
                    ));
                }
            }

            return Optional.empty();
        } catch (SQLException e) {
            throw new IllegalStateException(
                    "Erro ao buscar disciplina no banco de dados.", e
            );
        }
    }

    @Override
    public List<Disciplina> listarTodas() {
        String sql = """
                SELECT codigo, nome, carga_horaria
                FROM disciplinas
                ORDER BY codigo
                """;

        List<Disciplina> disciplinas = new ArrayList<>();

        try (Connection conexao = ConexaoSQLite.conectar();
             PreparedStatement statement = conexao.prepareStatement(sql);
             ResultSet resultado = statement.executeQuery()) {

            while (resultado.next()) {
                disciplinas.add(new Disciplina(
                        resultado.getString("codigo"),
                        resultado.getString("nome"),
                        resultado.getInt("carga_horaria")
                ));
            }

            return disciplinas;
        } catch (SQLException e) {
            throw new IllegalStateException(
                    "Erro ao listar disciplinas no banco de dados.", e
            );
        }
    }
}
