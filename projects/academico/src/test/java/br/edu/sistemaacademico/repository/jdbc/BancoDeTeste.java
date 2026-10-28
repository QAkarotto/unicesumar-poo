package br.edu.sistemaacademico.repository.jdbc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.stream.Collectors;

final class BancoDeTeste {

    private static final Path SCHEMA = Path.of("database", "schema.sql");

    private BancoDeTeste() {
    }

    static ConexaoFactory criarComTabelas(Path arquivo) throws IOException, SQLException {
        ConexaoFactory conexoes = semTabelas(arquivo);
        executarSchema(conexoes);
        return conexoes;
    }

    static ConexaoFactory semTabelas(Path arquivo) {
        return new ConexaoFactory("jdbc:sqlite:" + arquivo.toAbsolutePath());
    }

    private static void executarSchema(ConexaoFactory conexoes) throws IOException, SQLException {
        String script = Files.readAllLines(SCHEMA).stream()
                .filter(linha -> !linha.stripLeading().startsWith("--"))
                .collect(Collectors.joining("\n"));

        try (Connection conexao = conexoes.abrir();
             Statement comando = conexao.createStatement()) {
            for (String sql : Arrays.asList(script.split(";"))) {
                if (!sql.isBlank()) {
                    comando.execute(sql);
                }
            }
        }
    }
}
