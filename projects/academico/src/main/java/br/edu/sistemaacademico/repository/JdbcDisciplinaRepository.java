// repository/JdbcDisciplinaRepository.java
package br.edu.sistemaacademico.repository;

import br.edu.sistemaacademico.domain.Disciplina;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcDisciplinaRepository implements DisciplinaRepository {

    private final ConexaoBanco conexao;

    public JdbcDisciplinaRepository(ConexaoBanco conexao) {
        this.conexao = conexao;
    }

    @Override
    public void salvar(Disciplina disciplina) {
        String sql = "INSERT INTO disciplina (codigo, nome, carga_horaria) VALUES (?, ?, ?)";
        try (Connection c = conexao.abrir();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, disciplina.getCodigo());
            ps.setString(2, disciplina.getNome());
            ps.setInt(3, disciplina.getCargaHoraria());
            ps.executeUpdate();
        } catch (SQLException e) {
            String msg = String.valueOf(e.getMessage()).toUpperCase();
            if (msg.contains("UNIQUE") || msg.contains("PRIMARY")) {
                throw new RegistroDuplicadoException("Disciplina já cadastrada: " + disciplina.getCodigo());
            }
            throw new PersistenciaException("Erro ao salvar disciplina", e);
        }
    }

    @Override
    public Optional<Disciplina> buscarPorCodigo(String codigo) {
        String sql = "SELECT codigo, nome, carga_horaria FROM disciplina WHERE codigo = ?";
        try (Connection c = conexao.abrir();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, codigo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new Disciplina(
                            rs.getString("codigo"),
                            rs.getString("nome"),
                            rs.getInt("carga_horaria")));
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao buscar disciplina", e);
        }
    }
}