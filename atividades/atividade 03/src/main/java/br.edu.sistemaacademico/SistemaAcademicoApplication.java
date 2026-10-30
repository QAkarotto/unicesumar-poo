package br.edu.sistemaacademico;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Ponto de entrada da API. @SpringBootApplication liga a configuração automática
 * e faz o component scan do pacote br.edu.sistemaacademico e subpacotes.
 */
@SpringBootApplication
public class SistemaAcademicoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SistemaAcademicoApplication.class, args);
    }
}
