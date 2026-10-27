package br.edu.sistemaacademico.repository;

import br.edu.sistemaacademico.config.DatabaseConfig.Database;
import br.edu.sistemaacademico.domain.Aluno;
import org.springframework.stereotype.Repository;
import java.sql.*;
import java.util.Optional;

@Repository
public class AlunoRepositoryJdbc implements AlunoRepository {
    private final Database database;
    public AlunoRepositoryJdbc(Database database) { this.database = database; }

    @Override public Aluno salvar(Aluno aluno) {
        String sql = "INSERT INTO alunos (ra, nome, email) VALUES (?, ?, ?)";
        try (Connection c = database.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, aluno.getRegistroAcademico()); ps.setString(2, aluno.getNome()); ps.setString(3, aluno.getEmail());
            ps.executeUpdate(); return aluno;
        } catch (SQLException e) {
            if (e.getMessage() != null && e.getMessage().toLowerCase().contains("unique")) throw new IllegalArgumentException("Já existe aluno com esse RA.", e);
            throw new RepositorioException("Erro ao salvar aluno.", e);
        }
    }

    @Override public Optional<Aluno> buscarPorRa(String ra) {
        String sql = "SELECT ra, nome, email FROM alunos WHERE ra = ?";
        try (Connection c = database.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, ra); try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(new Aluno(rs.getString("ra"), rs.getString("nome"), rs.getString("email")));
                return Optional.empty();
            }
        } catch (SQLException e) { throw new RepositorioException("Erro ao consultar aluno.", e); }
    }
}
