package br.edu.sistemaacademico.repository;

import br.edu.sistemaacademico.domain.Disciplina;
import br.edu.sistemaacademico.infra.ConexaoSqlite;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

/** Implementação JDBC: SQL de Disciplina e conversão de registros em objetos Java. */
@Repository
public class JdbcDisciplinaRepository implements DisciplinaRepository {

    private static final String INSERIR =
            "INSERT INTO disciplina (codigo, nome, carga_horaria) VALUES (?, ?, ?)";
    private static final String BUSCAR_POR_CODIGO =
            "SELECT codigo, nome, carga_horaria FROM disciplina WHERE codigo = ?";

    private final ConexaoSqlite conexao;

    public JdbcDisciplinaRepository(ConexaoSqlite conexao) {
        this.conexao = conexao;
    }

    @Override
    public Disciplina salvar(Disciplina disciplina) {
        try (var connection = conexao.abrir();
             var ps = connection.prepareStatement(INSERIR)) {
            ps.setString(1, disciplina.getCodigo());
            ps.setString(2, disciplina.getNome());
            ps.setInt(3, disciplina.getCargaHoraria());
            ps.executeUpdate();
            return disciplina;
        } catch (SQLException e) {
            if (ErrosSqlite.violacaoDeUnicidade(e)) {
                throw new RegistroDuplicadoException(
                        "Já existe disciplina com o código " + disciplina.getCodigo() + ".", e);
            }
            throw new PersistenciaException("Erro ao salvar disciplina.", e);
        }
    }

    @Override
    public Optional<Disciplina> buscarPorCodigo(String codigo) {
        try (var connection = conexao.abrir();
             var ps = connection.prepareStatement(BUSCAR_POR_CODIGO)) {
            ps.setString(1, codigo);
            try (var rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(paraDisciplina(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao buscar disciplina.", e);
        }
    }

    private static Disciplina paraDisciplina(ResultSet rs) throws SQLException {
        return new Disciplina(
                rs.getString("codigo"),
                rs.getString("nome"),
                rs.getInt("carga_horaria"));
    }
}
