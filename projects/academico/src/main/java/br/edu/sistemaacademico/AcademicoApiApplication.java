package br.edu.sistemaacademico;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// @SpringBootApplication liga o Spring Boot e faz ele procurar os controllers
// dentro deste pacote e dos pacotes de baixo (api, domain).
@SpringBootApplication
public class AcademicoApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(AcademicoApiApplication.class, args);
    }
}
