package br.edu.sistemaacademico.repository.jdbc;

import br.edu.sistemaacademico.repository.RepositorioException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


@Component
public class BancoDeDados {

    private final String url;

    public BancoDeDados(@Value("${app.database.caminho}") String caminho,
                        @Value("${app.database.schema}") String schema,
                        @Value("${app.database.dados-iniciais}") String dadosIniciais) {
        this.url = "jdbc:sqlite:" + caminho;
        criarPastaDoBanco(caminho);
        executarScript(Path.of(schema));
        executarScript(Path.of(dadosIniciais));
    }

    public Connection abrirConexao() throws SQLException {
        return DriverManager.getConnection(url);
    }

    private void criarPastaDoBanco(String caminho) {
        try {
            Path pasta = Path.of(caminho).toAbsolutePath().getParent();
            if (pasta != null) {
                Files.createDirectories(pasta);
            }
        } catch (IOException e) {
            throw new RepositorioException("Não foi possível criar a pasta do banco.", e);
        }
    }

    private void executarScript(Path arquivo) {
        String conteudo;
        try {
            conteudo = Files.readString(arquivo);
        } catch (IOException e) {
            throw new RepositorioException("Não foi possível ler o script: " + arquivo, e);
        }

        StringBuilder semComentarios = new StringBuilder();
        for (String linha : conteudo.split("\\R")) {
            if (!linha.trim().startsWith("--")) {
                semComentarios.append(linha).append('\n');
            }
        }

        try (Connection conexao = abrirConexao();
             Statement comando = conexao.createStatement()) {
            for (String sql : semComentarios.toString().split(";")) {
                if (!sql.isBlank()) {
                    comando.execute(sql.trim());
                }
            }
        } catch (SQLException e) {
            throw new RepositorioException("Erro ao executar o script: " + arquivo, e);
        }
    }
}
