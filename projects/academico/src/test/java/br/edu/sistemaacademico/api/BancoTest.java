package br.edu.sistemaacademico.api;

import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.DriverManager;

class BancoTest {
    static final String URL = "jdbc:sqlite:target/teste-academico.db";

    static void recriar() throws Exception {
        try (var conexao = DriverManager.getConnection(URL);
             var comando = conexao.createStatement()) {
            for (var arquivo : new String[]{"database/schema.sql", "database/dados-iniciais.sql"}) {
                for (var sql : Files.readString(Path.of(arquivo)).split(";")) {
                    if (!sql.isBlank()) {
                        comando.executeUpdate(sql);
                    }
                }
            }
        }
    }
}
