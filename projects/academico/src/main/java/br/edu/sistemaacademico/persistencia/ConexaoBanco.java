package br.edu.sistemaacademico.persistencia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

// cada operacao abre a propria conexao e fecha no try-with-resources
@Component
public class ConexaoBanco {
    private final String url;

    public ConexaoBanco(@Value("${app.database.url}") String url) {
        this.url = url;
    }

    public Connection abrir() throws SQLException {
        return DriverManager.getConnection(url);
    }
}
