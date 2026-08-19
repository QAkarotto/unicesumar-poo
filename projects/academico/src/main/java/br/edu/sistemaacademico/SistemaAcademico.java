package br.edu.sistemaacademico;

import br.edu.sistemaacademico.domain.*;

public class SistemaAcademico {
    public static void main(String[] args) {
        try {
            // Criando objetos válidos
            Aluno aluno1 = new Aluno("A001", "João Silva", "joao@email.com");
            Aluno aluno2 = new Aluno("A002", "Maria Santos", "maria@email.com");

            Disciplina disciplina1 = new Disciplina("D001", "Programação Java", 80);
            Disciplina disciplina2 = new Disciplina("D002", "Banco de Dados", 60);

            PeriodoLetivo periodo = new PeriodoLetivo(2026, Semestre.PRIMEIRO);

            Turma turma1 = new Turma("T001", disciplina1, periodo);
            Turma turma2 = new Turma("T002", disciplina2, periodo);

            Matricula matricula1 = new Matricula("M001", aluno1, turma1);
            Matricula matricula2 = new Matricula("M002", aluno2, turma1);
            Matricula matricula3 = new Matricula("M003", aluno1, turma2);

            // Apresentando os resultados
            System.out.println("=== SISTEMA ACADÊMICO ===");
            System.out.println("\n--- Alunos ---");
            System.out.println(aluno1);
            System.out.println(aluno2);

            System.out.println("\n--- Disciplinas ---");
            System.out.println(disciplina1);
            System.out.println(disciplina2);

            System.out.println("\n--- Período Letivo ---");
            System.out.println(periodo);

            System.out.println("\n--- Turmas ---");
            System.out.println(turma1);
            System.out.println(turma2);

            System.out.println("\n--- Matrículas ---");
            System.out.println(matricula1);
            System.out.println(matricula2);
            System.out.println(matricula3);

            // TESTANDO OS SETTERS (para eliminar o aviso "no usages")
            System.out.println("\n--- Testando Alterações com Setters ---");
            System.out.println("Antes: " + aluno1);
            aluno1.setNome("João Carlos Silva");
            aluno1.setEmail("joao.carlos@email.com");
            System.out.println("Depois: " + aluno1);

            System.out.println("\n--- Testando Validações ---");
            testarValidacoes();

        } catch (IllegalArgumentException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private static void testarValidacoes() {
        try {
            // Teste 1: Email inválido
            new Aluno("A003", "Teste", "email-invalido");
        } catch (IllegalArgumentException e) {
            System.out.println("✓ Validação de email funcionou: " + e.getMessage());
        }

        try {
            // Teste 2: Nome vazio
            new Aluno("A004", "", "teste@email.com");
        } catch (IllegalArgumentException e) {
            System.out.println("✓ Validação de nome funcionou: " + e.getMessage());
        }

        try {
            // Teste 3: Carga horária negativa
            new Disciplina("D003", "Teste", -10);
        } catch (IllegalArgumentException e) {
            System.out.println("✓ Validação de carga horária funcionou: " + e.getMessage());
        }

        try {
            // Teste 4: Turma nula
            new Matricula("M004", new Aluno("A005", "Teste", "teste@email.com"), null);
        } catch (IllegalArgumentException e) {
            System.out.println("✓ Validação de turma nula funcionou: " + e.getMessage());
        }

        try {
            // Teste 5: Alterar email para inválido
            Aluno aluno = new Aluno("A006", "Teste", "teste@email.com");
            aluno.setEmail("email-invalido");
        } catch (IllegalArgumentException e) {
            System.out.println("✓ Validação de alteração de email funcionou: " + e.getMessage());
        }
    }
}