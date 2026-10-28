package br.edu.sistemaacademico.repository;

import br.edu.sistemaacademico.domain.Aluno;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcAlunoRepository implements AlunoRepository {

    private final String url;

    public JdbcAlunoRepository(@Value("${app.database.url}") String url) {
        this.url = url;
    }

    @Override
    public Aluno salvar(Aluno aluno) {
        String sql = "INSERT INTO aluno (identificador_academico, nome, email) VALUES (?, ?, ?)";

        try (Connection conexao = DriverManager.getConnection(url);
             PreparedStatement comando = conexao.prepareStatement(sql)) {

            comando.setString(1, aluno.getIdentificadorAcademico());
            comando.setString(2, aluno.getNome());
            comando.setString(3, aluno.getEmail());
            comando.executeUpdate();

            return aluno;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar aluno no banco.", e);
        }
    }

    @Override
    public Optional<Aluno> buscarPorIdentificador(String identificadorAcademico) {
        String sql = "SELECT identificador_academico, nome, email FROM aluno WHERE identificador_academico = ?";

        try (Connection conexao = DriverManager.getConnection(url);
             PreparedStatement comando = conexao.prepareStatement(sql)) {

            comando.setString(1, identificadorAcademico);

            try (ResultSet resultado = comando.executeQuery()) {
                if (resultado.next()) {
                    Aluno aluno = new Aluno(
                            resultado.getString("identificador_academico"),
                            resultado.getString("nome"),
                            resultado.getString("email")
                    );
                    return Optional.of(aluno);
                }
            }

            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar aluno no banco.", e);
        }
    }
}
