package br.edu.sistemaacademico;

import br.edu.sistemaacademico.repository.ConexaoBanco;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

@Component
public class InicializadorBanco implements CommandLineRunner {
    private final ConexaoBanco conexaoBanco;

    public InicializadorBanco(ConexaoBanco conexaoBanco) {
        this.conexaoBanco = conexaoBanco;
    }

    @Override
    public void run(String... args) {
        executarArquivo("database/schema.sql");
        executarArquivo("database/dados-iniciais.sql");
    }

    private void executarArquivo(String arquivo) {
        Path caminho = Path.of(arquivo);
        if (!Files.exists(caminho) && Files.exists(Path.of("projects/academico").resolve(arquivo))) {
            caminho = Path.of("projects/academico").resolve(arquivo);
        }
        if (!Files.exists(caminho)) {
            throw new IllegalStateException("Arquivo SQL não encontrado: " + caminho.toAbsolutePath());
        }
        try {
            String conteudo = Files.readString(caminho, StandardCharsets.UTF_8);
            // Remove comentários SQL de linha antes de separar os comandos por ponto e vírgula.
            StringBuilder semComentarios = new StringBuilder();
            for (String linha : conteudo.split("\\R")) {
                String limpa = linha.trim();
                if (!limpa.startsWith("--")) {
                    semComentarios.append(linha).append('\n');
                }
            }
            try (Connection conexao = conexaoBanco.obterConexao(); Statement comando = conexao.createStatement()) {
                for (String sql : semComentarios.toString().split(";")) {
                    String instrucao = sql.trim();
                    if (!instrucao.isEmpty()) {
                        comando.execute(instrucao);
                    }
                }
            }
        } catch (IOException | SQLException e) {
            throw new IllegalStateException("Falha ao executar o arquivo SQL: " + arquivo, e);
        }
    }
}
