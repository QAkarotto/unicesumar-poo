package br.edu.sistemaacademico.infra;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Fábrica de conexões JDBC para o arquivo SQLite. Cada operação do Repository abre
 * uma conexão, usa e fecha (try-with-resources).
 */
public class ConexaoSqlite {

    private final String url;

    public ConexaoSqlite(String caminhoDoArquivo) {
        var arquivo = Path.of(caminhoDoArquivo).toAbsolutePath();
        try {
            if (arquivo.getParent() != null) {
                Files.createDirectories(arquivo.getParent());
            }
        } catch (IOException e) {
            throw new IllegalStateException("Não foi possível criar a pasta do banco: " + arquivo.getParent(), e);
        }
        this.url = "jdbc:sqlite:" + arquivo;
    }

    public Connection abrir() throws SQLException {
        return DriverManager.getConnection(url);
    }

    public String getUrl() {
        return url;
    }
}
