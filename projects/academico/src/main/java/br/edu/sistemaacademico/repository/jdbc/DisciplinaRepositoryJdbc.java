package br.edu.sistemaacademico.repository.jdbc;

import br.edu.sistemaacademico.Disciplina;
import br.edu.sistemaacademico.repository.DisciplinaRepository;
import br.edu.sistemaacademico.repository.PersistenciaException;
import br.edu.sistemaacademico.repository.RegistroDuplicadoException;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

/**
 * Implementação de DisciplinaRepository usando JDBC puro com SQLite.
 */
@Repository
public class DisciplinaRepositoryJdbc implements DisciplinaRepository {

    private static final String SQL_INSERIR =
            "INSERT INTO disciplina (codigo, nome, carga_horaria) VALUES (?, ?, ?)";

    private static final String SQL_BUSCAR_POR_CODIGO =
            "SELECT codigo, nome, carga_horaria FROM disciplina WHERE codigo = ?";

    private final ConexaoBanco conexaoBanco;

    public DisciplinaRepositoryJdbc(ConexaoBanco conexaoBanco) {
        this.conexaoBanco = conexaoBanco;
    }

    @Override
    public Disciplina salvar(Disciplina disciplina) {
        try (Connection conexao = conexaoBanco.abrir();
             PreparedStatement comando = conexao.prepareStatement(SQL_INSERIR)) {

            comando.setString(1, disciplina.getCodigo());
            comando.setString(2, disciplina.getNome());
            comando.setInt(3, disciplina.getCargaHoraria());
            comando.executeUpdate();
            return disciplina;

        } catch (SQLException e) {
            if (ErrosSqlite.violouChavePrimaria(e)) {
                throw new RegistroDuplicadoException(
                        "Já existe disciplina com o código " + disciplina.getCodigo() + ".");
            }
            throw new PersistenciaException("Erro ao salvar disciplina.", e);
        }
    }

    @Override
    public Optional<Disciplina> buscarPorCodigo(String codigo) {
        try (Connection conexao = conexaoBanco.abrir();
             PreparedStatement consulta = conexao.prepareStatement(SQL_BUSCAR_POR_CODIGO)) {

            consulta.setString(1, codigo);

            try (ResultSet resultado = consulta.executeQuery()) {
                if (resultado.next()) {
                    return Optional.of(paraDisciplina(resultado));
                }
                return Optional.empty();
            }

        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao buscar disciplina " + codigo + ".", e);
        }
    }

    private Disciplina paraDisciplina(ResultSet linha) throws SQLException {
        return new Disciplina(
                linha.getString("codigo"),
                linha.getString("nome"),
                linha.getInt("carga_horaria"));
    }
}
