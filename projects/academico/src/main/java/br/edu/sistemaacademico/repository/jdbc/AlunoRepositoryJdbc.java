
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
public class AlunoRepositoryJdbc implements AlunoRepository {

    @Override
    public Aluno salvar(Aluno aluno) {
        String sql = "INSERT INTO aluno " +
                "(registro_academico, nome, email) " +
                "VALUES (?, ?, ?)";

        try (Connection conexao = ConexaoSQLite.conectar();
             PreparedStatement comando =
                     conexao.prepareStatement(sql)) {

            comando.setString(1, aluno.getRa());
            comando.setString(2, aluno.getNome());
            comando.setString(3, aluno.getEmail());

            comando.executeUpdate();

            return aluno;

        } catch (SQLException e) {
            throw new IllegalStateException(
                    "Erro ao salvar aluno no banco de dados.", e);
        }
    }

    @Override
    public Optional<Aluno> buscarPorRa(String ra) {
        String sql = "SELECT registro_academico, nome, email " +
                "FROM aluno WHERE registro_academico = ?";

        try (Connection conexao = ConexaoSQLite.conectar();
             PreparedStatement comando =
                     conexao.prepareStatement(sql)) {

            comando.setString(1, ra);

            try (ResultSet resultado = comando.executeQuery()) {

                if (resultado.next()) {
                    Aluno aluno = new Aluno(
                            resultado.getString("registro_academico"),
                            resultado.getString("nome"),
                            resultado.getString("email")
                    );

                    return Optional.of(aluno);
                }

                return Optional.empty();
            }

        } catch (SQLException e) {
            throw new IllegalStateException(
                    "Erro ao consultar aluno no banco de dados.", e);
        }
    }
}
