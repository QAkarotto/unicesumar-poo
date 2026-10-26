package br.edu.sistemaacademico.repository.jdbc;

import br.edu.sistemaacademico.repository.RepositorioException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@Component
public class BancoDeDados {

    private static final Path PASTA_DATABASE = Path.of("database");

    private final String url;

    public BancoDeDados(@Value("${app.database.url}") String url) {
        this.url = url;
        inicializarSeNecessario();
    }

    // Cada operação abre a sua própria conexão e fecha ao terminar (try-with-resources)
    public Connection abrirConexao() throws SQLException {
        return DriverManager.getConnection(url);
    }

    // Só roda os scripts se a tabela ainda não existir: o schema.sql faz DROP TABLE,
    // então rodar sempre apagaria os dados a cada reinício da aplicação
    private void inicializarSeNecessario() {
        if (!Files.isDirectory(PASTA_DATABASE)) {
            throw new IllegalStateException("Pasta 'database' não encontrada em "
                    + Path.of("").toAbsolutePath()
                    + ". Configure o Working directory da execução para a pasta 'Trabalho 4'.");
        }

        try (Connection conexao = abrirConexao()) {
            if (!tabelaExiste(conexao, "aluno")) {
                executarScript(conexao, PASTA_DATABASE.resolve("schema.sql"));
                executarScript(conexao, PASTA_DATABASE.resolve("dados-iniciais.sql"));
            }
        } catch (SQLException | IOException e) {
            throw new RepositorioException("Falha ao inicializar o banco de dados", e);
        }
    }

    private boolean tabelaExiste(Connection conexao, String nomeTabela) throws SQLException {
        String sql = "SELECT 1 FROM sqlite_master WHERE type = 'table' AND name = ?";
        try (PreparedStatement comando = conexao.prepareStatement(sql)) {
            comando.setString(1, nomeTabela);
            try (ResultSet resultado = comando.executeQuery()) {
                return resultado.next();
            }
        }
    }

    // Os scripts não recebem parâmetros, por isso aqui um Statement simples é suficiente
    private void executarScript(Connection conexao, Path arquivo) throws IOException, SQLException {
        String script = Files.readString(arquivo, StandardCharsets.UTF_8);
        try (Statement comando = conexao.createStatement()) {
            for (String sql : script.split(";")) {
                if (!sql.isBlank()) {
                    comando.execute(sql);
                }
            }
        }
    }
}
