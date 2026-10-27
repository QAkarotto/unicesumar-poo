package br.edu.sistemaacademico.api.infra;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;

@Component
public class ConexaoSqlite {

    private final String url;

    public ConexaoSqlite(@Value("${app.database.url}") String url) {
        this.url = url;
        prepararBancoSeNecessario();
    }

    public Connection abrir() throws SQLException {
        return DriverManager.getConnection(url);
    }


    private void prepararBancoSeNecessario() {
        Path arquivo = Path.of(url.replace("jdbc:sqlite:", ""));
        if (Files.exists(arquivo)) {
            return;
        }
        try (Connection conexao = abrir(); Statement st = conexao.createStatement()) {
            for (String comando : lerComandos("database/schema.sql")) {
                st.execute(comando);
            }
            for (String comando : lerComandos("database/dados-iniciais.sql")) {
                st.execute(comando);
            }
        } catch (SQLException | IOException e) {
            throw new IllegalStateException("Falha ao preparar o banco de dados", e);
        }
    }

    private String[] lerComandos(String caminho) throws IOException {
        String conteudo = Files.readString(Path.of(caminho));

        conteudo = conteudo.replaceAll("(?m)^\\s*--.*$", "");
        return Arrays.stream(conteudo.split(";"))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toArray(String[]::new);
    }
}
