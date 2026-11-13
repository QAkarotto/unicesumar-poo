package br.edu.sistemaacademico.repository;

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
public class ConexaoBanco {

    private final String arquivo;

    public ConexaoBanco(@Value("${app.banco:database/academico.db}") String arquivo) {
        this.arquivo = arquivo;
        preparar();
    }

    public Connection abrir() throws SQLException {
        return DriverManager.getConnection("jdbc:sqlite:" + arquivo);
    }

    private void preparar() {
        Path caminho = Path.of(arquivo).toAbsolutePath();
        Path pasta = caminho.getParent();
        if (pasta != null) {
            try {
                Files.createDirectories(pasta);
            } catch (IOException e) {
                throw new RuntimeException("Não foi possível criar a pasta do banco", e);
            }
        }
        executarArquivo("database/schema.sql");
        executarArquivo("database/dados-iniciais.sql");
    }

    private void executarArquivo(String caminho) {
        try {
            String sql = Files.readString(Path.of(caminho));
            try (Connection conexao = abrir();
                 Statement comando = conexao.createStatement()) {
                for (String parte : sql.split(";")) {
                    String trecho = parte.trim();
                    if (!trecho.isEmpty()) {
                        comando.execute(trecho);
                    }
                }
            }
        } catch (IOException | SQLException e) {
            throw new RuntimeException("Não foi possível preparar o banco", e);
        }
    }
}
