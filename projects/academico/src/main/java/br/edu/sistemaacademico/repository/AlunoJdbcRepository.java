package br.edu.sistemaacademico.repository;

import br.edu.sistemaacademico.domain.Aluno;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@Repository
public class AlunoJdbcRepository implements AlunoRepository {
    private final ConexaoBanco conexaoBanco;

    public AlunoJdbcRepository(ConexaoBanco conexaoBanco) {
        this.conexaoBanco = conexaoBanco;
    }

    @Override
    public Aluno salvar(Aluno aluno) {
        String sql = "INSERT INTO aluno (registro_academico, nome, email) VALUES (?, ?, ?)";
        try (Connection conexao = conexaoBanco.obterConexao();
             PreparedStatement comando = conexao.prepareStatement(sql)) {
            comando.setString(1, aluno.getIdentificadorAcademico());
            comando.setString(2, aluno.getNome());
            comando.setString(3, aluno.getEmail());
            comando.executeUpdate();
            return aluno;
        } catch (SQLException e) {
            if (e.getErrorCode() == 19 || (e.getMessage() != null && e.getMessage().toLowerCase().contains("unique constraint"))) {
                throw new RegistroDuplicadoException("Já existe um aluno com esse RA.", e);
            }
            throw new ErroPersistenciaException("Não foi possível salvar o aluno.", e);
        }
    }

    @Override
    public Optional<Aluno> buscarPorRa(String ra) {
        String sql = "SELECT registro_academico, nome, email FROM aluno WHERE registro_academico = ?";
        try (Connection conexao = conexaoBanco.obterConexao();
             PreparedStatement comando = conexao.prepareStatement(sql)) {
            comando.setString(1, ra);
            try (ResultSet resultado = comando.executeQuery()) {
                if (resultado.next()) {
                    return Optional.of(new Aluno(
                            resultado.getString("registro_academico"),
                            resultado.getString("nome"),
                            resultado.getString("email")));
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new ErroPersistenciaException("Não foi possível consultar o aluno.", e);
        }
    }
}
