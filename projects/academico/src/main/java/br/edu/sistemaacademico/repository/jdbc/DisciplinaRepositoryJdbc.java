package br.edu.sistemaacademico.repository.jdbc;

import br.edu.sistemaacademico.domain.Disciplina;
import br.edu.sistemaacademico.repository.DisciplinaRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@Repository
public class DisciplinaRepositoryJdbc implements DisciplinaRepository {

    private final String databaseUrl;

    public DisciplinaRepositoryJdbc(
            @Value("${app.database.url}") String databaseUrl) {
        this.databaseUrl = databaseUrl;
    }

    @Override
    public Disciplina salvar(Disciplina disciplina) {
        String sql = "INSERT INTO disciplinas " +
                "(codigo, nome, carga_horaria) VALUES (?, ?, ?)";

        try (Connection conexao = DriverManager.getConnection(databaseUrl);
             PreparedStatement comando = conexao.prepareStatement(sql)) {

            comando.setString(1, disciplina.getCodigo());
            comando.setString(2, disciplina.getNome());
            comando.setInt(3, disciplina.getCargaHoraria());

            comando.executeUpdate();

            return disciplina;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar disciplina.", e);
        }
    }

    @Override
    public Optional<Disciplina> buscarPorCodigo(String codigo) {
        String sql = "SELECT codigo, nome, carga_horaria " +
                "FROM disciplinas WHERE codigo = ?";

        try (Connection conexao = DriverManager.getConnection(databaseUrl);
             PreparedStatement comando = conexao.prepareStatement(sql)) {

            comando.setString(1, codigo);

            try (ResultSet resultado = comando.executeQuery()) {
                if (resultado.next()) {
                    Disciplina disciplina = new Disciplina(
                            resultado.getString("codigo"),
                            resultado.getString("nome"),
                            resultado.getInt("carga_horaria")
                    );

                    return Optional.of(disciplina);
                }
            }

            return Optional.empty();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao consultar disciplina.", e);
        }
    }
}