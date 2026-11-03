package br.edu.sistemaacademico.repository.jdbc;

import br.edu.sistemaacademico.domain.Disciplina;
import br.edu.sistemaacademico.repository.DisciplinaRepository;
import org.springframework.stereotype.Repository;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.Optional;

@Repository
public class DisciplinaRepositoryJdbc implements DisciplinaRepository {
    private final ConexaoSQLite banco;
    public DisciplinaRepositoryJdbc(ConexaoSQLite banco) { this.banco = banco; }
    @Override public Disciplina salvar(Disciplina disciplina) {
        String sql = "INSERT INTO disciplina (codigo, nome, carga_horaria) VALUES (?, ?, ?)";
        try (var c = banco.abrir(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, disciplina.getCodigo()); ps.setString(2, disciplina.getNome()); ps.setInt(3, disciplina.getCargaHoraria());
            ps.executeUpdate(); return disciplina;
        } catch (SQLException e) {
            if (e.getMessage() != null && e.getMessage().toLowerCase().contains("unique")) throw new IllegalStateException("Já existe uma disciplina com esse código.");
            throw new FalhaPersistenciaException("Erro ao salvar disciplina no SQLite.", e);
        }
    }
    @Override public Optional<Disciplina> buscarPorCodigo(String codigo) {
        String sql = "SELECT codigo, nome, carga_horaria FROM disciplina WHERE codigo = ?";
        try (var c = banco.abrir(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, codigo);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(new Disciplina(rs.getString("codigo"), rs.getString("nome"), rs.getInt("carga_horaria"))) : Optional.empty();
            }
        } catch (SQLException e) { throw new FalhaPersistenciaException("Erro ao consultar disciplina no SQLite.", e); }
    }
}
