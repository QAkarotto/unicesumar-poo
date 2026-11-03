package br.edu.sistemaacademico.repository;

import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

// Roda quando a aplicação inicia: se o arquivo do banco ainda não existe,
// cria as tabelas e os dados iniciais. Se já existe, não mexe em nada.
@Component
public class InicializadorBanco implements ApplicationRunner {
    private final String url;

    public InicializadorBanco(@Value("${app.database.url}") String url) {
        this.url = url;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Path arquivoBanco = Path.of(url.replace("jdbc:sqlite:", ""));
        if (Files.exists(arquivoBanco)) {
            return;
        }

        try (Connection conexao = DriverManager.getConnection(url)) {
            executarScript(conexao, "database/schema.sql");
            executarScript(conexao, "database/dados-iniciais.sql");
        }
    }

    private void executarScript(Connection conexao, String caminho) throws Exception {
        String script = Files.readString(Path.of(caminho));

        for (String sql : script.split(";")) {
            if (!sql.isBlank()) {
                try (PreparedStatement comando = conexao.prepareStatement(sql)) {
                    comando.executeUpdate();
                }
            }
        }
    }
}
