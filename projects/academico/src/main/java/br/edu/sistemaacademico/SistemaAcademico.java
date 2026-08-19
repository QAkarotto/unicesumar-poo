package br.edu.sistemaacademico;

import br.edu.sistemaacademico.domain.*;

public class SistemaAcademico {
    public static void main(String[] args) {
        System.out.println(" SISTEMA ACADÊMICO - TESTE ");

        // 1. Criando Aluno
        Aluno aluno = new Aluno("20261001", "Ketely", "ketely@email.com");
        System.out.println(aluno);

        // 2. Criando Disciplina
        Disciplina disciplina = new Disciplina("POO01", "Programação Orientada a Objetos", 80);
        System.out.println(disciplina);

        // 3. Criando Período Letivo
        PeriodoLetivo periodo = new PeriodoLetivo(2026, Semestre.PRIMEIRO);
        System.out.println("Período: " + periodo);

        // 4. Criando Turma
        Turma turma = new Turma("T01", disciplina, periodo);
        System.out.println(turma);

        // 5. Criando Matrícula
        Matricula matricula = new Matricula("M202601", aluno, turma);
        System.out.println(matricula);

        System.out.println("=================================");
        System.out.println("Todos os objetos foram criados com sucesso!");
    }
}