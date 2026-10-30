package br.edu.sistemaacademico.repository;

import br.edu.sistemaacademico.domain.Aluno;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Optional;

@Repository
public class AlunoJdbcRepository implements AlunoRepository {

    private final DataSource dataSource;

    public AlunoJdbcRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Aluno salvar(Aluno aluno) {
        String sql = "INSERT OR REPLACE INTO alunos (registro_academico, nome, email) VALUES (?, ?, ?)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, aluno.getRegistroAcademico());
            stmt.setString(2, aluno.getNome());
            stmt.setString(3, aluno.getEmail());
            stmt.executeUpdate();
            return aluno;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar aluno no banco de dados", e);
        }
    }

    @Override
    public Optional<Aluno> buscarPorRa(String ra) {
        String sql = "SELECT registro_academico, nome, email FROM alunos WHERE registro_academico = ?";
        try (Connection conn = dataSource.getConnection();
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
            throw new RuntimeException("Erro ao buscar aluno por RA", e);
        }
        return Optional.empty();
    }
}