package br.edu.sistemaacademico.repository;

import br.edu.sistemaacademico.domain.Aluno;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcAlunoRepository implements AlunoRepository {

    private final ConexaoBanco conexao;

    public JdbcAlunoRepository(ConexaoBanco conexao) {
        this.conexao = conexao;
    }

    @Override
    public void salvar(Aluno aluno) {
        String sql = "INSERT INTO aluno (registro_academico, nome, email) VALUES (?, ?, ?)";
        try (Connection c = conexao.abrir();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, aluno.getRegistroAcademico());
            ps.setString(2, aluno.getNome());
            ps.setString(3, aluno.getEmail());
            ps.executeUpdate();
        } catch (SQLException e) {
            String msg = String.valueOf(e.getMessage()).toUpperCase();
            if (msg.contains("UNIQUE") || msg.contains("PRIMARY") || msg.contains("CONSTRAINT")) {
                throw new RegistroDuplicadoException("Aluno já cadastrado: " + aluno.getRegistroAcademico());
            }
            throw new PersistenciaException("Erro ao salvar aluno", e);
        }
    }

    @Override
    public Optional<Aluno> buscarPorRa(String ra) {
        String sql = "SELECT registro_academico, nome, email FROM aluno WHERE registro_academico = ?";
        try (Connection c = conexao.abrir();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, ra);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new Aluno(
                            rs.getString("registro_academico"),
                            rs.getString("nome"),
                            rs.getString("email")));
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao buscar aluno", e);
        }
    }
}