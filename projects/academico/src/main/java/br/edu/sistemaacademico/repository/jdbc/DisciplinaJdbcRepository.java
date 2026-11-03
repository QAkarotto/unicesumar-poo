package br.edu.sistemaacademico.repository.jdbc;

import br.edu.sistemaacademico.domain.Disciplina;
import br.edu.sistemaacademico.repository.DisciplinaRepository;
import br.edu.sistemaacademico.repository.PersistenciaException;
import br.edu.sistemaacademico.repository.RegistroDuplicadoException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

@Repository
public class DisciplinaJdbcRepository implements DisciplinaRepository {
    private static final String INSERIR = "INSERT INTO disciplinas (codigo, nome, carga_horaria) VALUES (?, ?, ?)";
    private static final String BUSCAR_POR_CODIGO = "SELECT codigo, nome, carga_horaria FROM disciplinas WHERE codigo = ?";
    private final String urlBanco;

    public DisciplinaJdbcRepository(@Value("${app.database.url}") String urlBanco) {
        this.urlBanco = urlBanco;
    }

    @Override
    public Disciplina salvar(Disciplina disciplina) {
        try (Connection connection = DriverManager.getConnection(urlBanco);
             PreparedStatement statement = connection.prepareStatement(INSERIR)) {
            statement.setString(1, disciplina.getCodigo());
            statement.setString(2, disciplina.getNome());
            statement.setInt(3, disciplina.getCargaHoraria());
            statement.executeUpdate();
            return disciplina;
        } catch (SQLException excecao) {
            if (excecao.getErrorCode() == 19) {
                throw new RegistroDuplicadoException("Já existe uma disciplina com este código.", excecao);
            }
            throw new PersistenciaException("Não foi possível salvar a disciplina.", excecao);
        }
    }

    @Override
    public Optional<Disciplina> buscarPorCodigo(String codigo) {
        try (Connection connection = DriverManager.getConnection(urlBanco);
             PreparedStatement statement = connection.prepareStatement(BUSCAR_POR_CODIGO)) {
            statement.setString(1, codigo);
            try (ResultSet resultado = statement.executeQuery()) {
                return resultado.next() ? Optional.of(mapear(resultado)) : Optional.empty();
            }
        } catch (SQLException excecao) {
            throw new PersistenciaException("Não foi possível consultar a disciplina.", excecao);
        }
    }

    private Disciplina mapear(ResultSet resultado) throws SQLException {
        return new Disciplina(resultado.getString("codigo"), resultado.getString("nome"),
                resultado.getInt("carga_horaria"));
    }
}
