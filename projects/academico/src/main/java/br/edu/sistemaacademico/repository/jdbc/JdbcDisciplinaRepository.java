package br.edu.sistemaacademico.repository.jdbc;

import br.edu.sistemaacademico.domain.Disciplina;
import br.edu.sistemaacademico.repository.DisciplinaRepository;
import br.edu.sistemaacademico.repository.PersistenciaException;
import br.edu.sistemaacademico.repository.RegistroDuplicadoException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import org.springframework.stereotype.Repository;

/**
 * Implementação de {@link DisciplinaRepository} com JDBC e SQLite.
 */
@Repository
public class JdbcDisciplinaRepository implements DisciplinaRepository {

    private static final String INSERIR = """
            INSERT INTO disciplina (codigo, nome, carga_horaria)
            VALUES (?, ?, ?)
            """;

    private static final String BUSCAR_POR_CODIGO = """
            SELECT codigo, nome, carga_horaria
            FROM disciplina
            WHERE codigo = ?
            """;

    private final FabricaConexoes conexoes;

    public JdbcDisciplinaRepository(FabricaConexoes conexoes) {
        this.conexoes = conexoes;
    }

    @Override
    public Disciplina salvar(Disciplina disciplina) {
        try (Connection conexao = conexoes.abrir();
             PreparedStatement comando = conexao.prepareStatement(INSERIR)) {

            comando.setString(1, disciplina.getCodigo());
            comando.setString(2, disciplina.getNome());
            comando.setInt(3, disciplina.getCargaHoraria());
            comando.executeUpdate();
            return disciplina;

        } catch (SQLException erro) {
            if (ErrosSqlite.violouChavePrimaria(erro)) {
                throw new RegistroDuplicadoException(
                        "Já existe disciplina com o código " + disciplina.getCodigo() + "."
                );
            }
            throw new PersistenciaException("Erro ao salvar a disciplina.", erro);
        }
    }

    @Override
    public Optional<Disciplina> buscarPorCodigo(String codigo) {
        try (Connection conexao = conexoes.abrir();
             PreparedStatement consulta = conexao.prepareStatement(BUSCAR_POR_CODIGO)) {

            consulta.setString(1, codigo);

            try (ResultSet resultado = consulta.executeQuery()) {
                if (!resultado.next()) {
                    return Optional.empty();
                }
                return Optional.of(paraDisciplina(resultado));
            }

        } catch (SQLException erro) {
            throw new PersistenciaException("Erro ao consultar a disciplina.", erro);
        }
    }

    /** Converte a linha atual do ResultSet em um objeto de domínio. */
    private static Disciplina paraDisciplina(ResultSet linha) throws SQLException {
        return new Disciplina(
                linha.getString("codigo"),
                linha.getString("nome"),
                linha.getInt("carga_horaria")
        );
    }
}
