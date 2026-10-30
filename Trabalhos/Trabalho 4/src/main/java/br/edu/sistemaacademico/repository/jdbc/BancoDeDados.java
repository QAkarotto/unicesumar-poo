package br.edu.sistemaacademico.repository.jdbc;

import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Component
public class BancoDeDados {

    private final DataSource dataSource;

    public BancoDeDados(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Connection abrirConexao() throws SQLException {
        return dataSource.getConnection();
    }
}

