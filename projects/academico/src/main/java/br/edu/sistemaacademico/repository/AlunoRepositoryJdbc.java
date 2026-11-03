package br.edu.sistemaacademico.repository;

import br.edu.sistemaacademico.domain.Aluno;
import java.sql.*;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

@Repository
public class AlunoRepositoryJdbc implements AlunoRepository {

    @Value("${app.database.url}")
    private String url;

    @Override
    public Aluno salvar(Aluno aluno) {
        String sql = "INSERT INTO aluno (registro_academico, nome, email) VALUES (?, ?, ?)";

        try (Connection conexao = DriverManager.getConnection(url);
             PreparedStatement ps = conexao.prepareStatement(sql)) {

            ps.setString(1, aluno.getRegistroAcademico());
            ps.setString(2, aluno.getNome());
            ps.setString(3, aluno.getEmail());
            ps.executeUpdate();
            return aluno;

        } catch (SQLException e) {
            if (e.getMessage().contains("UNIQUE")) {
                throw new IllegalStateException("Aluno já cadastrado.");
            }
            throw new RuntimeException("Erro ao salvar aluno.", e);
        }
    }

    @Override
    public Optional<Aluno> buscarPorRa(String registroAcademico) {
        String sql = "SELECT registro_academico, nome, email FROM aluno WHERE registro_academico = ?";

        try (Connection conexao = DriverManager.getConnection(url);
             PreparedStatement ps = conexao.prepareStatement(sql)) {

            ps.setString(1, registroAcademico);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new Aluno(
                            rs.getString("registro_academico"),
                            rs.getString("nome"),
                            rs.getString("email")));
                }
                return Optional.empty();
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar aluno.", e);
        }
    }
}
