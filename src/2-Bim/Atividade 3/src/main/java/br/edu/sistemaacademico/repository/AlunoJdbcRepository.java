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

    private final ConexaoBanco banco;

    public AlunoJdbcRepository(ConexaoBanco banco) {
        this.banco = banco;
    }

    @Override
    public Aluno salvar(Aluno aluno) {
        String sql = "INSERT INTO aluno (ra, nome, email) VALUES (?, ?, ?)";
        try (Connection conexao = banco.abrir();
             PreparedStatement comando = conexao.prepareStatement(sql)) {
            comando.setString(1, aluno.getIdentificadorAcademico());
            comando.setString(2, aluno.getNome());
            comando.setString(3, aluno.getEmail());
            comando.executeUpdate();
            return aluno;
        } catch (SQLException e) {
            if (duplicado(e)) {
                throw new IllegalStateException("Aluno já cadastrado.");
            }
            throw new RuntimeException("Erro ao salvar aluno", e);
        }
    }

    @Override
    public Optional<Aluno> buscarPorRa(String ra) {
        String sql = "SELECT ra, nome, email FROM aluno WHERE ra = ?";
        try (Connection conexao = banco.abrir();
             PreparedStatement comando = conexao.prepareStatement(sql)) {
            comando.setString(1, ra);
            try (ResultSet resultado = comando.executeQuery()) {
                if (!resultado.next()) {
                    return Optional.empty();
                }
                return Optional.of(new Aluno(
                        resultado.getString("ra"),
                        resultado.getString("nome"),
                        resultado.getString("email")));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar aluno", e);
        }
    }

    private boolean duplicado(SQLException e) {
        return e.getErrorCode() == 19
                || (e.getMessage() != null && e.getMessage().contains("UNIQUE"));
    }
}
