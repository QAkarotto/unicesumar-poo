package br.edu.sistemaacademico.repository.jdbc;

import br.edu.sistemaacademico.domain.Disciplina;
import br.edu.sistemaacademico.repository.DisciplinaRepository;
import br.edu.sistemaacademico.repository.ErroDePersistenciaException;
import br.edu.sistemaacademico.repository.RegistroDuplicadoException;

import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@Repository
public class DisciplinaRepositoryJdbc implements DisciplinaRepository {

    private static final String SQL_EXISTE =
            "SELECT 1 FROM disciplina WHERE codigo = ?";

    private static final String SQL_INSERIR =
            "INSERT INTO disciplina (codigo, nome, carga_horaria) VALUES (?, ?, ?)";

    private static final String SQL_BUSCAR_POR_CODIGO =
            "SELECT codigo, nome, carga_horaria FROM disciplina WHERE codigo = ?";

    private final ConexaoFactory conexoes;

    public DisciplinaRepositoryJdbc(ConexaoFactory conexoes) {
        this.conexoes = conexoes;
    }

    @Override
    public Disciplina salvar(Disciplina disciplina) {
        String codigo = disciplina.getCodigo();

        try (Connection conexao = conexoes.abrir()) {
            if (existe(conexao, codigo)) {
                throw duplicada(codigo);
            }

            try (PreparedStatement comando = conexao.prepareStatement(SQL_INSERIR)) {
                comando.setString(1, codigo);
                comando.setString(2, disciplina.getNome());
                comando.setInt(3, disciplina.getCargaHoraria());
                comando.executeUpdate();
            }

            return disciplina;
        } catch (SQLException e) {
            if (ErrosSqlite.violouRestricao(e)) {
                throw duplicada(codigo);
            }
            throw new ErroDePersistenciaException("Erro ao salvar a disciplina.", e);
        }
    }

    @Override
    public Optional<Disciplina> buscarPorCodigo(String codigo) {
        try (Connection conexao = conexoes.abrir();
             PreparedStatement comando = conexao.prepareStatement(SQL_BUSCAR_POR_CODIGO)) {

            comando.setString(1, codigo);

            try (ResultSet resultado = comando.executeQuery()) {
                if (!resultado.next()) {
                    return Optional.empty();
                }
                return Optional.of(converter(resultado));
            }
        } catch (SQLException e) {
            throw new ErroDePersistenciaException("Erro ao consultar a disciplina.", e);
        }
    }

    private boolean existe(Connection conexao, String codigo) throws SQLException {
        try (PreparedStatement comando = conexao.prepareStatement(SQL_EXISTE)) {
            comando.setString(1, codigo);
            try (ResultSet resultado = comando.executeQuery()) {
                return resultado.next();
            }
        }
    }

    private Disciplina converter(ResultSet resultado) throws SQLException {
        try {
            return new Disciplina(
                    resultado.getString("codigo"),
                    resultado.getString("nome"),
                    resultado.getInt("carga_horaria")
            );
        } catch (IllegalArgumentException e) {
            throw new ErroDePersistenciaException(
                    "Registro de disciplina inválido no banco de dados.", e
            );
        }
    }

    private RegistroDuplicadoException duplicada(String codigo) {
        return new RegistroDuplicadoException(
                "Já existe uma disciplina com o código " + codigo + "."
        );
    }
}
