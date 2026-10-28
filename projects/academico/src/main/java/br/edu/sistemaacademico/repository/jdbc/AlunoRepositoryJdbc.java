package br.edu.sistemaacademico.repository.jdbc;

import br.edu.sistemaacademico.domain.Aluno;
import br.edu.sistemaacademico.repository.AlunoRepository;
import br.edu.sistemaacademico.repository.ErroDePersistenciaException;
import br.edu.sistemaacademico.repository.RegistroDuplicadoException;

import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@Repository
public class AlunoRepositoryJdbc implements AlunoRepository {

    private static final String SQL_EXISTE =
            "SELECT 1 FROM aluno WHERE registro_academico = ?";

    private static final String SQL_INSERIR =
            "INSERT INTO aluno (registro_academico, nome, email) VALUES (?, ?, ?)";

    private static final String SQL_BUSCAR_POR_RA =
            "SELECT registro_academico, nome, email FROM aluno WHERE registro_academico = ?";

    private final ConexaoFactory conexoes;

    public AlunoRepositoryJdbc(ConexaoFactory conexoes) {
        this.conexoes = conexoes;
    }

    @Override
    public Aluno salvar(Aluno aluno) {
        String ra = aluno.getRegistroAcademico();

        try (Connection conexao = conexoes.abrir()) {
            if (existe(conexao, ra)) {
                throw duplicado(ra);
            }

            try (PreparedStatement comando = conexao.prepareStatement(SQL_INSERIR)) {
                comando.setString(1, ra);
                comando.setString(2, aluno.getNome());
                comando.setString(3, aluno.getEmail());
                comando.executeUpdate();
            }

            return aluno;
        } catch (SQLException e) {
            if (ErrosSqlite.violouRestricao(e)) {
                throw duplicado(ra);
            }
            throw new ErroDePersistenciaException("Erro ao salvar o aluno.", e);
        }
    }

    @Override
    public Optional<Aluno> buscarPorRa(String ra) {
        try (Connection conexao = conexoes.abrir();
             PreparedStatement comando = conexao.prepareStatement(SQL_BUSCAR_POR_RA)) {

            comando.setString(1, ra);

            try (ResultSet resultado = comando.executeQuery()) {
                if (!resultado.next()) {
                    return Optional.empty();
                }
                return Optional.of(converter(resultado));
            }
        } catch (SQLException e) {
            throw new ErroDePersistenciaException("Erro ao consultar o aluno.", e);
        }
    }

    private boolean existe(Connection conexao, String ra) throws SQLException {
        try (PreparedStatement comando = conexao.prepareStatement(SQL_EXISTE)) {
            comando.setString(1, ra);
            try (ResultSet resultado = comando.executeQuery()) {
                return resultado.next();
            }
        }
    }

    private Aluno converter(ResultSet resultado) throws SQLException {
        try {
            return new Aluno(
                    resultado.getString("registro_academico"),
                    resultado.getString("nome"),
                    resultado.getString("email")
            );
        } catch (IllegalArgumentException e) {
            throw new ErroDePersistenciaException(
                    "Registro de aluno inválido no banco de dados.", e
            );
        }
    }

    private RegistroDuplicadoException duplicado(String ra) {
        return new RegistroDuplicadoException(
                "Já existe um aluno com o registro acadêmico " + ra + "."
        );
    }
}
