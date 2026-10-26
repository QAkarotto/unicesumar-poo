package br.edu.sistemaacademico.repository.jdbc;

import br.edu.sistemaacademico.domain.Aluno;
import br.edu.sistemaacademico.repository.AlunoRepository;
import br.edu.sistemaacademico.repository.RegistroDuplicadoException;
import br.edu.sistemaacademico.repository.RepositorioException;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@Repository
public class AlunoRepositoryJdbc implements AlunoRepository {

    private static final int SQLITE_CONSTRAINT = 19;

    private final BancoDeDados bancoDeDados;

    public AlunoRepositoryJdbc(BancoDeDados bancoDeDados) {
        this.bancoDeDados = bancoDeDados;
    }

    @Override
    public Aluno salvar(Aluno aluno) {
        String sql = "INSERT INTO aluno (registro_academico, nome, email) VALUES (?, ?, ?)";

        try (Connection conexao = bancoDeDados.abrirConexao();
             PreparedStatement comando = conexao.prepareStatement(sql)) {
            comando.setString(1, aluno.getRa());
            comando.setString(2, aluno.getNome());
            comando.setString(3, aluno.getEmail());
            comando.executeUpdate();
            return aluno;
        } catch (SQLException e) {
            if ((e.getErrorCode() & 0xFF) == SQLITE_CONSTRAINT) {
                throw new RegistroDuplicadoException("Já existe aluno com o RA: " + aluno.getRa(), e);
            }
            throw new RepositorioException("Erro ao salvar aluno", e);
        }
    }

    @Override
    public Optional<Aluno> buscarPorRa(String ra) {
        String sql = "SELECT registro_academico, nome, email FROM aluno WHERE registro_academico = ?";

        try (Connection conexao = bancoDeDados.abrirConexao();
             PreparedStatement comando = conexao.prepareStatement(sql)) {
            comando.setString(1, ra);

            try (ResultSet resultado = comando.executeQuery()) {
                if (resultado.next()) {
                    return Optional.of(paraAluno(resultado));
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RepositorioException("Erro ao buscar aluno pelo RA: " + ra, e);
        }
    }

    private Aluno paraAluno(ResultSet resultado) throws SQLException {
        return new Aluno(
                resultado.getString("registro_academico"),
                resultado.getString("nome"),
                resultado.getString("email")
        );
    }
}
