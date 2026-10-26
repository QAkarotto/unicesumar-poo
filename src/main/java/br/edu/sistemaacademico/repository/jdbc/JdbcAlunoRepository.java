package br.edu.sistemaacademico.repository.jdbc;

import br.edu.sistemaacademico.domain.Aluno;
import br.edu.sistemaacademico.repository.AlunoRepository;
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
public class JdbcAlunoRepository implements AlunoRepository {

    private final ConexaoBanco banco;

    public JdbcAlunoRepository(ConexaoBanco banco) {
        this.banco = banco;
    }

    @Override
    public Aluno salvar(Aluno aluno) {
        String sql = "INSERT INTO alunos (registro_academico, nome, email) VALUES (?, ?, ?)";

        try (Connection conexao = banco.abrir();
             PreparedStatement comando = conexao.prepareStatement(sql)) {
            comando.setString(1, aluno.getRegistroAcademico());
            comando.setString(2, aluno.getNome());
            comando.setString(3, aluno.getEmail());
            comando.executeUpdate();
            return aluno;
        } catch (SQLException e) {
            if (e instanceof SQLiteException sqlite
                    && sqlite.getResultCode() == SQLiteErrorCode.SQLITE_CONSTRAINT_PRIMARYKEY) {
                throw new RegistroDuplicadoException("Já existe um aluno com este RA.", e);
            }
            throw new PersistenciaException("Não foi possível salvar o aluno.", e);
        }
    }

    @Override
    public Optional<Aluno> buscarPorRa(String ra) {
        String sql = "SELECT registro_academico, nome, email FROM alunos WHERE registro_academico = ?";

        try (Connection conexao = banco.abrir();
             PreparedStatement comando = conexao.prepareStatement(sql)) {
            comando.setString(1, ra);
            try (ResultSet resultado = comando.executeQuery()) {
                if (resultado.next()) {
                    return Optional.of(new Aluno(
                            resultado.getString("registro_academico"),
                            resultado.getString("nome"),
                            resultado.getString("email")
                    ));
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new PersistenciaException("Não foi possível consultar o aluno.", e);
        }
    }
}
