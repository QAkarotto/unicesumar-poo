package br.edu.sistemaacademico.repository.jdbc;

import br.edu.sistemaacademico.domain.Disciplina;
import br.edu.sistemaacademico.repository.DisciplinaRepository;
import br.edu.sistemaacademico.repository.PersistenciaException;
import br.edu.sistemaacademico.repository.RegistroDuplicadoException;
import org.springframework.stereotype.Repository;
import org.sqlite.SQLiteErrorCode;
import org.sqlite.SQLiteException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@Repository
public class JdbcDisciplinaRepository implements DisciplinaRepository {

    private final ConexaoBanco banco;

    public JdbcDisciplinaRepository(ConexaoBanco banco) {
        this.banco = banco;
    }

    @Override
    public Disciplina salvar(Disciplina disciplina) {
        String sql = "INSERT INTO disciplinas (codigo, nome, carga_horaria) VALUES (?, ?, ?)";

        try (Connection conexao = banco.abrir();
             PreparedStatement comando = conexao.prepareStatement(sql)) {
            comando.setString(1, disciplina.getCodigo());
            comando.setString(2, disciplina.getNome());
            comando.setInt(3, disciplina.getCargaHoraria());
            comando.executeUpdate();
            return disciplina;
        } catch (SQLException e) {
            if (e instanceof SQLiteException sqlite
                    && sqlite.getResultCode() == SQLiteErrorCode.SQLITE_CONSTRAINT_PRIMARYKEY) {
                throw new RegistroDuplicadoException("Já existe uma disciplina com este código.", e);
            }
            throw new PersistenciaException("Não foi possível salvar a disciplina.", e);
        }
    }

    @Override
    public Optional<Disciplina> buscarPorCodigo(String codigo) {
        String sql = "SELECT codigo, nome, carga_horaria FROM disciplinas WHERE codigo = ?";

        try (Connection conexao = banco.abrir();
             PreparedStatement comando = conexao.prepareStatement(sql)) {
            comando.setString(1, codigo);
            try (ResultSet resultado = comando.executeQuery()) {
                if (resultado.next()) {
                    return Optional.of(new Disciplina(
                            resultado.getString("codigo"),
                            resultado.getString("nome"),
                            resultado.getInt("carga_horaria")
                    ));
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new PersistenciaException("Não foi possível consultar a disciplina.", e);
        }
    }
}
