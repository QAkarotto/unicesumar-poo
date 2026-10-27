package br.edu.sistemaacademico.api.repository;

import br.edu.sistemaacademico.api.infra.ConexaoSqlite;
import br.edu.sistemaacademico.api.infra.ErroDeAcessoADadosException;
import br.edu.sistemaacademico.api.infra.RegistroDuplicadoException;
import br.edu.sistemaacademico.domain.Disciplina;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@Repository
public class DisciplinaRepositoryJdbc implements DisciplinaRepository {

    private final ConexaoSqlite conexao;

    public DisciplinaRepositoryJdbc(ConexaoSqlite conexao) {
        this.conexao = conexao;
    }

    @Override
    public Disciplina salvar(Disciplina disciplina) {
        if (buscarPorCodigo(disciplina.getCodigo()).isPresent()) {
            throw new RegistroDuplicadoException(
                    "Já existe disciplina com código " + disciplina.getCodigo());
        }

        String sql = "INSERT INTO disciplina (codigo, nome, carga_horaria) VALUES (?, ?, ?)";

        try (Connection c = conexao.abrir();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, disciplina.getCodigo());
            ps.setString(2, disciplina.getNome());
            ps.setInt(3, disciplina.getCargaHoraria());
            ps.executeUpdate();
            return disciplina;
        } catch (SQLException e) {
            throw new ErroDeAcessoADadosException("Erro ao salvar disciplina", e);
        }
    }

    @Override
    public Optional<Disciplina> buscarPorCodigo(String codigo) {
        String sql = "SELECT codigo, nome, carga_horaria FROM disciplina WHERE codigo = ?";

        try (Connection c = conexao.abrir();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, codigo);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return Optional.empty();
                }
                return Optional.of(new Disciplina(
                        rs.getString("codigo"),
                        rs.getString("nome"),
                        rs.getInt("carga_horaria")));
            }
        } catch (SQLException e) {
            throw new ErroDeAcessoADadosException("Erro ao buscar disciplina", e);
        }
    }
}