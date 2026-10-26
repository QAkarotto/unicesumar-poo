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

@Repository
public class DisciplinaRepositoryJdbc implements DisciplinaRepository {

    private static final int SQLITE_CONSTRAINT = 19;

    private final BancoDeDados bancoDeDados;

    public DisciplinaRepositoryJdbc(BancoDeDados bancoDeDados) {
        this.bancoDeDados = bancoDeDados;
    }

    @Override
    public Disciplina salvar(Disciplina disciplina) {
        String sql = "INSERT INTO disciplina (codigo, nome, carga_horaria) VALUES (?, ?, ?)";

        try (Connection conexao = bancoDeDados.abrirConexao();
             PreparedStatement comando = conexao.prepareStatement(sql)) {
            comando.setString(1, disciplina.getCodigo());
            comando.setString(2, disciplina.getNome());
            comando.setInt(3, disciplina.getCargaHoraria());
            comando.executeUpdate();
            return disciplina;
        } catch (SQLException e) {
            if ((e.getErrorCode() & 0xFF) == SQLITE_CONSTRAINT) {
                throw new RegistroDuplicadoException("Já existe disciplina com o código: " + disciplina.getCodigo(), e);
            }
            throw new RepositorioException("Erro ao salvar disciplina", e);
        }
    }

    @Override
    public Optional<Disciplina> buscarPorCodigo(String codigo) {
        String sql = "SELECT codigo, nome, carga_horaria FROM disciplina WHERE codigo = ?";

        try (Connection conexao = bancoDeDados.abrirConexao();
             PreparedStatement comando = conexao.prepareStatement(sql)) {
            comando.setString(1, codigo);

            try (ResultSet resultado = comando.executeQuery()) {
                if (resultado.next()) {
                    return Optional.of(paraDisciplina(resultado));
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RepositorioException("Erro ao buscar disciplina pelo código: " + codigo, e);
        }
    }

    private Disciplina paraDisciplina(ResultSet resultado) throws SQLException {
        return new Disciplina(
                resultado.getString("codigo"),
                resultado.getString("nome"),
                resultado.getInt("carga_horaria")
        );
    }
}
