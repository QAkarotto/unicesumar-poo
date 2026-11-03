package br.edu.sistemaacademico.persistencia;

import br.edu.sistemaacademico.domain.Disciplina;
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
        var sql = "INSERT INTO disciplina (codigo, nome, carga_horaria) VALUES (?, ?, ?)";
        try (var con = conexao.abrir(); var comando = con.prepareStatement(sql)) {
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
        var sql = "SELECT codigo, nome, carga_horaria FROM disciplina WHERE codigo = ?";
        try (var con = conexao.abrir(); var comando = con.prepareStatement(sql)) {
            comando.setString(1, codigo);
            try (var resultado = comando.executeQuery()) {
                return resultado.next() ? Optional.of(paraDisciplina(resultado)) : Optional.empty();
            }
        } catch (SQLException erro) {
            throw new PersistenciaException("Não foi possível consultar a disciplina.", erro);
        }
    }

    private static Disciplina paraDisciplina(ResultSet resultado) throws SQLException {
        return new Disciplina(resultado.getString("codigo"), resultado.getString("nome"),
                resultado.getInt("carga_horaria"));
    }
}
