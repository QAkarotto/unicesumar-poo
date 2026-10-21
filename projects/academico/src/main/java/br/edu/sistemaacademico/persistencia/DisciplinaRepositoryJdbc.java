package br.edu.sistemaacademico.persistencia;

import br.edu.sistemaacademico.domain.Disciplina;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class DisciplinaRepositoryJdbc implements DisciplinaRepository {
    private static final String INSERIR =
            "INSERT INTO disciplina (codigo, nome, carga_horaria) VALUES (?, ?, ?)";
    private static final String BUSCAR_POR_CODIGO =
            "SELECT codigo, nome, carga_horaria FROM disciplina WHERE codigo = ?";

    private final ConexaoBanco conexao;

    public DisciplinaRepositoryJdbc(ConexaoBanco conexao) {
        this.conexao = conexao;
    }

    @Override
    public Disciplina salvar(Disciplina disciplina) {
        try (var con = conexao.abrir(); var comando = con.prepareStatement(INSERIR)) {
            comando.setString(1, disciplina.getCodigo());
            comando.setString(2, disciplina.getNome());
            comando.setInt(3, disciplina.getCargaHoraria());
            comando.executeUpdate();
            return disciplina;
        } catch (SQLException erro) {
            if (ErrosSqlite.chaveDuplicada(erro)) {
                throw new RegistroDuplicadoException(
                        "Já existe disciplina com o código " + disciplina.getCodigo() + ".");
            }
            throw new PersistenciaException("Não foi possível salvar a disciplina.", erro);
        }
    }

    @Override
    public Optional<Disciplina> buscarPorCodigo(String codigo) {
        try (var con = conexao.abrir(); var consulta = con.prepareStatement(BUSCAR_POR_CODIGO)) {
            consulta.setString(1, codigo);
            try (var registros = consulta.executeQuery()) {
                return registros.next() ? Optional.of(paraDisciplina(registros)) : Optional.empty();
            }
        } catch (SQLException erro) {
            throw new PersistenciaException("Não foi possível consultar a disciplina.", erro);
        }
    }

    private static Disciplina paraDisciplina(ResultSet registro) throws SQLException {
        return new Disciplina(registro.getString("codigo"), registro.getString("nome"),
                registro.getInt("carga_horaria"));
    }
}
