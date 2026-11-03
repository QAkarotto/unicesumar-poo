package br.edu.sistemaacademico.repository.jdbc;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Único lugar que sabe ONDE está o banco.
 * A URL vem de application.properties (app.database.url).
 */
@Component
public class ConexaoBanco {

    private static final String PREFIXO_SQLITE = "jdbc:sqlite:";

    private final String url;

    public ConexaoBanco(@Value("${app.database.url}") String url) {
        this.url = url;
        criarPastaDoArquivo();
    }

    /**
     * Abre uma nova conexão. Quem chama deve fechá-la
     * (usamos try-with-resources nos Repositories).
     */
    public Connection abrir() throws SQLException {
        return DriverManager.getConnection(url);
    }

    // O SQLite cria o arquivo .db sozinho, mas não cria a pasta "database/".
    private void criarPastaDoArquivo() {
        if (!url.startsWith(PREFIXO_SQLITE)) {
            return;
        }
        Path pasta = Path.of(url.substring(PREFIXO_SQLITE.length()))
                .toAbsolutePath()
                .getParent();
        try {
            if (pasta != null) {
                Files.createDirectories(pasta);
            }
        } catch (IOException e) {
            throw new UncheckedIOException("Não foi possível criar a pasta do banco: " + pasta, e);
        }
    }
}
