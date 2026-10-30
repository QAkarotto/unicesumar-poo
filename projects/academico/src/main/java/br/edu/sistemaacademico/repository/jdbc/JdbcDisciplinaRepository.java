package br.edu.sistemaacademico.repository.jdbc;

import br.edu.sistemaacademico.domain.Disciplina;
import br.edu.sistemaacademico.repository.DisciplinaRepository;
import br.edu.sistemaacademico.repository.RegistroDuplicadoException;
import br.edu.sistemaacademico.repository.RepositorioException;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

/** Implementação de {@link DisciplinaRepository} com JDBC puro sobre SQLite. */
@Repository
public class JdbcDisciplinaRepository implements DisciplinaRepository {

    private static final String INSERIR =
            "INSERT INTO disciplina (codigo, nome, carga_horaria) VALUES (?, ?, ?)";
    private static final String BUSCAR_POR_CODIGO =
            "SELECT codigo, nome, carga_horaria FROM disciplina WHERE codigo = ?";

    private final BancoDeDados banco;

    public JdbcDisciplinaRepository(BancoDeDados banco) {
        this.banco = banco;
    }

    @Override
    public Disciplina salvar(Disciplina disciplina) {
        try (Connection conexao = banco.abrirConexao();
             PreparedStatement comando = conexao.prepareStatement(INSERIR)) {
            comando.setString(1, disciplina.getCodigo());
            comando.setString(2, disciplina.getNome());
            comando.setInt(3, disciplina.getCargaHoraria());
            comando.executeUpdate();
            return disciplina;
        } catch (SQLException e) {
            if (ErrosSql.violacaoDeUnicidade(e)) {
                throw new RegistroDuplicadoException(
                        "Já existe uma disciplina com o código " + disciplina.getCodigo(), e);
            }
            throw new RepositorioException(
                    "Erro ao salvar a disciplina " + disciplina.getCodigo(), e);
        }
    }

    @Override
    public Optional<Disciplina> buscarPorCodigo(String codigo) {
        try (Connection conexao = banco.abrirConexao();
             PreparedStatement comando = conexao.prepareStatement(BUSCAR_POR_CODIGO)) {
            comando.setString(1, codigo);
            try (ResultSet resultado = comando.executeQuery()) {
                if (!resultado.next()) {
                    return Optional.empty();
                }
                return Optional.of(new Disciplina(
                        resultado.getString("codigo"),
                        resultado.getString("nome"),
                        resultado.getInt("carga_horaria")));
            }
        } catch (SQLException e) {
            throw new RepositorioException("Erro ao buscar a disciplina " + codigo, e);
        }
    }
}
