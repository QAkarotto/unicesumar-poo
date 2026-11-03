package br.edu.sistemaacademico.repository.jdbc;

import br.edu.sistemaacademico.domain.Aluno;
import br.edu.sistemaacademico.repository.AlunoRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@Repository
public class AlunoRepositoryJdbc implements AlunoRepository {

    private final String databaseUrl;

    public AlunoRepositoryJdbc(
            @Value("${app.database.url}") String databaseUrl) {
        this.databaseUrl = databaseUrl;
    }

    @Override
    public Aluno salvar(Aluno aluno) {
        String sql = "INSERT INTO alunos " +
                "(identificador_academico, nome, email) VALUES (?, ?, ?)";

        try (Connection conexao = DriverManager.getConnection(databaseUrl);
             PreparedStatement comando = conexao.prepareStatement(sql)) {

            comando.setString(1, aluno.getIdentificadorAcademico());
            comando.setString(2, aluno.getNome());
            comando.setString(3, aluno.getEmail());

            comando.executeUpdate();

            return aluno;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar aluno.", e);
        }
    }

    @Override
    public Optional<Aluno> buscarPorRa(String ra) {
        String sql = "SELECT identificador_academico, nome, email " +
                "FROM alunos WHERE identificador_academico = ?";

        try (Connection conexao = DriverManager.getConnection(databaseUrl);
             PreparedStatement comando = conexao.prepareStatement(sql)) {

            comando.setString(1, ra);

            try (ResultSet resultado = comando.executeQuery()) {
                if (resultado.next()) {
                    Aluno aluno = new Aluno(
                            resultado.getString("identificador_academico"),
                            resultado.getString("nome"),
                            resultado.getString("email")
                    );

                    return Optional.of(aluno);
                }
            }

            return Optional.empty();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao consultar aluno.", e);
        }
    }
}