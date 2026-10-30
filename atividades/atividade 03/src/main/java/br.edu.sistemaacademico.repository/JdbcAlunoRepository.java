package br.edu.sistemaacademico.repository;

import br.edu.sistemaacademico.domain.Aluno;
import br.edu.sistemaacademico.infra.ConexaoSqlite;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

/** Implementação JDBC: é aqui que o SQL de Aluno fica e que registros viram objetos Java. */
@Repository
public class JdbcAlunoRepository implements AlunoRepository {

    private static final String INSERIR =
            "INSERT INTO aluno (registro_academico, nome, email) VALUES (?, ?, ?)";
    private static final String BUSCAR_POR_RA =
            "SELECT registro_academico, nome, email FROM aluno WHERE registro_academico = ?";

    private final ConexaoSqlite conexao;

    public JdbcAlunoRepository(ConexaoSqlite conexao) {
        this.conexao = conexao;
    }

    @Override
    public Aluno salvar(Aluno aluno) {
        try (var connection = conexao.abrir();
             var ps = connection.prepareStatement(INSERIR)) {
            ps.setString(1, aluno.getRegistroAcademico());
            ps.setString(2, aluno.getNome());
            ps.setString(3, aluno.getEmail());
            ps.executeUpdate();
            return aluno;
        } catch (SQLException e) {
            if (ErrosSqlite.violacaoDeUnicidade(e)) {
                throw new RegistroDuplicadoException(
                        "Já existe aluno com o registro acadêmico " + aluno.getRegistroAcademico() + ".", e);
            }
            throw new PersistenciaException("Erro ao salvar aluno.", e);
        }
    }

    @Override
    public Optional<Aluno> buscarPorRa(String registroAcademico) {
        try (var connection = conexao.abrir();
             var ps = connection.prepareStatement(BUSCAR_POR_RA)) {
            ps.setString(1, registroAcademico);
            try (var rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(paraAluno(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao buscar aluno.", e);
        }
    }

    // linha do banco (ResultSet) -> objeto de domínio
    private static Aluno paraAluno(ResultSet rs) throws SQLException {
        return new Aluno(
                rs.getString("registro_academico"),
                rs.getString("nome"),
                rs.getString("email"));
    }
}
