package br.edu.sistemaacademico.persistencia;

import br.edu.sistemaacademico.domain.Aluno;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class AlunoRepositoryJdbc implements AlunoRepository {
    private static final String INSERIR =
            "INSERT INTO aluno (registro_academico, nome, email) VALUES (?, ?, ?)";
    private static final String BUSCAR_POR_RA =
            "SELECT registro_academico, nome, email FROM aluno WHERE registro_academico = ?";

    private final ConexaoBanco conexao;

    public AlunoRepositoryJdbc(ConexaoBanco conexao) {
        this.conexao = conexao;
    }

    @Override
    public Aluno salvar(Aluno aluno) {
        try (var con = conexao.abrir(); var comando = con.prepareStatement(INSERIR)) {
            comando.setString(1, aluno.getRegistroAcademico());
            comando.setString(2, aluno.getNome());
            comando.setString(3, aluno.getEmail());
            comando.executeUpdate();
            return aluno;
        } catch (SQLException erro) {
            if (ErrosSqlite.chaveDuplicada(erro)) {
                throw new RegistroDuplicadoException(
                        "Já existe aluno com o registro acadêmico " + aluno.getRegistroAcademico() + ".");
            }
            throw new PersistenciaException("Não foi possível salvar o aluno.", erro);
        }
    }

    @Override
    public Optional<Aluno> buscarPorRa(String registroAcademico) {
        try (var con = conexao.abrir(); var consulta = con.prepareStatement(BUSCAR_POR_RA)) {
            consulta.setString(1, registroAcademico);
            try (var registros = consulta.executeQuery()) {
                return registros.next() ? Optional.of(paraAluno(registros)) : Optional.empty();
            }
        } catch (SQLException erro) {
            throw new PersistenciaException("Não foi possível consultar o aluno.", erro);
        }
    }

    // o objeto volta pelo construtor, entao passa pelas mesmas validacoes do dominio
    private static Aluno paraAluno(ResultSet registro) throws SQLException {
        return new Aluno(registro.getString("registro_academico"), registro.getString("nome"),
                registro.getString("email"));
    }
}
