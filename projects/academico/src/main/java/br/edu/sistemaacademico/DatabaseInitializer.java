
package br.edu.sistemaacademico;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

@Component
public class DatabaseInitializer implements CommandLineRunner {

    private final String databaseUrl;

    public DatabaseInitializer(
            @Value("${app.database.url}") String databaseUrl) {
        this.databaseUrl = databaseUrl;
    }

    @Override
    public void run(String... args) throws Exception {

        Files.createDirectories(Path.of("./database"));

        try (Connection conexao = DriverManager.getConnection(databaseUrl)) {

            executarArquivo(conexao, "database/schema.sql");
            executarArquivo(conexao, "database/dados-iniciais.sql");

        }

        System.out.println("Banco de dados preparado com sucesso!");
    }

    private void executarArquivo(Connection conexao, String arquivo)
            throws Exception {

        ClassPathResource resource = new ClassPathResource(arquivo);

        try (InputStream entrada = resource.getInputStream()) {

            String sql = new String(
                    entrada.readAllBytes(),
                    StandardCharsets.UTF_8
            );

            try (Statement comando = conexao.createStatement()) {

                for (String instrucao : sql.split(";")) {
                    if (!instrucao.isBlank()) {
                        comando.execute(instrucao);
                    }
                }
            }
        }
    }
}
