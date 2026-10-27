package br.edu.sistemaacademico.repository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Component
public class ConexaoBanco {
    private final String caminhoBanco;

    public ConexaoBanco(@Value("${sistema.academico.db:database/sistema-academico.db}") String caminhoBanco) {
        this.caminhoBanco = caminhoBanco;
    }

    public Connection obterConexao() throws SQLException {
        Path caminhoConfigurado = Path.of(caminhoBanco);
        // Funciona tanto abrindo `projects/academico` quanto a raiz do repositório Git.
        if ("database/sistema-academico.db".equals(caminhoBanco)
                && !Files.exists(Path.of("database"))
                && Files.exists(Path.of("projects/academico/database"))) {
            caminhoConfigurado = Path.of("projects/academico/database/sistema-academico.db");
        }
        Path caminho = caminhoConfigurado.toAbsolutePath();
        try {
            Path pasta = caminho.getParent();
            if (pasta != null) {
                java.nio.file.Files.createDirectories(pasta);
            }
        } catch (java.io.IOException e) {
            throw new SQLException("Não foi possível criar a pasta do banco SQLite.", e);
        }
        return DriverManager.getConnection("jdbc:sqlite:" + caminho);
    }
}
