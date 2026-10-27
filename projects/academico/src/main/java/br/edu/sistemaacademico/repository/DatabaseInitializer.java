package br.edu.sistemaacademico.repository;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

@Component
public class DatabaseInitializer {
    private final DatabaseConnection database;

    public DatabaseInitializer(DatabaseConnection database) {
        this.database = database;
    }

    @PostConstruct
    public void inicializar() {
        executarArquivo("database/schema.sql");
        executarArquivo("database/dados-iniciais.sql");
    }

    private void executarArquivo(String caminho) {
        try {
            String script = new ClassPathResource(caminho)
                    .getContentAsString(StandardCharsets.UTF_8);
            try (Connection connection = database.abrir()) {
                try (Statement statement = connection.createStatement()) {
                    for (String comando : script.split(";")) {
                        String comandoSemComentario = comando.replaceAll("(?m)--.*$", "").trim();
                        if (!comandoSemComentario.isBlank()) {
                            statement.execute(comandoSemComentario);
                        }
                    }
                }
            }
        } catch (IOException | SQLException exception) {
            throw new DatabaseException("Erro ao inicializar o banco SQLite.", exception);
        }
    }
}
