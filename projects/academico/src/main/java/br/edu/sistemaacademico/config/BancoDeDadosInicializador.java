package br.edu.sistemaacademico.config;

import br.edu.sistemaacademico.repository.PersistenciaException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Component
public class BancoDeDadosInicializador implements ApplicationRunner {
    private final String urlBanco;

    public BancoDeDadosInicializador(@Value("${app.database.url}") String urlBanco) {
        this.urlBanco = urlBanco;
    }

    @Override
    public void run(ApplicationArguments args) {
        try (Connection connection = DriverManager.getConnection(urlBanco)) {
            executarScript(connection, "database/schema.sql");
            executarScript(connection, "database/dados-iniciais.sql");
        } catch (SQLException | IOException excecao) {
            throw new PersistenciaException("Não foi possível preparar o banco de dados SQLite.", excecao);
        }
    }

    private void executarScript(Connection connection, String arquivo) throws IOException, SQLException {
        var recurso = new ClassPathResource(arquivo);
        String sql = new String(recurso.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        for (String comando : sql.split(";")) {
            if (!comando.isBlank()) {
                try (Statement statement = connection.createStatement()) {
                    statement.execute(comando);
                }
            }
        }
    }
}
