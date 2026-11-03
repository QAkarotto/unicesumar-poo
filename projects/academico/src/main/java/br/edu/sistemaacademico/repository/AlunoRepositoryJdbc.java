package br.edu.sistemaacademico.repository;

import br.edu.sistemaacademico.domain.Aluno;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

@Repository
public class AlunoRepositoryJdbc implements AlunoRepository {

    private static final String INSERT =
            "INSERT INTO aluno (registro_academico, nome, email) VALUES (?, ?, ?)";
    private static final String SELECT_POR_RA =
            "SELECT registro_academico, nome, email FROM aluno WHERE registro_academico = ?";

    private final String url;

    public AlunoRepositoryJdbc(@Value("${app.database.url}") String url) {
        this.url = url;
    }

    @Override
    public Aluno salvar(Aluno aluno) {
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement ps = conn.prepareStatement(INSERT)) {
            ps.setString(1, aluno.getRegistroAcademico());
            ps.setString(2, aluno.getNome());
            ps.setString(3, aluno.getEmail());
            ps.executeUpdate();
            return aluno;
        } catch (SQLException e) {
            if ((e.getErrorCode() & 0xFF) == 19) {
                throw new RegistroDuplicadoException(
                        "Aluno já cadastrado: " + aluno.getRegistroAcademico());
            }
            throw new ErroPersistenciaException("Erro ao salvar aluno", e);
        }
    }

    @Override
    public Optional<Aluno> buscarPorRa(String ra) {
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement ps = conn.prepareStatement(SELECT_POR_RA)) {
            ps.setString(1, ra);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new Aluno(
                            rs.getString("registro_academico"),
                            rs.getString("nome"),
                            rs.getString("email")));
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new ErroPersistenciaException("Erro ao buscar aluno", e);
        }
    }
}