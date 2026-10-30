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

/** Implementação de {@link AlunoRepository} com JDBC puro sobre SQLite. */
@Repository
public class JdbcAlunoRepository implements AlunoRepository {

    private static final String INSERIR =
            "INSERT INTO aluno (registro_academico, nome, email) VALUES (?, ?, ?)";
    private static final String BUSCAR_POR_RA =
            "SELECT registro_academico, nome, email FROM aluno WHERE registro_academico = ?";

    private final BancoDeDados banco;

    public JdbcAlunoRepository(BancoDeDados banco) {
        this.banco = banco;
    }

    @Override
    public Aluno salvar(Aluno aluno) {
        try (Connection conexao = banco.abrirConexao();
             PreparedStatement comando = conexao.prepareStatement(INSERIR)) {
            comando.setString(1, aluno.getRegistroAcademico());
            comando.setString(2, aluno.getNome());
            comando.setString(3, aluno.getEmail());
            comando.executeUpdate();
            return aluno;
        } catch (SQLException e) {
            if (ErrosSql.violacaoDeUnicidade(e)) {
                throw new RegistroDuplicadoException(
                        "Já existe um aluno com o RA " + aluno.getRegistroAcademico(), e);
            }
            throw new RepositorioException("Erro ao salvar o aluno " + aluno.getRegistroAcademico(), e);
        }
    }

    @Override
    public Optional<Aluno> buscarPorRa(String ra) {
        try (Connection conexao = banco.abrirConexao();
             PreparedStatement comando = conexao.prepareStatement(BUSCAR_POR_RA)) {
            comando.setString(1, ra);
            try (ResultSet resultado = comando.executeQuery()) {
                if (!resultado.next()) {
                    return Optional.empty();
                }
                // Converte a linha do banco de volta em um objeto de domínio.
                return Optional.of(new Aluno(
                        resultado.getString("registro_academico"),
                        resultado.getString("nome"),
                        resultado.getString("email")));
            }
        } catch (SQLException e) {
            throw new RepositorioException("Erro ao buscar o aluno " + ra, e);
        }
    }
}
