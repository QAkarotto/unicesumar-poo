package br.edu.sistemaacademico.config;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@Component
public class DatabaseInitializer {

    private final ConnectionFactory connectionFactory;

    public DatabaseInitializer(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    @PostConstruct
    public void inicializarBanco() {
        try (Connection conn = connectionFactory.getConnection()) {
            if (!tabelaExiste(conn, "aluno") || !tabelaExiste(conn, "disciplina")) {
                executarScriptSql(conn, "database/schema.sql");
                executarScriptSql(conn, "database/dados-iniciais.sql");
            }
        } catch (SQLException | IOException e) {
            throw new IllegalStateException("Falha ao inicializar o banco de dados SQLite: " + e.getMessage(), e);
        }
    }

    private boolean tabelaExiste(Connection conn, String nomeTabela) throws SQLException {
        String sql = "SELECT 1 FROM sqlite_master WHERE type = 'table' AND name = '" + nomeTabela + "'";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            return rs.next();
        }
    }

    private void executarScriptSql(Connection conn, String caminhoRelativo) throws IOException, SQLException {
        Path path = resolverCaminho(caminhoRelativo);
        if (path == null || !Files.exists(path)) {
            return;
        }

        String conteudo = Files.readString(path, StandardCharsets.UTF_8);
        try (Statement stmt = conn.createStatement()) {
            // Em SQLite JDBC, scripts contendo BEGIN TRANSACTION...COMMIT podem ser executados
            stmt.executeUpdate(conteudo);
        }
    }

    private Path resolverCaminho(String caminho) {
        Path direto = Path.of(caminho);
        if (Files.exists(direto)) {
            return direto;
        }
        Path noProjeto = Path.of("projects/academico").resolve(caminho);
        if (Files.exists(noProjeto)) {
            return noProjeto;
        }
        return direto;
    }
}
