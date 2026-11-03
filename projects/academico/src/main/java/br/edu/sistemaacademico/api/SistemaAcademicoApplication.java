package br.edu.sistemaacademico.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// scanBasePackages: faz o Spring encontrar também as classes de br.edu.sistemaacademico.repository
@SpringBootApplication(scanBasePackages = "br.edu.sistemaacademico")
public class SistemaAcademicoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SistemaAcademicoApplication.class, args);
    }
}