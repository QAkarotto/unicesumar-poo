package br.edu.sistemaacademico.domain;

public class SistemaAcademico {
     public static void main(String[] args) {
        System.out.println("--- INICIALIZANDO SISTEMA ACADÊMICO ---\n");

        
        
        try {
            Aluno aluno = new Aluno("RA24294346-2", "Vegeta", "paoladeandrade31@unicesumar.edu.br");
            
            Disciplina disciplina = new Disciplina("POO-01", "Programação Orientada a Objetos", 80);
            
            PeriodoLetivo periodo = new PeriodoLetivo(2026, PeriodoLetivo.Semestre.SEGUNDO);
            
            Turma turma = new Turma("TURMA-A", periodo.toString());

            OfertaDisciplina oferta = turma.oferecer(disciplina);
            
            Matricula matricula = oferta.matricular(aluno);

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


