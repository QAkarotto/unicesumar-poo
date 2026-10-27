package br.edu.sistemaacademico.repository.jdbc;

import br.edu.sistemaacademico.domain.Aluno;
import br.edu.sistemaacademico.repository.AlunoRepository;
import br.edu.sistemaacademico.repository.PersistenciaException;
import br.edu.sistemaacademico.repository.RegistroDuplicadoException;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@Repository
public class JdbcAlunoRepository implements AlunoRepository {

    private static final String INSERT =
            "INSERT INTO aluno (registro_academico, nome, email) VALUES (?, ?, ?)";
    private static final String SELECT_POR_RA =
            "SELECT registro_academico, nome, email FROM aluno WHERE registro_academico = ?";

    private final ConexaoSqlite conexao;

    public JdbcAlunoRepository(ConexaoSqlite conexao) {
        this.conexao = conexao;
    }

    @Override
    public Aluno salvar(Aluno aluno) {
        try (Connection c = conexao.abrir();
             PreparedStatement ps = c.prepareStatement(INSERT)) {
            ps.setString(1, aluno.getRegistroAcademico());
            ps.setString(2, aluno.getNome());
            ps.setString(3, aluno.getEmail());
            ps.executeUpdate();
            return aluno;
        } catch (SQLException e) {
            if (ViolacaoDeUnicidade.eh(e)) {
                throw new RegistroDuplicadoException(
                        "Já existe um aluno com o RA " + aluno.getRegistroAcademico() + ".", e);
            }
            throw new PersistenciaException("Erro ao salvar aluno.", e);
        }
    }

    @Override
    public Optional<Aluno> buscarPorRa(String registroAcademico) {
        try (Connection c = conexao.abrir();
             PreparedStatement ps = c.prepareStatement(SELECT_POR_RA)) {
            ps.setString(1, registroAcademico);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return Optional.empty();
                }
                // Linha do banco -> objeto Java
                return Optional.of(new Aluno(
                        rs.getString("registro_academico"),
                        rs.getString("nome"),
                        rs.getString("email")));
            }
        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao buscar aluno.", e);
        }
    }
}
