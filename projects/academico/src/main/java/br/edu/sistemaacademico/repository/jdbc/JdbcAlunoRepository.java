package br.edu.sistemaacademico.repository.jdbc;

import br.edu.sistemaacademico.domain.Aluno;
import br.edu.sistemaacademico.repository.AlunoRepository;
import br.edu.sistemaacademico.repository.PersistenciaException;
import br.edu.sistemaacademico.repository.RegistroDuplicadoException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import org.springframework.stereotype.Repository;

/**
 * Implementação de {@link AlunoRepository} com JDBC e SQLite.
 * <p>
 * Fluxo de cada operação: Connection → PreparedStatement → (ResultSet) → objeto Java.
 */
@Repository
public class JdbcAlunoRepository implements AlunoRepository {

    private static final String INSERIR = """
            INSERT INTO aluno (registro_academico, nome, email)
            VALUES (?, ?, ?)
            """;

    private static final String BUSCAR_POR_RA = """
            SELECT registro_academico, nome, email
            FROM aluno
            WHERE registro_academico = ?
            """;

    private final FabricaConexoes conexoes;

    public JdbcAlunoRepository(FabricaConexoes conexoes) {
        this.conexoes = conexoes;
    }

    @Override
    public Aluno salvar(Aluno aluno) {
        try (Connection conexao = conexoes.abrir();
             PreparedStatement comando = conexao.prepareStatement(INSERIR)) {

            comando.setString(1, aluno.getRegistroAcademico());
            comando.setString(2, aluno.getNome());
            comando.setString(3, aluno.getEmail());
            comando.executeUpdate();
            return aluno;

        } catch (SQLException erro) {
            if (ErrosSqlite.violouChavePrimaria(erro)) {
                throw new RegistroDuplicadoException(
                        "Já existe aluno com o registro acadêmico " + aluno.getRegistroAcademico() + "."
                );
            }
            throw new PersistenciaException("Erro ao salvar o aluno.", erro);
        }
    }

    @Override
    public Optional<Aluno> buscarPorRa(String registroAcademico) {
        try (Connection conexao = conexoes.abrir();
             PreparedStatement consulta = conexao.prepareStatement(BUSCAR_POR_RA)) {

            consulta.setString(1, registroAcademico);

            try (ResultSet resultado = consulta.executeQuery()) {
                if (!resultado.next()) {
                    return Optional.empty();
                }
                return Optional.of(paraAluno(resultado));
            }

        } catch (SQLException erro) {
            throw new PersistenciaException("Erro ao consultar o aluno.", erro);
        }
    }

    /** Converte a linha atual do ResultSet em um objeto de domínio. */
    private static Aluno paraAluno(ResultSet linha) throws SQLException {
        return new Aluno(
                linha.getString("registro_academico"),
                linha.getString("nome"),
                linha.getString("email")
        );
    }
}
