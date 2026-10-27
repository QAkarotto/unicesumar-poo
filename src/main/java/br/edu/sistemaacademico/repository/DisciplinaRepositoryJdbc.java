package br.edu.sistemaacademico.repository;

import br.edu.sistemaacademico.config.DatabaseConfig.Database;
import br.edu.sistemaacademico.domain.Disciplina;
import org.springframework.stereotype.Repository;
import java.sql.*;
import java.util.Optional;

@Repository
public class DisciplinaRepositoryJdbc implements DisciplinaRepository {
    private final Database database;
    public DisciplinaRepositoryJdbc(Database database) { this.database = database; }

    @Override public Disciplina salvar(Disciplina disciplina) {
        String sql = "INSERT INTO disciplinas (codigo, nome, carga_horaria) VALUES (?, ?, ?)";
        try (Connection c = database.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, disciplina.getCodigo()); ps.setString(2, disciplina.getNome()); ps.setInt(3, disciplina.getCargaHoraria());
            ps.executeUpdate(); return disciplina;
        } catch (SQLException e) {
            if (e.getMessage() != null && e.getMessage().toLowerCase().contains("unique")) throw new IllegalArgumentException("Já existe disciplina com esse código.", e);
            throw new RepositorioException("Erro ao salvar disciplina.", e);
        }
    }

    @Override public Optional<Disciplina> buscarPorCodigo(String codigo) {
        String sql = "SELECT codigo, nome, carga_horaria FROM disciplinas WHERE codigo = ?";
        try (Connection c = database.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, codigo); try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(new Disciplina(rs.getString("codigo"), rs.getString("nome"), rs.getInt("carga_horaria")));
                return Optional.empty();
            }
        } catch (SQLException e) { throw new RepositorioException("Erro ao consultar disciplina.", e); }
    }
}
