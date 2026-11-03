package br.edu.sistemaacademico.persistencia;

import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.DriverManager;
import java.util.List;

public final class BancoDeTeste {
    private BancoDeTeste() {
    }

    // mesmo efeito dos dois comandos sqlite3 do README, num arquivo dentro da pasta temporaria do teste
    public static String criar(Path pasta) throws Exception {
        var url = "jdbc:sqlite:" + pasta.resolve("teste.db");
        try (var con = DriverManager.getConnection(url); var comando = con.createStatement()) {
            for (var arquivo : List.of("database/schema.sql", "database/dados-iniciais.sql")) {
                for (var sql : Files.readString(Path.of(arquivo)).split(";")) {
                    if (!sql.isBlank()) {
                        comando.execute(sql);
                    }
                }
            }
        }
        return url;
    }
}
