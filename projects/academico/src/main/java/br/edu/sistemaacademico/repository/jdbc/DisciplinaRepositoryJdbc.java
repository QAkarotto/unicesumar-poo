package br.edu.sistemaacademico.repository.jdbc;

import br.edu.sistemaacademico.config.ConnectionFactory;
import br.edu.sistemaacademico.domain.Disciplina;
import br.edu.sistemaacademico.repository.DisciplinaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class DisciplinaRepositoryJdbc implements DisciplinaRepository {

    private final ConnectionFactory connectionFactory;

    public DisciplinaRepositoryJdbc(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    @Override
    public Disciplina salvar(Disciplina disciplina) {
        String sql = "INSERT INTO disciplina (codigo, nome, carga_horaria) VALUES (?, ?, ?)";
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, disciplina.getCodigo());
            ps.setString(2, disciplina.getNome());
            ps.setInt(3, disciplina.getCargaHoraria());
            ps.executeUpdate();
            return disciplina;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar disciplina no banco de dados: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Disciplina> buscarPorCodigo(String codigo) {
        if (codigo == null || codigo.isBlank()) {
            return Optional.empty();
        }
        String sql = "SELECT codigo, nome, carga_horaria FROM disciplina WHERE codigo = ?";
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, codigo.trim());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapearDisciplina(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar disciplina por código: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public List<Disciplina> listarTodas() {
        String sql = "SELECT codigo, nome, carga_horaria FROM disciplina ORDER BY codigo";
        List<Disciplina> disciplinas = new ArrayList<>();
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                disciplinas.add(mapearDisciplina(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar disciplinas: " + e.getMessage(), e);
        }
        return disciplinas;
    }

    @Override
    public boolean existePorCodigo(String codigo) {
        if (codigo == null || codigo.isBlank()) {
            return false;
        }
        String sql = "SELECT 1 FROM disciplina WHERE codigo = ?";
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, codigo.trim());
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao verificar existência de disciplina: " + e.getMessage(), e);
        }
    }

    private Disciplina mapearDisciplina(ResultSet rs) throws SQLException {
        String codigo = rs.getString("codigo");
        String nome = rs.getString("nome");
        int cargaHoraria = rs.getInt("carga_horaria");
        return new Disciplina(codigo, nome, cargaHoraria);
    }
}
