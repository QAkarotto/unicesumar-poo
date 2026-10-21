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

    private static final String INSERIR =
            "INSERT INTO aluno (ra, nome, email) VALUES (?, ?, ?)";
    private static final String BUSCAR_POR_RA =
            "SELECT ra, nome, email FROM aluno WHERE ra = ?";

    private final BancoDeDados banco;

    public AlunoRepositoryJdbc(BancoDeDados banco) {
        this.banco = banco;
    }

    @Override
    public Aluno salvar(Aluno aluno) {
        try (Connection conexao = banco.abrirConexao();
             PreparedStatement comando = conexao.prepareStatement(INSERIR)) {
            comando.setString(1, aluno.getRa());
            comando.setString(2, aluno.getNome());
            comando.setString(3, aluno.getEmail());
            comando.executeUpdate();
            return aluno;
        } catch (SQLException e) {
            if (e.getErrorCode() == SQLITE_CONSTRAINT) {
                throw new RegistroDuplicadoException("Já existe aluno com RA " + aluno.getRa(), e);
            }
            throw new RepositorioException("Erro ao salvar aluno.", e);
        }
    }

    @Override
    public Optional<Aluno> buscarPorRa(String ra) {
        try (Connection conexao = banco.abrirConexao();
             PreparedStatement comando = conexao.prepareStatement(BUSCAR_POR_RA)) {
            comando.setString(1, ra);
            try (ResultSet resultado = comando.executeQuery()) {
                if (resultado.next()) {
                    return Optional.of(converter(resultado));
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RepositorioException("Erro ao buscar aluno.", e);
        }
    }

    private Aluno converter(ResultSet resultado) throws SQLException {
        return new Aluno(
                resultado.getString("ra"),
                resultado.getString("nome"),
                resultado.getString("email"));
    }
}
