package br.edu.sistemaacademico.repository.jdbc;

import br.edu.sistemaacademico.domain.Aluno;
import br.edu.sistemaacademico.repository.AlunoRepository;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcAlunoRepository implements AlunoRepository {

    @Override
    public Aluno salvar(Aluno aluno) {
        String sql = """
                INSERT INTO alunos
                    (identificador_academico, nome, email)
                VALUES (?, ?, ?)
                """;

        try (Connection conexao = ConexaoSQLite.conectar();
             PreparedStatement statement = conexao.prepareStatement(sql)) {

            statement.setString(1, aluno.getIdentificadorAcademico());
            statement.setString(2, aluno.getNome());
            statement.setString(3, aluno.getEmail());
            statement.executeUpdate();

            return aluno;
        } catch (SQLException e) {
            throw new IllegalStateException(
                    "Erro ao salvar aluno no banco de dados.", e
            );
        }
    }

    @Override
    public Optional<Aluno> buscarPorIdentificador(String identificador) {
        String sql = """
                SELECT identificador_academico, nome, email
                FROM alunos
                WHERE identificador_academico = ?
                """;

        try (Connection conexao = ConexaoSQLite.conectar();
             PreparedStatement statement = conexao.prepareStatement(sql)) {

            statement.setString(1, identificador);

            try (ResultSet resultado = statement.executeQuery()) {
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
            throw new IllegalStateException(
                    "Erro ao buscar aluno no banco de dados.", e
            );
        }
    }

    @Override
    public List<Aluno> listarTodos() {
        String sql = """
                SELECT identificador_academico, nome, email
                FROM alunos
                ORDER BY identificador_academico
                """;

        List<Aluno> alunos = new ArrayList<>();

        try (Connection conexao = ConexaoSQLite.conectar();
             PreparedStatement statement = conexao.prepareStatement(sql);
             ResultSet resultado = statement.executeQuery()) {

            while (resultado.next()) {
                alunos.add(new Aluno(
                        resultado.getString("identificador_academico"),
                        resultado.getString("nome"),
                        resultado.getString("email")
                ));
            }

            return alunos;
        } catch (SQLException e) {
            throw new IllegalStateException(
                    "Erro ao listar alunos no banco de dados.", e
            );
        }
    }
}
