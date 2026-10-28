
package br.edu.sistemaacademico;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@Component
public class DatabaseInitializer {

    private final String url;

    public DatabaseInitializer(
            @Value("${app.database.url}") String url) {
        this.url = url;
    }
    @PostConstruct
    public void inicializar() {
        Path pastaBanco = Path.of("database");

        try {
            Files.createDirectories(pastaBanco);

            try (Connection conexao = DriverManager.getConnection(url);
                 Statement comando = conexao.createStatement()) {

                criarTabelasSeNecessario(comando);
                inserirDadosIniciaisSeNecessario(comando);
            }

        } catch (SQLException | IOException e) {
            throw new IllegalStateException(
                    "Não foi possível inicializar o banco SQLite.", e);
        }
    }

    private void criarTabelasSeNecessario(Statement comando)
            throws SQLException {

        comando.execute("""
                CREATE TABLE IF NOT EXISTS aluno (
                    registro_academico TEXT PRIMARY KEY NOT NULL,
                    nome TEXT NOT NULL,
                    email TEXT NOT NULL
                )
                """);

        comando.execute("""
                CREATE TABLE IF NOT EXISTS disciplina (
                    codigo TEXT PRIMARY KEY NOT NULL,
                    nome TEXT NOT NULL,
                    carga_horaria INTEGER NOT NULL
                        CHECK (carga_horaria > 0)
                )
                """);
    }

    private void inserirDadosIniciaisSeNecessario(Statement comando)
            throws SQLException, IOException {

        try (ResultSet resultado =
                     comando.executeQuery("SELECT COUNT(*) FROM aluno")) {

            if (resultado.next() && resultado.getInt(1) == 0) {
                executarScript(
                        comando,
                        Path.of("projects", "academico", "database", "dados-iniciais.sql").toString()
                );
            }
        }
    }



    private void executarScript(Statement comando, String arquivo)
            throws IOException, SQLException {

        String sql = Files.readString(Path.of(arquivo));

        sql = sql.replaceFirst(
                "(?i)BEGIN\\s+TRANSACTION\\s*;", ""
        );

        sql = sql.replaceFirst(
                "(?i)COMMIT\\s*;?\\s*$", ""
        );

        for (String instrucao : sql.split(";")) {
            if (!instrucao.isBlank()) {
                comando.execute(instrucao);
            }
        }
    }
}
