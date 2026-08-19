package br.edu.sistemaacademico;

import br.edu.sistemaacademico.domain.Aluno;
import br.edu.sistemaacademico.domain.Disciplina;
import br.edu.sistemaacademico.domain.Matricula;
import br.edu.sistemaacademico.domain.PeriodoLetivo;
import br.edu.sistemaacademico.domain.Semestre;
import br.edu.sistemaacademico.domain.Turma;

public class SistemaAcademico {

    public static void main(String[] args) {
        var aluno = new Aluno(
                "RA6700067",
                "Manoel Gomes",
                "manoelgomes@gmail.com"
        );

        var disciplina = new Disciplina(
                "POO",
                "Programação Orientada a Objetos",
                80
        );

        var periodoLetivo = new PeriodoLetivo(2026, Semestre.SEGUNDO);
        var turma = new Turma("POO-NA", disciplina, periodoLetivo);
        var matricula = new Matricula("MAT-001", aluno, turma);

        System.out.println("=== Sistema Acadêmico ===");
        System.out.println("Aluno: " + aluno);
        System.out.println("Disciplina: " + disciplina);
        System.out.println("Período: " + periodoLetivo);
        System.out.println("Turma: " + turma);
        System.out.println("Matrícula: " + matricula);

        aluno.setEmail("manoelgomes@universidade.edu.br");
        System.out.println("E-mail atualizado: " + aluno.getEmail());

        System.out.println();
        System.out.println("=== Tentativas com dados inválidos ===");

        try {
            aluno.setEmail("email-invalido");
        } catch (IllegalArgumentException e) {
            System.out.println("Recusado: " + e.getMessage());
            System.out.println("E-mail continua: " + aluno.getEmail());
        }

        try {
            new Disciplina("ED", "Estrutura de Dados", 0);
        } catch (IllegalArgumentException e) {
            System.out.println("Recusado: " + e.getMessage());
        }

        try {
            new Turma("POO-NB", null, periodoLetivo);
        } catch (IllegalArgumentException e) {
            System.out.println("Recusado: " + e.getMessage());
        }
    }
}
