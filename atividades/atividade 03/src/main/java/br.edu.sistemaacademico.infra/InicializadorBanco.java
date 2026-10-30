package br.edu.sistemaacademico.infra;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Executa os scripts SQL do projeto (database/schema.sql e database/dados-iniciais.sql).
 * Os dois scripts são idempotentes (CREATE TABLE IF NOT EXISTS / INSERT OR IGNORE).
 */
public class InicializadorBanco {

    private final ConexaoSqlite conexao;

    public InicializadorBanco(ConexaoSqlite conexao) {
        this.conexao = conexao;
    }

    public void executar(Path... scripts) {
        for (var script : scripts) {
            executarScript(script);
        }
    }

    private void executarScript(Path script) {
        String conteudo;
        try {
            conteudo = Files.readString(script);
        } catch (IOException e) {
            throw new IllegalStateException(
                    "Script SQL não encontrado: " + script.toAbsolutePath()
                            + " (execute a aplicação a partir da raiz do projeto).", e);
        }

        try (var connection = conexao.abrir(); var statement = connection.createStatement()) {
            for (var comando : separarComandos(conteudo)) {
                statement.execute(comando);
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Erro ao executar o script " + script + ": " + e.getMessage(), e);
        }
    }

    /** Remove linhas de comentário (--) e separa os comandos por ';'. */
    static List<String> separarComandos(String conteudo) {
        var semComentarios = new StringBuilder();
        for (var linha : conteudo.split("\\R")) {
            if (!linha.trim().startsWith("--")) {
                semComentarios.append(linha).append('\n');
            }
        }
        var comandos = new ArrayList<String>();
        for (var parte : semComentarios.toString().split(";")) {
            if (!parte.isBlank()) {
                comandos.add(parte.trim());
            }
        }
        return comandos;
    }
}
