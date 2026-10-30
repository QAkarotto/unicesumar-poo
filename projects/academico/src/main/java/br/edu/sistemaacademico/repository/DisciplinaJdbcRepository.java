package br.edu.sistemaacademico.repository;

import br.edu.sistemaacademico.domain.Disciplina;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Optional;

@Repository
public class DisciplinaJdbcRepository implements DisciplinaRepository {

    private final DataSource dataSource;

    public DisciplinaJdbcRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Disciplina salvar(Disciplina disciplina) {
        String sql = "INSERT OR REPLACE INTO disciplinas (codigo, nome, carga_horaria) VALUES (?, ?, ?)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, disciplina.getCodigo());
            stmt.setString(2, disciplina.getNome());
            stmt.setInt(3, disciplina.getCargaHoraria());
            stmt.executeUpdate();
            return disciplina;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar disciplina no banco de dados", e);
        }
    }

    @Override
    public Optional<Disciplina> buscarPorCodigo(String codigo) {
        String sql = "SELECT codigo, nome, carga_horaria FROM disciplinas WHERE codigo = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, codigo);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Disciplina disciplina = new Disciplina(
                            rs.getString("codigo"),
                            rs.getString("nome"),
                            rs.getInt("carga_horaria")
                    );
                    return Optional.of(disciplina);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar disciplina por código", e);
        }
        return Optional.empty();
    }
}