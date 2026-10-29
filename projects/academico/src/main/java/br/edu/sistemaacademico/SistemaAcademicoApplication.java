
package br.edu.sistemaacademico;

import br.edu.sistemaacademico.repository.jdbc.ConexaoSQLite;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SistemaAcademicoApplication {

    public static void main(String[] args) {

        ConexaoSQLite.inicializarBanco();

        SpringApplication.run(
                SistemaAcademicoApplication.class,
                args
        );
    }
}
