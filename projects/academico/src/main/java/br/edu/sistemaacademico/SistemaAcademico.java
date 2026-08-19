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
                "RA2026001",
                "Paola Oliveira",
                "paola.oliveira@email.com"
        );

        var disciplina = new Disciplina(
                "POO",
                "Programação Orientada a Objetos",
                80
        );

        var turma = new Turma("POO-NA", disciplina, periodoLetivo);
        var matricula = new Matricula("MAT-001", aluno, turma);

        System.out.println("=== Sistema Acadêmico ===");
        System.out.println("Aluno: " + aluno);
        System.out.println("Disciplina: " + disciplina);
        System.out.println("Período: " + periodoLetivo);
        System.out.println("Turma: " + turma);
        System.out.println("Matrícula: " + matricula);

        aluno.setEmail("paola.oliveira@universidade.edu.br");
        System.out.println("E-mail atualizado: " + aluno.getEmail());
    } // João Pedro Hulchak Kazmierzak RA: 25141620-2 e Hiuri Luciano dos Santos RA: 25208360-2
}
