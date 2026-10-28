package br.edu.sistemaacademico;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * Alternativa ao {@code sqlite3} para quem não tem o executável instalado:
 * executa {@code database/schema.sql} e {@code database/dados-iniciais.sql}
 * no banco configurado em {@code app.database.url}.
 * <p>
 * Execute pela IDE com o diretório de trabalho em {@code projects/academico}.
 * ATENÇÃO: o schema apaga e recria as tabelas.
 * <p>
 * A API não chama esta classe: iniciar a aplicação não cria nem carrega o banco.
 */
public final class PrepararBanco {

    private PrepararBanco() {
    }

    public static void main(String[] args) throws Exception {
        var url = urlConfigurada();
        preparar(url, Path.of("database/schema.sql"), Path.of("database/dados-iniciais.sql"));
        System.out.println("Banco preparado em " + url);
    }

    /** Executa os scripts informados, em ordem, no banco da URL. */
    public static void preparar(String url, Path... scripts) throws SQLException, IOException {
        try (Connection conexao = DriverManager.getConnection(url)) {
            conexao.setAutoCommit(false);
            try (Statement comando = conexao.createStatement()) {
                for (Path script : scripts) {
                    for (String sql : comandosDo(script)) {
                        comando.executeUpdate(sql);
                    }
                }
                conexao.commit();
            } catch (SQLException erro) {
                conexao.rollback();
                throw erro;
            }
        }
    }

    /**
     * Divide o script em comandos por ";". A transação é controlada pelo JDBC,
     * então BEGIN/COMMIT do arquivo são ignorados. Suficiente para os scripts
     * do projeto, que não têm ";" dentro de textos.
     */
    private static String[] comandosDo(Path script) throws IOException {
        var semComentarios = Files.readAllLines(script).stream()
                .filter(linha -> !linha.strip().startsWith("--"))
                .reduce("", (a, b) -> a + b + "\n");
        return java.util.Arrays.stream(semComentarios.split(";"))
                .map(String::strip)
                .filter(sql -> !sql.isEmpty())
                .filter(sql -> !sql.equalsIgnoreCase("BEGIN TRANSACTION")
                        && !sql.equalsIgnoreCase("BEGIN")
                        && !sql.equalsIgnoreCase("COMMIT"))
                .toArray(String[]::new);
    }

    private static String urlConfigurada() throws IOException {
        var propriedades = new Properties();
        try (InputStream entrada = PrepararBanco.class.getResourceAsStream("/application.properties")) {
            if (entrada != null) {
                propriedades.load(entrada);
            }
        }
        return propriedades.getProperty("app.database.url", "jdbc:sqlite:database/sistema-academico.db");
    }
}
