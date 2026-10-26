package br.edu.sistemaacademico.support;

import br.edu.sistemaacademico.repository.jdbc.ConexaoBanco;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;

public final class BancoTeste {

    private BancoTeste() {
    }

    public static ConexaoBanco preparar(Path arquivo) throws IOException, SQLException {
        var banco = new ConexaoBanco("jdbc:sqlite:" + arquivo.toAbsolutePath());
        executarScript(banco, Path.of("database/schema.sql"));
        executarScript(banco, Path.of("database/dados-iniciais.sql"));
        return banco;
    }

    private static void executarScript(ConexaoBanco banco, Path arquivo)
            throws IOException, SQLException {
        try (var conexao = banco.abrir()) {
            for (String sql : Files.readString(arquivo).split(";")) {
                if (!sql.isBlank()) {
                    try (var comando = conexao.prepareStatement(sql)) {
                        comando.execute();
                    }
                }
            }
        }
    }
}
