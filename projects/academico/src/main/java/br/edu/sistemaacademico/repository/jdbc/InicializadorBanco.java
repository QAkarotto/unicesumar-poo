package br.edu.sistemaacademico.repository.jdbc;

import br.edu.sistemaacademico.repository.PersistenciaException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Prepara o banco na PRIMEIRA execução, para quem não tem o sqlite3 instalado.
 *
 * Se a tabela "aluno" ainda não existe, executa database/schema.sql e
 * database/dados-iniciais.sql via JDBC. Se ela já existe, não faz nada:
 * os dados cadastrados continuam lá depois de reiniciar a aplicação.
 *
 * Não é ORM: são os mesmos scripts SQL do projeto, executados como texto.
 */
@Component
public class InicializadorBanco implements ApplicationRunner {

    private static final String SQL_TABELA_EXISTE =
            "SELECT 1 FROM sqlite_master WHERE type = 'table' AND name = ?";

    private final ConexaoBanco conexaoBanco;
    private final Path pastaScripts;

    // @Autowired: como a classe tem dois construtores, indica qual o Spring deve usar
    @Autowired
    public InicializadorBanco(ConexaoBanco conexaoBanco) {
        this(conexaoBanco, Path.of("database"));
    }

    // Usado nos testes, para apontar outra pasta de scripts
    InicializadorBanco(ConexaoBanco conexaoBanco, Path pastaScripts) {
        this.conexaoBanco = conexaoBanco;
        this.pastaScripts = pastaScripts;
    }

    @Override
    public void run(ApplicationArguments args) {
        inicializarSeNecessario();
    }

    /**
     * @return true se criou as tabelas agora; false se o banco já estava pronto
     */
    public boolean inicializarSeNecessario() {
        try (Connection conexao = conexaoBanco.abrir()) {
            if (tabelaExiste(conexao, "aluno")) {
                return false;
            }
            executarScript(conexao, pastaScripts.resolve("schema.sql"));
            executarScript(conexao, pastaScripts.resolve("dados-iniciais.sql"));
            return true;
        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao preparar o banco de dados.", e);
        }
    }

    private boolean tabelaExiste(Connection conexao, String nome) throws SQLException {
        try (PreparedStatement consulta = conexao.prepareStatement(SQL_TABELA_EXISTE)) {
            consulta.setString(1, nome);
            try (ResultSet resultado = consulta.executeQuery()) {
                return resultado.next();
            }
        }
    }

    // Os scripts são simples: cada comando termina com ";"
    private void executarScript(Connection conexao, Path arquivo) throws SQLException {
        String conteudo = lerArquivo(arquivo);
        try (Statement comando = conexao.createStatement()) {
            for (String sql : conteudo.split(";")) {
                if (!sql.isBlank()) {
                    comando.execute(sql);
                }
            }
        }
    }

    private String lerArquivo(Path arquivo) {
        if (!Files.exists(arquivo)) {
            throw new IllegalStateException(
                    "Script não encontrado: " + arquivo.toAbsolutePath()
                            + ". Execute a aplicação a partir da pasta projects/academico.");
        }
        try {
            return Files.readString(arquivo, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
