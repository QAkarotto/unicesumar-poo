package br.edu.sistemaacademico.jdbc;

import br.edu.sistemaacademico.domain.Disciplina;
import br.edu.sistemaacademico.repository.DisciplinaRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class DisciplinaJDBC implements DisciplinaRepository {
    private final String url;

    public DisciplinaJDBC(@Value("${app.database.url}") String url) {
        this.url = url;
    }

    @Override
    public Disciplina salvar(Disciplina disciplina) {
        String sql = "INSERT INTO disciplina (codigo, nome, carga_horaria) VALUES (?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, disciplina.getCodigo());
            stmt.setString(2, disciplina.getNome());
            stmt.setInt(3, disciplina.getCargaHoraria());
            stmt.executeUpdate();
            return disciplina;
        } catch (SQLException e) {
            throw new IllegalStateException("Erro ao salvar disciplina: " + e.getMessage());
        }
    }

    @Override
    public Disciplina buscarDisciplinaPorCodigo(String codigo) {
        String sql = "SELECT codigo, nome, carga_horaria FROM disciplina WHERE codigo = ?";
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, codigo);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return converter(rs);
                }
                return null;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Erro ao buscar disciplina: " + e.getMessage());
        }
    }

    @Override
    public List<Disciplina> listarTodas() {
        String sql = "SELECT codigo, nome, carga_horaria FROM disciplina";
        List<Disciplina> lista = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                lista.add(converter(rs));
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Erro ao listar disciplinas: " + e.getMessage());
        }
        return lista;
    }

    private Disciplina converter(ResultSet rs) throws SQLException {
        return new Disciplina(
                rs.getString("codigo"),
                rs.getString("nome"),
                rs.getInt("carga_horaria"));
    }
}