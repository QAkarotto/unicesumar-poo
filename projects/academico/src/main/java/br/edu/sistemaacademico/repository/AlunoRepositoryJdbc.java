package br.edu.sistemaacademico.repository;

import br.edu.sistemaacademico.domain.Aluno;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.Optional;

@Repository
public class AlunoRepositoryJdbc implements AlunoRepository {

    private static final String URL = "jdbc:sqlite:database/sistema-academico.db";

    @Override
    public Aluno salvar(Aluno aluno) {
        String sqlTabela = "CREATE TABLE IF NOT EXISTS alunos (" +
                           "registro_academico TEXT PRIMARY KEY, " +
                           "nome TEXT NOT NULL, " +
                           "email TEXT NOT NULL)";

        String sql = "INSERT INTO alunos (registro_academico, nome, email) VALUES (?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement()) {

            // Garante que a tabela 'alunos' existe antes de inserir
            stmt.execute(sqlTabela);

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, aluno.getRegistroAcademico());
                pstmt.setString(2, aluno.getNome());
                pstmt.setString(3, aluno.getEmail());
                pstmt.executeUpdate();
            }
            return aluno;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar aluno no banco: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Aluno> buscarPorRa(String ra) {
        String sql = "SELECT registro_academico, nome, email FROM alunos WHERE registro_academico = ?";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, ra);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Aluno aluno = new Aluno(
                            rs.getString("registro_academico"),
                            rs.getString("nome"),
                            rs.getString("email")
                    );
                    return Optional.of(aluno);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar aluno por RA: " + e.getMessage(), e);
        }
        return Optional.empty();
    }
}