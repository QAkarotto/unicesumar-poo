package br.edu.sistemaacademico.repository;

import br.edu.sistemaacademico.domain.Disciplina;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@Repository
public class DisciplinaJdbcRepository implements DisciplinaRepository {
    private final ConexaoBanco conexaoBanco;

    public DisciplinaJdbcRepository(ConexaoBanco conexaoBanco) {
        this.conexaoBanco = conexaoBanco;
    }

    @Override
    public Disciplina salvar(Disciplina disciplina) {
        String sql = "INSERT INTO disciplina (codigo, nome, carga_horaria) VALUES (?, ?, ?)";
        try (Connection conexao = conexaoBanco.obterConexao();
             PreparedStatement comando = conexao.prepareStatement(sql)) {
            comando.setString(1, disciplina.getCodigo());
            comando.setString(2, disciplina.getNome());
            comando.setInt(3, disciplina.getCargaHoraria());
            comando.executeUpdate();
            return disciplina;
        } catch (SQLException e) {
            if (e.getErrorCode() == 19 || (e.getMessage() != null && e.getMessage().toLowerCase().contains("unique constraint"))) {
                throw new RegistroDuplicadoException("Já existe uma disciplina com esse código.", e);
            }
            throw new ErroPersistenciaException("Não foi possível salvar a disciplina.", e);
        }
    }

    @Override
    public Optional<Disciplina> buscarPorCodigo(String codigo) {
        String sql = "SELECT codigo, nome, carga_horaria FROM disciplina WHERE codigo = ?";
        try (Connection conexao = conexaoBanco.obterConexao();
             PreparedStatement comando = conexao.prepareStatement(sql)) {
            comando.setString(1, codigo);
            try (ResultSet resultado = comando.executeQuery()) {
                if (resultado.next()) {
                    return Optional.of(new Disciplina(
                            resultado.getString("codigo"),
                            resultado.getString("nome"),
                            resultado.getInt("carga_horaria")));
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new ErroPersistenciaException("Não foi possível consultar a disciplina.", e);
        }
    }
}
