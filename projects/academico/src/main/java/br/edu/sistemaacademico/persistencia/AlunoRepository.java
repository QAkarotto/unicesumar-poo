package br.edu.sistemaacademico.persistencia;

import br.edu.sistemaacademico.domain.Aluno;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class AlunoRepository {
    private final ConexaoBanco conexao;

    public AlunoRepository(ConexaoBanco conexao) {
        this.conexao = conexao;
    }

    public void salvar(Aluno aluno) {
        if (buscarPorRegistroAcademico(aluno.getRegistroAcademico()).isPresent()) {
            throw new IllegalArgumentException("Já existe aluno com este registro acadêmico.");
        }
        var sql = "INSERT INTO aluno (registro_academico, nome, email) VALUES (?, ?, ?)";
        try (var con = conexao.abrir(); var comando = con.prepareStatement(sql)) {
            comando.setString(1, aluno.getRegistroAcademico());
            comando.setString(2, aluno.getNome());
            comando.setString(3, aluno.getEmail());
            comando.executeUpdate();
        } catch (SQLException erro) {
            throw new PersistenciaException("Não foi possível salvar o aluno.", erro);
        }
    }

    public Optional<Aluno> buscarPorRegistroAcademico(String registroAcademico) {
        var sql = "SELECT registro_academico, nome, email FROM aluno WHERE registro_academico = ?";
        try (var con = conexao.abrir(); var comando = con.prepareStatement(sql)) {
            comando.setString(1, registroAcademico);
            try (var resultado = comando.executeQuery()) {
                return resultado.next() ? Optional.of(paraAluno(resultado)) : Optional.empty();
            }
        } catch (SQLException erro) {
            throw new PersistenciaException("Não foi possível consultar o aluno.", erro);
        }
    }

    public List<Aluno> listarTodos() {
        var sql = "SELECT registro_academico, nome, email FROM aluno ORDER BY registro_academico";
        try (var con = conexao.abrir(); var comando = con.prepareStatement(sql);
             var resultado = comando.executeQuery()) {
            var alunos = new ArrayList<Aluno>();
            while (resultado.next()) {
                alunos.add(paraAluno(resultado));
            }
            return alunos;
        } catch (SQLException erro) {
            throw new PersistenciaException("Não foi possível listar os alunos.", erro);
        }
    }

    private static Aluno paraAluno(ResultSet resultado) throws SQLException {
        return new Aluno(resultado.getString("registro_academico"), resultado.getString("nome"),
                resultado.getString("email"));
    }
}
