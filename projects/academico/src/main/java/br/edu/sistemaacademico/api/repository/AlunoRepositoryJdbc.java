package br.edu.sistemaacademico.api.repository;

import br.edu.sistemaacademico.api.infra.ConexaoSqlite;
import br.edu.sistemaacademico.api.infra.ErroDeAcessoADadosException;
import br.edu.sistemaacademico.api.infra.RegistroDuplicadoException;
import br.edu.sistemaacademico.domain.Aluno;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@Repository
public class AlunoRepositoryJdbc implements AlunoRepository {

    private final ConexaoSqlite conexao;

    public AlunoRepositoryJdbc(ConexaoSqlite conexao) {
        this.conexao = conexao;
    }

    @Override
    public Aluno salvar(Aluno aluno) {
        if (buscarPorRa(aluno.getIdentificadorAcademico()).isPresent()) {
            throw new RegistroDuplicadoException(
                    "Já existe aluno com RA " + aluno.getIdentificadorAcademico());
        }

        String sql = "INSERT INTO aluno (registro_academico, nome, email) VALUES (?, ?, ?)";

        try (Connection c = conexao.abrir();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, aluno.getIdentificadorAcademico());
            ps.setString(2, aluno.getNome());
            ps.setString(3, aluno.getEmail());
            ps.executeUpdate();
            return aluno;
        } catch (SQLException e) {
            throw new ErroDeAcessoADadosException("Erro ao salvar aluno", e);
        }
    }

    @Override
    public Optional<Aluno> buscarPorRa(String ra) {
        String sql = "SELECT registro_academico, nome, email FROM aluno WHERE registro_academico = ?";

        try (Connection c = conexao.abrir();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, ra);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return Optional.empty();
                }
                // ResultSet -> objeto Java
                return Optional.of(new Aluno(
                        rs.getString("registro_academico"),
                        rs.getString("nome"),
                        rs.getString("email")));
            }
        } catch (SQLException e) {
            throw new ErroDeAcessoADadosException("Erro ao buscar aluno", e);
        }
    }
}