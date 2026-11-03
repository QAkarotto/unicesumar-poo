package br.edu.sistemaacademico.repository.jdbc;

import br.edu.sistemaacademico.domain.Aluno;
import br.edu.sistemaacademico.repository.AlunoRepository;
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
public class AlunoJdbcRepository implements AlunoRepository {
    private static final String INSERIR = "INSERT INTO alunos (registro_academico, nome, email) VALUES (?, ?, ?)";
    private static final String BUSCAR_POR_RA = "SELECT registro_academico, nome, email FROM alunos WHERE registro_academico = ?";
    private final String urlBanco;

    public AlunoJdbcRepository(@Value("${app.database.url}") String urlBanco) {
        this.urlBanco = urlBanco;
    }

    @Override
    public Aluno salvar(Aluno aluno) {
        try (Connection connection = DriverManager.getConnection(urlBanco);
             PreparedStatement statement = connection.prepareStatement(INSERIR)) {
            statement.setString(1, aluno.getRegistroAcademico());
            statement.setString(2, aluno.getNome());
            statement.setString(3, aluno.getEmail());
            statement.executeUpdate();
            return aluno;
        } catch (SQLException excecao) {
            if (excecao.getErrorCode() == 19) {
                throw new RegistroDuplicadoException("Já existe um aluno com este registro acadêmico.", excecao);
            }
            throw new PersistenciaException("Não foi possível salvar o aluno.", excecao);
        }
    }

    @Override
    public Optional<Aluno> buscarPorRegistroAcademico(String registroAcademico) {
        try (Connection connection = DriverManager.getConnection(urlBanco);
             PreparedStatement statement = connection.prepareStatement(BUSCAR_POR_RA)) {
            statement.setString(1, registroAcademico);
            try (ResultSet resultado = statement.executeQuery()) {
                return resultado.next() ? Optional.of(mapear(resultado)) : Optional.empty();
            }
        } catch (SQLException excecao) {
            throw new PersistenciaException("Não foi possível consultar o aluno.", excecao);
        }
    }

    private Aluno mapear(ResultSet resultado) throws SQLException {
        return new Aluno(resultado.getString("registro_academico"), resultado.getString("nome"),
                resultado.getString("email"));
    }
}
