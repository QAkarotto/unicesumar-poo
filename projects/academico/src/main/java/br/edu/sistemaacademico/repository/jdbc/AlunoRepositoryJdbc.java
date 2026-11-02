package br.edu.sistemaacademico.repository.jdbc;

import br.edu.sistemaacademico.config.ConnectionFactory;
import br.edu.sistemaacademico.domain.Aluno;
import br.edu.sistemaacademico.repository.AlunoRepository;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class AlunoRepositoryJdbc implements AlunoRepository {

    private final ConnectionFactory connectionFactory;

    public AlunoRepositoryJdbc(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    @Override
    public Aluno salvar(Aluno aluno) {
        String sql = "INSERT INTO aluno (registro_academico, nome, email) VALUES (?, ?, ?)";
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, aluno.getRegistroAcademico());
            ps.setString(2, aluno.getNome());
            ps.setString(3, aluno.getEmail());
            ps.executeUpdate();
            return aluno;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar aluno no banco de dados: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Aluno> buscarPorRa(String ra) {
        if (ra == null || ra.isBlank()) {
            return Optional.empty();
        }
        String sql = "SELECT registro_academico, nome, email FROM aluno WHERE registro_academico = ?";
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, ra.trim());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapearAluno(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar aluno por registro acadêmico: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public List<Aluno> listarTodos() {
        String sql = "SELECT registro_academico, nome, email FROM aluno ORDER BY registro_academico";
        List<Aluno> alunos = new ArrayList<>();
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                alunos.add(mapearAluno(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar alunos: " + e.getMessage(), e);
        }
        return alunos;
    }

    @Override
    public boolean existePorRa(String ra) {
        if (ra == null || ra.isBlank()) {
            return false;
        }
        String sql = "SELECT 1 FROM aluno WHERE registro_academico = ?";
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, ra.trim());
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao verificar existência de aluno: " + e.getMessage(), e);
        }
    }

    private Aluno mapearAluno(ResultSet rs) throws SQLException {
        String ra = rs.getString("registro_academico");
        String nome = rs.getString("nome");
        String email = rs.getString("email");
        return new Aluno(ra, nome, email);
    }
}
