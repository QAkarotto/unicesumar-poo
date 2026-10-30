package br.edu.sistemaacademico.repository.jdbc;

import br.edu.sistemaacademico.repository.RepositorioException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Abre conexões JDBC com o SQLite e prepara o banco na inicialização:
 * executa database/schema.sql e database/dados-iniciais.sql (ambos idempotentes).
 */
@Component
public class BancoDeDados {

    private static final String PREFIXO_URL = "jdbc:sqlite:";

    private final String url;

    public BancoDeDados(
            @Value("${academico.banco.url}") String url,
            @Value("${academico.banco.schema}") String schema,
            @Value("${academico.banco.dados-iniciais}") String dadosIniciais) {
        this.url = url;
        criarDiretorioDoArquivo(url);
        executarScript(Path.of(schema));
        executarScript(Path.of(dadosIniciais));
    }

    public Connection abrirConexao() throws SQLException {
        return DriverManager.getConnection(url);
    }

    private void executarScript(Path arquivo) {
        String conteudo;
        try {
            conteudo = Files.readString(arquivo, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RepositorioException("Não foi possível ler o script SQL: " + arquivo, e);
        }

        try (Connection conexao = abrirConexao(); Statement comando = conexao.createStatement()) {
            for (String instrucao : separarInstrucoes(conteudo)) {
                comando.execute(instrucao);
            }
        } catch (SQLException e) {
            throw new RepositorioException("Erro ao executar o script SQL: " + arquivo, e);
        }
    }

    /** Remove comentários de linha (--) e separa as instruções por ponto e vírgula. */
    private static String[] separarInstrucoes(String conteudo) {
        var semComentarios = new StringBuilder();
        for (String linha : conteudo.split("\\R")) {
            if (!linha.stripLeading().startsWith("--")) {
                semComentarios.append(linha).append('\n');
            }
        }
        return java.util.Arrays.stream(semComentarios.toString().split(";"))
                .map(String::strip)
                .filter(instrucao -> !instrucao.isEmpty())
                .toArray(String[]::new);
    }

    private static void criarDiretorioDoArquivo(String url) {
        if (!url.startsWith(PREFIXO_URL)) {
            return;
        }
        Path pasta = Path.of(url.substring(PREFIXO_URL.length())).toAbsolutePath().getParent();
        try {
            if (pasta != null) {
                Files.createDirectories(pasta);
            }
        } catch (IOException e) {
            throw new RepositorioException("Não foi possível criar a pasta do banco: " + pasta, e);
        }
    }
}
