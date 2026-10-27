
package br.edu.sistemaacademico.repository.jdbc;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@Component
public class InicializadorBanco {

    private final String url;

    public InicializadorBanco(
            @Value("${app.database.url}") String url) {
        this.url = url;
    }

    @PostConstruct
    public void inicializar() {
        try {
            Path banco = Path.of("database/sistema-academico.db");
            Files.createDirectories(banco.getParent());

            try (Connection conexao = DriverManager.getConnection(url)) {
                if (!tabelaExiste(conexao, "aluno")
                        && !tabelaExiste(conexao, "disciplina")) {

                    executarScript(
                            conexao,
                            localizarArquivo("schema.sql"));

                    executarScript(
                            conexao,
                            localizarArquivo("dados-iniciais.sql"));
                }
            }
        } catch (Exception e) {
            throw new IllegalStateException(
                    "Não foi possível inicializar o banco de dados.", e);
        }
    }

    private Path localizarArquivo(String nome) {
        Path[] caminhos = {
                Path.of("database", nome),
                Path.of("projects", "academico", "database", nome)
        };

        for (Path caminho : caminhos) {
            if (Files.exists(caminho)) {
                return caminho;
            }
        }

        throw new IllegalStateException(
                "Arquivo SQL não encontrado: " + nome);
    }

    private boolean tabelaExiste(Connection conexao, String nome)
            throws SQLException {

        String sql = """
                SELECT name
                FROM sqlite_master
                WHERE type = 'table' AND name = ?
                """;

        try (var comando = conexao.prepareStatement(sql)) {
            comando.setString(1, nome);

            try (ResultSet resultado = comando.executeQuery()) {
                return resultado.next();
            }
        }
    }

    private void executarScript(Connection conexao, Path caminho)
            throws Exception {

        String sql = Files.readString(caminho);

        try (Statement comando = conexao.createStatement()) {
            for (String instrucao : sql.split(";")) {
                if (!instrucao.isBlank()) {
                    comando.execute(instrucao);
                }
            }
        }
    }
}
