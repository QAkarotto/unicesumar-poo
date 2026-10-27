
package br.edu.sistemaacademico.repository.jdbc;

import br.edu.sistemaacademico.domain.Aluno;
import br.edu.sistemaacademico.repository.AlunoRepository;
import br.edu.sistemaacademico.repository.PersistenciaException;
import br.edu.sistemaacademico.repository.RegistroDuplicadoException;

import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@Repository
public class JdbcAlunoRepository implements AlunoRepository {

    private final ConexaoSQLite conexao;

    public JdbcAlunoRepository(ConexaoSQLite conexao) {
        this.conexao = conexao;
    }

    @Override
    public Aluno salvar(Aluno aluno) {

        String sql = """
            INSERT INTO alunos
            (registro_academico, nome, email)
            VALUES (?, ?, ?)
            """;

        try (Connection connection = conexao.abrir();
             PreparedStatement ps =
                     connection.prepareStatement(sql)) {

            ps.setString(1, aluno.getRegistroAcademico());
            ps.setString(2, aluno.getNome());
            ps.setString(3, aluno.getEmail());

            ps.executeUpdate();

            return aluno;

        } catch (SQLException e) {

            if (e.getErrorCode() == 19
                    && e.getMessage() != null
                    && e.getMessage().contains("UNIQUE constraint failed")) {
                throw new RegistroDuplicadoException(
                        "Aluno com esse RA já cadastrado.", e);
            }

            throw new PersistenciaException(
                    "Falha ao salvar aluno.", e);
        }
    }

    @Override
    public Optional<Aluno> buscarPorRa(String ra) {

        String sql = """
            SELECT registro_academico, nome, email
            FROM alunos
            WHERE registro_academico = ?
            """;

        try (Connection connection = conexao.abrir();
             PreparedStatement ps =
                     connection.prepareStatement(sql)) {

            ps.setString(1, ra);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    Aluno aluno = new Aluno(
                            rs.getString("registro_academico"),
                            rs.getString("nome"),
                            rs.getString("email")
                    );

                    return Optional.of(aluno);
                }

                return Optional.empty();
            }

        } catch (SQLException e) {
            throw new PersistenciaException(
                    "Falha ao consultar aluno.", e);
        }
    }
}
