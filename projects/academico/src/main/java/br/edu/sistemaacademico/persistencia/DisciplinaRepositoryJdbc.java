package br.edu.sistemaacademico.persistencia;

import br.edu.sistemaacademico.domain.Disciplina;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class DisciplinaRepositoryJdbc implements DisciplinaRepository {

    private final ConexaoBanco conexao;

    public DisciplinaRepositoryJdbc(ConexaoBanco conexao) {
        this.conexao = conexao;
    }

    @Override
    public Disciplina salvar(Disciplina disciplina) {
        if (buscarPorCodigo(disciplina.getCodigo()).isPresent()) {
            throw new IllegalStateException("Já existe disciplina com este código.");
        }

        String sql = "INSERT INTO disciplina (codigo, nome, carga_horaria) VALUES (?, ?, ?)";

        try (Connection con = conexao.abrir();
             PreparedStatement comando = con.prepareStatement(sql)) {

            comando.setString(1, disciplina.getCodigo());
            comando.setString(2, disciplina.getNome());
            comando.setInt(3, disciplina.getCargaHoraria());

            comando.executeUpdate();
            return disciplina;
        } catch (SQLException erro) {
            throw new PersistenciaException("Não foi possível salvar a disciplina.", erro);
        }
    }

    @Override
    public Optional<Disciplina> buscarPorCodigo(String codigo) {
        String sql = "SELECT codigo, nome, carga_horaria FROM disciplina WHERE codigo = ?";

        try (Connection con = conexao.abrir();
             PreparedStatement comando = con.prepareStatement(sql)) {

            comando.setString(1, codigo);

            try (ResultSet resultado = comando.executeQuery()) {
                if (resultado.next()) {
                    return Optional.of(converterParaDisciplina(resultado));
                }
                return Optional.empty();
            }
        } catch (SQLException erro) {
            throw new PersistenciaException("Não foi possível consultar a disciplina.", erro);
        }
    }

    private static Disciplina converterParaDisciplina(ResultSet resultado) throws SQLException {
        return new Disciplina(
                resultado.getString("codigo"),
                resultado.getString("nome"),
                resultado.getInt("carga_horaria")
        );
    }
}
