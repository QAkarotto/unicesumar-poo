package br.edu.sistemaacademico.repository.jdbc;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Ao subir a aplicação, executa schema.sql e dados-iniciais.sql.
 * Os scripts são idempotentes (IF NOT EXISTS / INSERT OR IGNORE),
 * então os dados já gravados nunca são apagados.
 */
@Component
public class InicializadorBanco implements ApplicationRunner {

    private final ConexaoSqlite conexao;
    private final Path schema;
    private final Path dadosIniciais;

    public InicializadorBanco(ConexaoSqlite conexao,
                              @Value("${app.database.schema}") String schema,
                              @Value("${app.database.dados-iniciais}") String dadosIniciais) {
        this.conexao = conexao;
        this.schema = Path.of(schema);
        this.dadosIniciais = Path.of(dadosIniciais);
    }

    @Override
    public void run(ApplicationArguments args) {
        executar(schema);
        executar(dadosIniciais);
    }

    private void executar(Path script) {
        try (Connection c = conexao.abrir(); Statement st = c.createStatement()) {
            String sql = Files.readString(script);
            for (String comando : sql.split(";")) {
                if (!comando.isBlank()) {
                    st.execute(comando);
                }
            }
        } catch (SQLException | IOException e) {
            throw new IllegalStateException("Falha ao executar o script " + script, e);
        }
    }
}
