package br.edu.sistemaacademico.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Component
public class ConnectionFactory {

    private final String url;

    public ConnectionFactory(@Value("${app.database.url:jdbc:sqlite:database/sistema-academico.db}") String url) {
        this.url = url;
    }

    public Connection getConnection() throws SQLException {
        if (url != null && url.startsWith("jdbc:sqlite:")) {
            String path = url.substring("jdbc:sqlite:".length());
            File file = new File(path);
            File parent = file.getParentFile();
            if (parent != null && !parent.exists()) {
                parent.mkdirs();
            }
        }
        return DriverManager.getConnection(url);
    }

    public String getUrl() {
        return url;
    }
}
