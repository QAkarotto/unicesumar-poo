package br.edu.sistemaacademico.domain;

import br.edu.sistemaacademico.domain.*;

public class SistemaAcademico {

    public static void main(String[] args) {

        System.out.println("========================================");
        System.out.println("       SISTEMA ACADÊMICO");
        System.out.println("========================================");

        // ========================================
        // 1. CRIAÇÃO DOS ALUNOS
        // ========================================

        Aluno joao = new Aluno(
                "RA001",
                "João Augusto",
                "joao@email.com"
        );

        Aluno maria = new Aluno(
                "RA002",
                "Maria Silva",
                "maria@email.com"
        );

        System.out.println("\n--- ALUNOS ---");
        System.out.println(joao);
        System.out.println(maria);


        // ========================================
        // 2. CRIAÇÃO DAS DISCIPLINAS
        // ========================================

        Disciplina poo = new Disciplina(
                "ESOFT01",
                "Programação Orientada a Objetos",
                80
        );

        Disciplina bancoDados = new Disciplina(
                "ESOFT02",
                "Banco de Dados",
                80
        );

        Disciplina sistemasOperacionais = new Disciplina(
                "ESOFT03",
                "Sistemas Operacionais",
                60
        );

        Disciplina engenhariaRequisitos = new Disciplina(
                "ESOFT04",
                "Engenharia de Requisitos",
                60
        );

        System.out.println("\n--- DISCIPLINAS ---");
        System.out.println(poo);
        System.out.println(bancoDados);
        System.out.println(sistemasOperacionais);
        System.out.println(engenhariaRequisitos);


        // ========================================
        // 3. PERÍODO LETIVO
        // ========================================

        PeriodoLetivo periodo2026_2 =
                new PeriodoLetivo(
                        2026,
                        Semestre.SEGUNDO
                );

        System.out.println("\n--- PERÍODO LETIVO ---");
        System.out.println(periodo2026_2);


        // ========================================
        // 4. CRIAÇÃO DA TURMA
        // ========================================

        Turma turma =
                new Turma(
                        "ESOFT4S-NA",
                        periodo2026_2
                );

        System.out.println("\n--- TURMA ---");
        System.out.println(turma);


        // ========================================
        // 5. OFERTA DE DISCIPLINAS
        // ========================================

        OfertaDisciplina ofertaPoo =
                turma.adicionarDisciplina(poo);

        OfertaDisciplina ofertaBanco =
                turma.adicionarDisciplina(bancoDados);

        OfertaDisciplina ofertaSistemas =
                turma.adicionarDisciplina(sistemasOperacionais);

        OfertaDisciplina ofertaRequisitos =
                turma.adicionarDisciplina(engenhariaRequisitos);

        System.out.println("\n--- TURMA COM DISCIPLINAS ---");
        System.out.println(turma);


        // ========================================
        // 6. TENTATIVA DE DISCIPLINA DUPLICADA
        // ========================================

        System.out.println("\n--- TESTE: DISCIPLINA DUPLICADA ---");

        try {

            turma.adicionarDisciplina(poo);

        } catch (IllegalArgumentException e) {

            System.out.println(
                    "Erro esperado: " + e.getMessage()
            );
        }


        // ========================================
        // 7. MATRÍCULAS
        // ========================================

        System.out.println("\n--- MATRÍCULAS ---");

        Matricula matriculaJoaoPoo =
                ofertaPoo.matricular(joao);

        Matricula matriculaMariaPoo =
                ofertaPoo.matricular(maria);

        Matricula matriculaJoaoBanco =
                ofertaBanco.matricular(joao);

        System.out.println(matriculaJoaoPoo);
        System.out.println(matriculaMariaPoo);
        System.out.println(matriculaJoaoBanco);


        // ========================================
        // 8. CONSULTA DAS MATRÍCULAS DA OFERTA
        // ========================================

        System.out.println(
                "\n--- MATRÍCULAS EM PROGRAMAÇÃO ORIENTADA A OBJETOS ---"
        );

        for (Matricula matricula :
                ofertaPoo.getMatriculas()) {

            System.out.println(matricula);
        }


        // ========================================
        // 9. TENTATIVA DE MATRÍCULA DUPLICADA
        // ========================================

        System.out.println(
                "\n--- TESTE: MATRÍCULA DUPLICADA ---"
        );

        try {

            ofertaPoo.matricular(joao);

        } catch (IllegalArgumentException e) {

            System.out.println(
                    "Erro esperado: " + e.getMessage()
            );
        }


        // ========================================
        // 10. REPROVAÇÃO
        // ========================================

        System.out.println(
                "\n--- REGISTRANDO REPROVAÇÃO ---"
        );

        matriculaJoaoPoo.registrarResultado(
                Resultado.REPROVADO
        );

        System.out.println(matriculaJoaoPoo);


        // ========================================
        // 11. NOVA MATRÍCULA APÓS REPROVAÇÃO
        // ========================================

        System.out.println(
                "\n--- NOVA MATRÍCULA APÓS REPROVAÇÃO ---"
        );

        PeriodoLetivo periodo2027_1 =
                new PeriodoLetivo(
                        2027,
                        Semestre.PRIMEIRO
                );

        Turma novaTurma =
                new Turma(
                        "ESOFT5S-NA",
                        periodo2027_1
                );

        OfertaDisciplina novaOfertaPoo =
                novaTurma.adicionarDisciplina(poo);

        Matricula novaMatricula =
                novaOfertaPoo.matricular(joao);

        System.out.println(novaMatricula);


        // ========================================
        // 12. APROVAÇÃO
        // ========================================

        System.out.println(
                "\n--- REGISTRANDO APROVAÇÃO ---"
        );

        novaMatricula.registrarResultado(
                Resultado.APROVADO
        );

        System.out.println(novaMatricula);


        // ========================================
        // 13. TENTATIVA DE NOVA MATRÍCULA
        //     APÓS APROVAÇÃO
        // ========================================

        System.out.println(
                "\n--- TESTE: MATRÍCULA APÓS APROVAÇÃO ---"
        );

        PeriodoLetivo periodo2027_2 =
                new PeriodoLetivo(
                        2027,
                        Semestre.SEGUNDO
                );

        Turma terceiraTurma =
                new Turma(
                        "ESOFT6S-NA",
                        periodo2027_2
                );

        OfertaDisciplina terceiraOfertaPoo =
                terceiraTurma.adicionarDisciplina(poo);

        try {

            terceiraOfertaPoo.matricular(joao);

        } catch (IllegalStateException e) {

            System.out.println(
                    "Erro esperado: " + e.getMessage()
            );
        }


        // ========================================
        // 14. HISTÓRICO DO ALUNO
        // ========================================

        System.out.println(
                "\n--- HISTÓRICO DO JOÃO ---"
        );

        for (Matricula matricula :
                joao.getHistorico()) {

            System.out.println(matricula);
        }


        // ========================================
        // 15. TESTE DE ALTERAÇÃO DE E-MAIL
        // ========================================

        System.out.println(
                "\n--- ALTERAÇÃO DE E-MAIL ---"
        );

        System.out.println(
                "E-mail antigo: " + joao.getEmail()
        );

        joao.alterarEmail(
                "joao.novo@email.com"
        );

        System.out.println(
                "E-mail novo: " + joao.getEmail()
        );


        // ========================================
        // 16. TESTE DE E-MAIL INVÁLIDO
        // ========================================

        System.out.println(
                "\n--- TESTE: E-MAIL INVÁLIDO ---"
        );

        try {

            joao.alterarEmail("email-invalido");

        } catch (IllegalArgumentException e) {

            System.out.println(
                    "Erro esperado: " + e.getMessage()
            );
        }


        // ========================================
        // FINAL
        // ========================================

        System.out.println("\n========================================");
        System.out.println("       TESTES FINALIZADOS");
        System.out.println("========================================");
    }
}