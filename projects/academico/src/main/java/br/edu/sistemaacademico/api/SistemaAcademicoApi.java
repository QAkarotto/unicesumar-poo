package br.edu.sistemaacademico.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Ponto de entrada da API REST.
 *
 * @SpringBootApplication liga a configuração automática (Tomcat embutido, conversão
 * para JSON etc.) e faz o Spring procurar @RestController e @Component neste pacote
 * e nos subpacotes.
 *
 * A classe SistemaAcademico (demonstração no console) continua existindo, intacta.
 */
@SpringBootApplication
public class SistemaAcademicoApi {

    public static void main(String[] args) {
        SpringApplication.run(SistemaAcademicoApi.class, args);
    }
}
