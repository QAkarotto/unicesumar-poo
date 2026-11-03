package br.edu.sistemaacademico.repository;

import br.edu.sistemaacademico.domain.Aluno;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

@Repository
public class AlunoRepositoryJdbc implements AlunoRepository {
    private final String url;

    public AlunoRepositoryJdbc(@Value("${app.database.url}") String url) {
        this.url = url;
    }

    @Override
    public Aluno salvar(Aluno aluno) {
        var sql = "INSERT INTO aluno (registro_academico, nome, email) VALUES (?, ?, ?)";
        try (var conexao = DriverManager.getConnection(url);
             var comando = conexao.prepareStatement(sql)) {
            comando.setString(1, aluno.getRegistroAcademico());
            comando.setString(2, aluno.getNome());
            comando.setString(3, aluno.getEmail());
            comando.executeUpdate();
            return aluno;
        } catch (SQLException e) {
            if (e.getMessage().contains("SQLITE_CONSTRAINT_PRIMARYKEY")) {
                throw new IllegalStateException("Já existe um aluno com este registro acadêmico.");
            }
            throw new ErroBancoDadosException("Erro ao salvar o aluno.", e);
        }
    }

    @Override
    public Optional<Aluno> buscarPorRa(String ra) {
        var sql = "SELECT registro_academico, nome, email FROM aluno WHERE registro_academico = ?";
        try (var conexao = DriverManager.getConnection(url);
             var comando = conexao.prepareStatement(sql)) {
            comando.setString(1, ra);
            try (var resultado = comando.executeQuery()) {
                if (!resultado.next()) {
                    return Optional.empty();
                }
                return Optional.of(new Aluno(
                        resultado.getString("registro_academico"),
                        resultado.getString("nome"),
                        resultado.getString("email")));
            }
        } catch (SQLException e) {
            throw new ErroBancoDadosException("Erro ao buscar o aluno.", e);
        }
    }
}
