package org.example;

import org.example.PeriodoLetivo.Semestre;

public class SistemaAcademico {

    public static void main(String[] args) {
        System.out.println("--- INICIALIZANDO SISTEMA ACADÊMICO ---\n");


        try {
            Aluno aluno = new Aluno("RA123456", "Elisson Silva", "elisson@unicesumar.edu.br");
            
            
            Disciplina disciplina = new Disciplina("POO-01", "Programação Orientada a Objetos", 80);
            
            PeriodoLetivo periodo = new PeriodoLetivo(2026, Semestre.SEGUNDO);
            
            Turma turma = new Turma("TURMA-A", disciplina, periodo);
            
            Matricula matricula = new Matricula("MAT-2026-001", aluno, turma);

            System.out.println("Matricula criada com sucesso!");
            System.out.println(matricula);

            System.out.println("\n---------------------------------------");
            System.out.println(" Testando Proteção de Estado (Tentativa Inválida):");
           
            aluno.alterarDados("", "email@invalido.com");

        } catch (IllegalArgumentException e) {
            System.out.println(" Erro capturado com sucesso: " + e.getMessage());
            System.out.println("  O estado do objeto permaneceu protegido e consistente.");
            
        }
    }
}


