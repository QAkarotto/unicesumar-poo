
package br.edu.sistemaacademico.repository.jdbc;

import br.edu.sistemaacademico.domain.Aluno;
import br.edu.sistemaacademico.repository.AlunoRepository;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@Repository
public class AlunoRepositoryJDBC implements AlunoRepository {

    private final ConexaoBanco conexaoBanco;

    public AlunoRepositoryJDBC(ConexaoBanco conexaoBanco) {
        this.conexaoBanco = conexaoBanco;
    }

    @Override
    public void salvar(Aluno aluno) {
        String sql = """
                INSERT INTO aluno (registro_academico, nome, email)
                VALUES (?, ?, ?)
                """;

        try (Connection conexao = conexaoBanco.conectar();
             PreparedStatement comando = conexao.prepareStatement(sql)) {

            comando.setString(1, aluno.getRegistroAcademico());
            comando.setString(2, aluno.getNome());
            comando.setString(3, aluno.getEmail());

            comando.executeUpdate();

        } catch (SQLException e) {
            throw new IllegalStateException(
                    "Não foi possível salvar o aluno.", e);
        }
    }

    @Override
    public Optional<Aluno> buscarPorRegistroAcademico(String registroAcademico) {
        String sql = """
                SELECT registro_academico, nome, email
                FROM aluno
                WHERE registro_academico = ?
                """;

        try (Connection conexao = conexaoBanco.conectar();
             PreparedStatement comando = conexao.prepareStatement(sql)) {

            comando.setString(1, registroAcademico);

            try (ResultSet resultado = comando.executeQuery()) {
                if (resultado.next()) {
                    Aluno aluno = new Aluno(
                            resultado.getString("registro_academico"),
                            resultado.getString("nome"),
                            resultado.getString("email")
                    );

                    return Optional.of(aluno);
                }
            }

        } catch (SQLException e) {
            throw new IllegalStateException(
                    "Não foi possível buscar o aluno.", e);
        }

        return Optional.empty();
    }
}
