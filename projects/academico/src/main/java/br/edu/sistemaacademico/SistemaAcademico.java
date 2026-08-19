package br.edu.sistemaacademico;

import br.edu.sistemaacademico.domain.*;

public class SistemaAcademico {

    public static void main(String[] args) {

        System.out.println("=== SISTEMA ACADÊMICO ===\n");

        Aluno aluno1 = new Aluno("2024001", "Ana Lima", "ana.lima@email.com");
        Aluno aluno2 = new Aluno("2024002", "Bruno Souza", "bruno.souza@email.com");

        System.out.println("Alunos criados:");
        System.out.println("  " + aluno1);
        System.out.println("  " + aluno2);

        Disciplina disciplina = new Disciplina("POO001", "Programação Orientada a Objetos", 80);
        System.out.println("\nDisciplina criada:");
        System.out.println("  " + disciplina);

        PeriodoLetivo periodo = new PeriodoLetivo(2024, Semestre.PRIMEIRO);
        System.out.println("\nPeríodo letivo criado:");
        System.out.println("  " + periodo);

        Turma turma = new Turma("T01", disciplina, periodo);
        System.out.println("\nTurma criada:");
        System.out.println("  " + turma);

        Matricula matricula1 = new Matricula("MAT001", aluno1, turma);
        Matricula matricula2 = new Matricula("MAT002", aluno2, turma);
        System.out.println("\nMatrículas realizadas:");
        System.out.println("  " + matricula1);
        System.out.println("  " + matricula2);

        System.out.println("\n--- Atualização de e-mail ---");
        System.out.println("E-mail atual de Ana: " + aluno1.getEmail());
        aluno1.setEmail("ana.lima.novo@univ.edu.br");
        System.out.println("E-mail atualizado:   " + aluno1.getEmail());

        System.out.println("\n--- Tentativas inválidas (devem ser rejeitadas) ---");

        tentativa("Aluno sem nome", () ->
                new Aluno("2024003", "", "valido@email.com"));

        tentativa("Aluno com e-mail vazio", () ->
                new Aluno("2024004", "Carlos", ""));

        tentativa("Aluno com e-mail sem @", () ->
                new Aluno("2024005", "Diana", "emailsemarroba.com"));

        tentativa("Atualizar e-mail de Bruno com valor inválido", () ->
                aluno2.setEmail("nao-e-email"));

        System.out.println("E-mail de Bruno permanece: " + aluno2.getEmail());

        tentativa("Disciplina com carga horária zero", () ->
                new Disciplina("XXX", "Inválida", 0));

        tentativa("Disciplina com carga horária negativa", () ->
                new Disciplina("XXX", "Inválida", -10));

        tentativa("Período letivo com semestre nulo", () ->
                new PeriodoLetivo(2024, null));

        tentativa("Turma sem disciplina", () ->
                new Turma("T99", null, periodo));

        tentativa("Matrícula sem aluno", () ->
                new Matricula("MAT999", null, turma));

        tentativa("Matrícula sem código", () ->
                new Matricula("", aluno1, turma));

        System.out.println("\n=== FIM DO CENÁRIO ===");
    }

    private static void tentativa(String descricao, Runnable acao) {
        try {
            acao.run();
            System.out.println("  [FALHA] \"" + descricao + "\" deveria ter lançado exceção!");
        } catch (IllegalArgumentException e) {
            System.out.println("  [OK] " + descricao + " → " + e.getMessage());
        }
    }
}