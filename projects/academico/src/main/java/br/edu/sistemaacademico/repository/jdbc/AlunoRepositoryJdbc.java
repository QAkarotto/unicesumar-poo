package br.edu.sistemaacademico.repository.jdbc;

import br.edu.sistemaacademico.Aluno;
import br.edu.sistemaacademico.repository.AlunoRepository;
import br.edu.sistemaacademico.repository.PersistenciaException;
import br.edu.sistemaacademico.repository.RegistroDuplicadoException;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

/**
 * Implementação de AlunoRepository usando JDBC puro com SQLite.
 * É aqui (e só aqui) que ficam os comandos SQL de aluno.
 */
@Repository
public class AlunoRepositoryJdbc implements AlunoRepository {

    private static final String SQL_INSERIR =
            "INSERT INTO aluno (registro_academico, nome, email) VALUES (?, ?, ?)";

    private static final String SQL_BUSCAR_POR_RA =
            "SELECT registro_academico, nome, email FROM aluno WHERE registro_academico = ?";

    private final ConexaoBanco conexaoBanco;

    public AlunoRepositoryJdbc(ConexaoBanco conexaoBanco) {
        this.conexaoBanco = conexaoBanco;
    }

    @Override
    public Aluno salvar(Aluno aluno) {
        // try-with-resources fecha a Connection e o PreparedStatement automaticamente
        try (Connection conexao = conexaoBanco.abrir();
             PreparedStatement comando = conexao.prepareStatement(SQL_INSERIR)) {

            // Os "?" do SQL são preenchidos aqui: objeto Java -> colunas da tabela
            comando.setString(1, aluno.getIdentificadorAcademico());
            comando.setString(2, aluno.getNome());
            comando.setString(3, aluno.getEmail());
            comando.executeUpdate();
            return aluno;

        } catch (SQLException e) {
            if (ErrosSqlite.violouChavePrimaria(e)) {
                throw new RegistroDuplicadoException(
                        "Já existe aluno com o RA " + aluno.getIdentificadorAcademico() + ".");
            }
            throw new PersistenciaException("Erro ao salvar aluno.", e);
        }
    }

    @Override
    public Optional<Aluno> buscarPorRa(String ra) {
        try (Connection conexao = conexaoBanco.abrir();
             PreparedStatement consulta = conexao.prepareStatement(SQL_BUSCAR_POR_RA)) {

            consulta.setString(1, ra);

            try (ResultSet resultado = consulta.executeQuery()) {
                if (resultado.next()) {
                    return Optional.of(paraAluno(resultado));
                }
                return Optional.empty();
            }

        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao buscar aluno " + ra + ".", e);
        }
    }

    // Linha da tabela -> objeto Java. O construtor de Aluno valida os dados de novo.
    private Aluno paraAluno(ResultSet linha) throws SQLException {
        return new Aluno(
                linha.getString("registro_academico"),
                linha.getString("nome"),
                linha.getString("email"));
    }
}
