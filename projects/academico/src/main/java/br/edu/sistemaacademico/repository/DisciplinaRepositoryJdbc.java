package br.edu.sistemaacademico.repository;

import br.edu.sistemaacademico.domain.Disciplina;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

@Repository
public class DisciplinaRepositoryJdbc implements DisciplinaRepository {

    private static final String INSERT =
            "INSERT INTO disciplina (codigo, nome, carga_horaria) VALUES (?, ?, ?)";
    private static final String SELECT_POR_CODIGO =
            "SELECT codigo, nome, carga_horaria FROM disciplina WHERE codigo = ?";

    private final String url;

    public DisciplinaRepositoryJdbc(@Value("${app.database.url}") String url) {
        this.url = url;
    }

    @Override
    public Disciplina salvar(Disciplina disciplina) {
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement ps = conn.prepareStatement(INSERT)) {
            ps.setString(1, disciplina.getCodigo());
            ps.setString(2, disciplina.getNome());
            ps.setInt(3, disciplina.getCargaHoraria());
            ps.executeUpdate();
            return disciplina;
        } catch (SQLException e) {
            if ((e.getErrorCode() & 0xFF) == 19) {
                throw new RegistroDuplicadoException(
                        "Disciplina já cadastrada: " + disciplina.getCodigo());
            }
            throw new ErroPersistenciaException("Erro ao salvar disciplina", e);
        }
    }

    @Override
    public Optional<Disciplina> buscarPorCodigo(String codigo) {
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement ps = conn.prepareStatement(SELECT_POR_CODIGO)) {
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
            throw new ErroPersistenciaException("Erro ao buscar disciplina", e);
        }
    }
}