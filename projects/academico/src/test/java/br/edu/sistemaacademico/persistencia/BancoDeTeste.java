package br.edu.sistemaacademico.persistencia;

import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.List;

public final class BancoDeTeste {

    private BancoDeTeste() {
    }

    public static String criar(Path pasta) throws Exception {
        String url = "jdbc:sqlite:" + pasta.resolve("teste.db");

        try (Connection con = DriverManager.getConnection(url);
             Statement comando = con.createStatement()) {

            for (String arquivo : List.of("database/schema.sql", "database/dados-iniciais.sql")) {
                Path caminho = Path.of(arquivo);
                if (!Files.exists(caminho)) {
                    caminho = Path.of("projects/academico", arquivo);
                }
                String conteudo = Files.readString(caminho);
                for (String sql : conteudo.split(";")) {
                    if (!sql.isBlank()) {
                        comando.execute(sql.trim());
                    }
                }
            }
        }

        return url;
    }
}
