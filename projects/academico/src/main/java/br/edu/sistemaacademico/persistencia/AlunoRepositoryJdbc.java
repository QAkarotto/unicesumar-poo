package br.edu.sistemaacademico.persistencia;

import br.edu.sistemaacademico.domain.Aluno;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class AlunoRepositoryJdbc implements AlunoRepository {

    private final ConexaoBanco conexao;

    public AlunoRepositoryJdbc(ConexaoBanco conexao) {
        this.conexao = conexao;
    }

    @Override
    public Aluno salvar(Aluno aluno) {
        if (buscarPorRa(aluno.getRegistroAcademico()).isPresent()) {
            throw new IllegalStateException("Já existe aluno com este registro acadêmico.");
        }

        String sql = "INSERT INTO aluno (registro_academico, nome, email) VALUES (?, ?, ?)";

        try (Connection con = conexao.abrir();
             PreparedStatement comando = con.prepareStatement(sql)) {

            comando.setString(1, aluno.getRegistroAcademico());
            comando.setString(2, aluno.getNome());
            comando.setString(3, aluno.getEmail());

            comando.executeUpdate();
            return aluno;
        } catch (SQLException erro) {
            throw new PersistenciaException("Não foi possível salvar o aluno.", erro);
        }
    }

    @Override
    public Optional<Aluno> buscarPorRa(String ra) {
        String sql = "SELECT registro_academico, nome, email FROM aluno WHERE registro_academico = ?";

        try (Connection con = conexao.abrir();
             PreparedStatement comando = con.prepareStatement(sql)) {

            comando.setString(1, ra);

            try (ResultSet resultado = comando.executeQuery()) {
                if (resultado.next()) {
                    return Optional.of(converterParaAluno(resultado));
                }
                return Optional.empty();
            }
        } catch (SQLException erro) {
            throw new PersistenciaException("Não foi possível consultar o aluno.", erro);
        }
    }

    private static Aluno converterParaAluno(ResultSet resultado) throws SQLException {
        return new Aluno(
                resultado.getString("registro_academico"),
                resultado.getString("nome"),
                resultado.getString("email")
        );
    }
}
