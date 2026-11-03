package br.edu.sistemaacademico.repository;

import br.edu.sistemaacademico.domain.Aluno;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

@Repository
public class AlunoRepositoryJdbc implements AlunoRepository {
    private final String url;

    public AlunoRepositoryJdbc(@Value("${app.database.url}") String url) {
        this.url = url;
    }

    @Override
    public Aluno salvar(Aluno aluno) {
        String sql = "INSERT INTO aluno (registro_academico, nome, email) VALUES (?, ?, ?)";

        try (Connection conexao = DriverManager.getConnection(url);
             PreparedStatement comando = conexao.prepareStatement(sql)) {
            comando.setString(1, aluno.getRegistroAcademico());
            comando.setString(2, aluno.getNome());
            comando.setString(3, aluno.getEmail());
            comando.executeUpdate();
            return aluno;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar o aluno no banco de dados.", e);
        }
    }

    @Override
    public Optional<Aluno> buscarPorRa(String registroAcademico) {
        String sql = "SELECT registro_academico, nome, email FROM aluno WHERE registro_academico = ?";

        try (Connection conexao = DriverManager.getConnection(url);
             PreparedStatement comando = conexao.prepareStatement(sql)) {
            comando.setString(1, registroAcademico);
            ResultSet resultado = comando.executeQuery();

            if (resultado.next()) {
                Aluno aluno = new Aluno(
                        resultado.getString("registro_academico"),
                        resultado.getString("nome"),
                        resultado.getString("email")
                );
                return Optional.of(aluno);
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar o aluno no banco de dados.", e);
        }
    }
}
