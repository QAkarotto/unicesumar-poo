package br.edu.sistemaacademico.jdbc;

import br.edu.sistemaacademico.domain.Aluno;
import br.edu.sistemaacademico.repository.AlunoRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class AlunoJDBC implements AlunoRepository {
    private final String url;

    public AlunoJDBC(@Value("${app.database.url}") String url) {
        this.url = url;
    }

    @Override
    public Aluno salvar(Aluno aluno) {
        String sql = "INSERT INTO aluno (registro_academico, nome, email) VALUES (?, ?, ?)";
        try{
            Connection conn = DriverManager.getConnection(url);
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, aluno.getRa());
            stmt.setString(2, aluno.getNome());
            stmt.setString(3, aluno.getEmail());
            stmt.executeUpdate();
            stmt.close();
            conn.close();
            return aluno;
        } catch (SQLException e) {
            throw new IllegalStateException("Erro ao salvar aluno: " + e.getMessage());
        }
    }

    @Override
    public Aluno buscarAlunoPorRa(String ra) {
        String sql = "SELECT registro_academico, nome, email FROM aluno WHERE registro_academico = ?";
        Aluno aluno=null;
        try{
            Connection conn = DriverManager.getConnection(url);
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, ra);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                aluno = new Aluno(
                        rs.getString("registro_academico"),
                        rs.getString("nome"),
                        rs.getString("email")
                );
            }
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            throw new IllegalStateException("Erro ao buscar aluno: " + e.getMessage());
        }
        return aluno;
    }

    @Override
    public List<Aluno> listarTodos() {
        String sql = "SELECT * FROM aluno";
        List<Aluno> lista = new ArrayList<>();
        try{
            Connection conn = DriverManager.getConnection(url);
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Aluno aluno = new Aluno(
                        rs.getString("registro_academico"),
                        rs.getString("nome"),
                        rs.getString("email")
                );
                lista.add(aluno);
            }
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            throw new IllegalStateException("Erro ao listar alunos: " + e.getMessage());
        }
        return lista;
    }
}