package br.edu.sistemaacademico.repository;

import br.edu.sistemaacademico.domain.Disciplina;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.Optional;

@Repository
public class DisciplinaRepositoryJdbc implements DisciplinaRepository {

    private static final String URL = "jdbc:sqlite:database/sistema-academico.db";

    @Override
    public Disciplina salvar(Disciplina disciplina) {
        String sqlTabela = "CREATE TABLE IF NOT EXISTS disciplinas (" +
                           "codigo TEXT PRIMARY KEY, " +
                           "nome TEXT NOT NULL, " +
                           "carga_horaria INTEGER NOT NULL)";

        String sql = "INSERT INTO disciplinas (codigo, nome, carga_horaria) VALUES (?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement()) {

            // Garante que a tabela 'disciplinas' existe antes de inserir
            stmt.execute(sqlTabela);

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, disciplina.getCodigo());
                pstmt.setString(2, disciplina.getNome());
                pstmt.setInt(3, disciplina.getCargaHoraria());
                pstmt.executeUpdate();
            }
            return disciplina;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar disciplina no banco: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Disciplina> buscarPorCodigo(String codigo) {
        String sql = "SELECT codigo, nome, carga_horaria FROM disciplinas WHERE codigo = ?";
        try (Connection conn = DriverManager.getConnection(URL);
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
            throw new RuntimeException("Erro ao buscar disciplina por código: " + e.getMessage(), e);
        }
        return Optional.empty();
    }
}