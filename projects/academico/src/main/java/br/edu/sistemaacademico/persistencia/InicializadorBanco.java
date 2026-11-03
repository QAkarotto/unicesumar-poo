package br.edu.sistemaacademico.persistencia;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

// Prepara um banco vazio com os scripts de database/ quando a API sobe, pra nao depender do sqlite3 instalado.
// O schema.sql comeca com DROP TABLE, entao so roda quando as tabelas ainda nao existem:
// rodar a cada inicializacao apagaria os dados cadastrados.
@Component
public class InicializadorBanco implements ApplicationRunner {
    private static final Logger LOG = LoggerFactory.getLogger(InicializadorBanco.class);
    private static final String CONTAR_TABELAS =
            "SELECT COUNT(*) FROM sqlite_master WHERE type = 'table' AND name IN ('aluno', 'disciplina')";
    private static final Set<String> CONTROLE_TRANSACAO = Set.of("BEGIN TRANSACTION", "COMMIT");

    private final ConexaoBanco conexao;
    private final Path schema;
    private final Path dadosIniciais;

    public InicializadorBanco(ConexaoBanco conexao,
                              @Value("${app.database.schema}") String schema,
                              @Value("${app.database.dados-iniciais}") String dadosIniciais) {
        this.conexao = conexao;
        this.schema = Path.of(schema);
        this.dadosIniciais = Path.of(dadosIniciais);
    }

    @Override
    public void run(ApplicationArguments args) throws SQLException, IOException {
        try (var con = conexao.abrir()) {
            int tabelas = contarTabelas(con);
            if (tabelas == 2) {
                return;
            }
            // com so uma tabela, o DROP do schema apagaria os dados da outra
            if (tabelas == 1) {
                throw new IllegalStateException("O banco tem só uma das tabelas aluno/disciplina. "
                        + "Recrie-o com database/schema.sql antes de iniciar a API.");
            }
            for (var script : new Path[]{schema, dadosIniciais}) {
                if (!Files.exists(script)) {
                    throw new IllegalStateException("Arquivo " + script.toAbsolutePath() + " não encontrado. "
                            + "Inicie a API a partir da pasta projects/academico.");
                }
            }
            LOG.info("Banco sem tabelas: executando {} e {}", schema, dadosIniciais);
            executarScripts(con, schema, dadosIniciais);
        }
    }

    private static int contarTabelas(Connection con) throws SQLException {
        try (var consulta = con.prepareStatement(CONTAR_TABELAS); var registros = consulta.executeQuery()) {
            registros.next();
            return registros.getInt(1);
        }
    }

    // os scripts trazem BEGIN/COMMIT pro sqlite3; aqui a transacao fica por conta da conexao
    private static void executarScripts(Connection con, Path... scripts) throws SQLException, IOException {
        con.setAutoCommit(false);
        try (var comando = con.createStatement()) {
            for (var script : scripts) {
                for (var sql : Files.readString(script).split(";")) {
                    var texto = sql.strip();
                    if (!texto.isEmpty() && !CONTROLE_TRANSACAO.contains(texto.toUpperCase())) {
                        comando.executeUpdate(texto);
                    }
                }
            }
            con.commit();
        } catch (SQLException | IOException erro) {
            con.rollback();
            throw erro;
        } finally {
            con.setAutoCommit(true);
        }
    }
}
