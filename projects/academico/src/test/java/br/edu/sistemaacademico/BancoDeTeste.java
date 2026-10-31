package br.edu.sistemaacademico;

import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Cria um banco SQLite temporário com o schema e os dados iniciais do projeto,
 * para que os testes não usem nem alterem {@code database/sistema-academico.db}.
 */
public final class BancoDeTeste {

    private BancoDeTeste() {
    }

    public static String criar() throws Exception {
        var arquivo = Files.createTempFile("sistema-academico-teste-", ".db");
        arquivo.toFile().deleteOnExit();
        var url = "jdbc:sqlite:" + arquivo.toAbsolutePath();
        PrepararBanco.preparar(url, Path.of("database/schema.sql"), Path.of("database/dados-iniciais.sql"));
        return url;
    }
}
