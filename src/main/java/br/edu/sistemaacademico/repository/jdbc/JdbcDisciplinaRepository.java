package br.edu.sistemaacademico.repository.jdbc;

import br.edu.sistemaacademico.domain.Disciplina;
import br.edu.sistemaacademico.repository.DisciplinaRepository;
import br.edu.sistemaacademico.repository.PersistenciaException;
import br.edu.sistemaacademico.repository.RegistroDuplicadoException;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@Repository
public class JdbcDisciplinaRepository implements DisciplinaRepository {

    private static final String INSERT =
            "INSERT INTO disciplina (codigo, nome, carga_horaria) VALUES (?, ?, ?)";
    private static final String SELECT_POR_CODIGO =
            "SELECT codigo, nome, carga_horaria FROM disciplina WHERE codigo = ?";

    private final ConexaoSqlite conexao;

    public JdbcDisciplinaRepository(ConexaoSqlite conexao) {
        this.conexao = conexao;
    }

    @Override
    public Disciplina salvar(Disciplina disciplina) {
        try (Connection c = conexao.abrir();
             PreparedStatement ps = c.prepareStatement(INSERT)) {
            ps.setString(1, disciplina.getCodigo());
            ps.setString(2, disciplina.getNome());
            ps.setInt(3, disciplina.getCargaHoraria());
            ps.executeUpdate();
            return disciplina;
        } catch (SQLException e) {
            if (ViolacaoDeUnicidade.eh(e)) {
                throw new RegistroDuplicadoException(
                        "Já existe uma disciplina com o código " + disciplina.getCodigo() + ".", e);
            }
            throw new PersistenciaException("Erro ao salvar disciplina.", e);
        }
    }

    @Override
    public Optional<Disciplina> buscarPorCodigo(String codigo) {
        try (Connection c = conexao.abrir();
             PreparedStatement ps = c.prepareStatement(SELECT_POR_CODIGO)) {
            ps.setString(1, codigo);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return Optional.empty();
                }
                return Optional.of(new Disciplina(
                        rs.getString("codigo"),
                        rs.getString("nome"),
                        rs.getInt("carga_horaria")));
            }
        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao buscar disciplina.", e);
        }
    }
}
