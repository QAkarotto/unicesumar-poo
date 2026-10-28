package br.edu.sistemaacademico.api;

import br.edu.sistemaacademico.repository.jdbc.ConexaoSQLite;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.SQLException;

@Configuration
public class InicializadorBanco {

    @Bean
    CommandLineRunner inicializarBanco() {
        return args -> {
            try {
                ConexaoSQLite.criarTabelas();
                System.out.println("Banco SQLite inicializado com sucesso.");
            } catch (SQLException e) {
                throw new IllegalStateException(
                        "Nao foi possivel inicializar o banco SQLite.", e
                );
            }
        };
    }
}
