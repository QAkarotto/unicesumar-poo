package br.edu.sistemaacademico.repository.jdbc;

import br.edu.sistemaacademico.domain.Aluno;
import br.edu.sistemaacademico.repository.AlunoRepository;
import org.springframework.stereotype.Repository;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.Optional;

@Repository
public class AlunoRepositoryJdbc implements AlunoRepository {
    private final ConexaoSQLite banco;
    public AlunoRepositoryJdbc(ConexaoSQLite banco) { this.banco = banco; }
    @Override public Aluno salvar(Aluno aluno) {
        String sql = "INSERT INTO aluno (registro_academico, nome, email) VALUES (?, ?, ?)";
        try (var c = banco.abrir(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, aluno.getRegistroAcademico()); ps.setString(2, aluno.getNome()); ps.setString(3, aluno.getEmail());
            ps.executeUpdate(); return aluno;
        } catch (SQLException e) {
            if (e.getMessage() != null && e.getMessage().toLowerCase().contains("unique")) throw new IllegalStateException("Já existe um aluno com esse registro acadêmico.");
            throw new FalhaPersistenciaException("Erro ao salvar aluno no SQLite.", e);
        }
    }
    @Override public Optional<Aluno> buscarPorRegistroAcademico(String ra) {
        String sql = "SELECT registro_academico, nome, email FROM aluno WHERE registro_academico = ?";
        try (var c = banco.abrir(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, ra);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(new Aluno(rs.getString("registro_academico"), rs.getString("nome"), rs.getString("email"))) : Optional.empty();
            }
        } catch (SQLException e) { throw new FalhaPersistenciaException("Erro ao consultar aluno no SQLite.", e); }
    }
}
