package br.edu.sistemaacademico.repository;

import br.edu.sistemaacademico.domain.Disciplina;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

@Repository
public class DisciplinaRepositoryJdbc implements DisciplinaRepository {
    private final String url;

    public DisciplinaRepositoryJdbc(@Value("${app.database.url}") String url) {
        this.url = url;
    }

    @Override
    public Disciplina salvar(Disciplina disciplina) {
        var sql = "INSERT INTO disciplina (codigo, nome, carga_horaria) VALUES (?, ?, ?)";
        try (var conexao = DriverManager.getConnection(url);
             var comando = conexao.prepareStatement(sql)) {
            comando.setString(1, disciplina.getCodigo());
            comando.setString(2, disciplina.getNome());
            comando.setInt(3, disciplina.getCargaHoraria());
            comando.executeUpdate();
            return disciplina;
        } catch (SQLException e) {
            if (e.getMessage().contains("SQLITE_CONSTRAINT_PRIMARYKEY")) {
                throw new IllegalStateException("Já existe uma disciplina com este código.");
            }
            throw new ErroBancoDadosException("Erro ao salvar a disciplina.", e);
        }
    }

    @Override
    public Optional<Disciplina> buscarPorCodigo(String codigo) {
        var sql = "SELECT codigo, nome, carga_horaria FROM disciplina WHERE codigo = ?";
        try (var conexao = DriverManager.getConnection(url);
             var comando = conexao.prepareStatement(sql)) {
            comando.setString(1, codigo);
            try (var resultado = comando.executeQuery()) {
                if (!resultado.next()) {
                    return Optional.empty();
                }
                return Optional.of(new Disciplina(
                        resultado.getString("codigo"),
                        resultado.getString("nome"),
                        resultado.getInt("carga_horaria")));
            }
        } catch (SQLException e) {
            throw new ErroBancoDadosException("Erro ao buscar a disciplina.", e);
        }
    }
}
