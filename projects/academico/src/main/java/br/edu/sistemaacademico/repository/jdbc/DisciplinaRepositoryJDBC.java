
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
public class DisciplinaRepositoryJDBC implements DisciplinaRepository {

    private final ConexaoBanco conexaoBanco;

    public DisciplinaRepositoryJDBC(ConexaoBanco conexaoBanco) {
        this.conexaoBanco = conexaoBanco;
    }

    @Override
    public void salvar(Disciplina disciplina) {
        String sql = """
                INSERT INTO disciplina (codigo, nome, carga_horaria)
                VALUES (?, ?, ?)
                """;

        try (Connection conexao = conexaoBanco.conectar();
             PreparedStatement comando = conexao.prepareStatement(sql)) {

            comando.setString(1, disciplina.getCodigo());
            comando.setString(2, disciplina.getNome());
            comando.setInt(3, disciplina.getCargaHoraria());

            comando.executeUpdate();

        } catch (SQLException e) {
            throw new IllegalStateException(
                    "Não foi possível salvar a disciplina.", e);
        }
    }

    @Override
    public Optional<Disciplina> buscarPorCodigo(String codigo) {
        String sql = """
                SELECT codigo, nome, carga_horaria
                FROM disciplina
                WHERE codigo = ?
                """;

        try (Connection conexao = conexaoBanco.conectar();
             PreparedStatement comando = conexao.prepareStatement(sql)) {

            comando.setString(1, codigo);

            try (ResultSet resultado = comando.executeQuery()) {
                if (resultado.next()) {
                    Disciplina disciplina = new Disciplina(
                            resultado.getString("codigo"),
                            resultado.getString("nome"),
                            resultado.getInt("carga_horaria")
                    );

                    return Optional.of(disciplina);
                }
            }

        } catch (SQLException e) {
            throw new IllegalStateException(
                    "Não foi possível buscar a disciplina.", e);
        }

        return Optional.empty();
    }
}
