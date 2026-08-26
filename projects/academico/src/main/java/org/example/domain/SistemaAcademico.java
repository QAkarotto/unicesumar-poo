package br.edu.sistemaacademico;

import br.edu.sistemaacademico.domain.Aluno;
import br.edu.sistemaacademico.domain.Disciplina;
import br.edu.sistemaacademico.domain.Matricula;
import br.edu.sistemaacademico.domain.OfertaDisciplina;
import br.edu.sistemaacademico.domain.PeriodoLetivo;
import br.edu.sistemaacademico.domain.ResultadoAcademico;
import br.edu.sistemaacademico.domain.Semestre;
import br.edu.sistemaacademico.domain.Turma;

public class SistemaAcademico {

    public static void main(String[] args) {

        System.out.println("=================================");
        System.out.println("     SISTEMA ACADÊMICO");
        System.out.println("=================================");


        // =================================
        // 1. CRIANDO DISCIPLINAS
        // =================================

        Disciplina poo = new Disciplina(
                "POO",
                "Programação Orientada a Objetos",
                80
        );

        Disciplina banco = new Disciplina(
                "BD",
                "Banco de Dados",
                80
        );

        Disciplina sistemas = new Disciplina(
                "SO",
                "Sistemas Operacionais",
                80
        );

        Disciplina requisitos = new Disciplina(
                "ER",
                "Engenharia de Requisitos",
                80
        );


        // =================================
        // 2. CRIANDO PERÍODO LETIVO
        // =================================

        PeriodoLetivo periodo2026 =
                new PeriodoLetivo(
                        2026,
                        Semestre.SEGUNDO
                );


        // =================================
        // 3. CRIANDO TURMA
        // =================================

        Turma turma = new Turma(
                "ESOFT4S-NA",
                periodo2026
        );


        // =================================
        // 4. OFERTANDO DISCIPLINAS
        // =================================

        OfertaDisciplina ofertaPoo =
                turma.ofertarDisciplina(poo);

        OfertaDisciplina ofertaBanco =
                turma.ofertarDisciplina(banco);

        OfertaDisciplina ofertaSistemas =
                turma.ofertarDisciplina(sistemas);

        OfertaDisciplina ofertaRequisitos =
                turma.ofertarDisciplina(requisitos);


        System.out.println("\n=== TURMA ===");
        System.out.println(turma);


        // =================================
        // 5. TENTANDO OFERTAR DISCIPLINA DUPLICADA
        // =================================

        System.out.println("\n=== TESTE: DISCIPLINA DUPLICADA ===");

        try {

            turma.ofertarDisciplina(poo);

        } catch (IllegalArgumentException e) {

            System.out.println(
                    "Erro esperado: " + e.getMessage()
            );
        }


        // =================================
        // 6. CRIANDO ALUNOS
        // =================================

        Aluno daniel = new Aluno(
                "A001",
                "Daniel",
                "daniel@email.com"
        );

        Aluno paola = new Aluno(
                "A002",
                "Paola Oliveira",
                "paola@email.com"
        );


        // =================================
        // 7. MATRICULANDO DANIEL EM POO
        // =================================

        System.out.println("\n=== MATRÍCULA DANIEL ===");

        Matricula matriculaDaniel =
                ofertaPoo.matricular(daniel);

        System.out.println(matriculaDaniel);


        // =================================
        // 8. TENTANDO MATRÍCULA DUPLICADA
        // =================================

        System.out.println("\n=== TESTE: MATRÍCULA DUPLICADA ===");

        try {

            ofertaPoo.matricular(daniel);

        } catch (IllegalStateException e) {

            System.out.println(
                    "Erro esperado: " + e.getMessage()
            );
        }


        // =================================
        // 9. REGISTRANDO REPROVAÇÃO
        // =================================

        System.out.println("\n=== RESULTADO: REPROVAÇÃO ===");

        matriculaDaniel.registrarResultado(
                ResultadoAcademico.REPROVADO
        );

        System.out.println(matriculaDaniel);


        // =================================
        // 10. NOVA TURMA / NOVO PERÍODO
        // =================================

        PeriodoLetivo periodo2027 =
                new PeriodoLetivo(
                        2027,
                        Semestre.PRIMEIRO
                );

        Turma novaTurma =
                new Turma(
                        "ESOFT5S-NA",
                        periodo2027
                );


        OfertaDisciplina novaOfertaPoo =
                novaTurma.ofertarDisciplina(poo);


        // =================================
        // 11. DANIEL PODE CURSAR NOVAMENTE
        // PORQUE FOI REPROVADO
        // =================================

        System.out.println(
                "\n=== NOVA MATRÍCULA APÓS REPROVAÇÃO ==="
        );

        Matricula novaMatricula =
                novaOfertaPoo.matricular(daniel);

        System.out.println(novaMatricula);


        // =================================
        // 12. DANIEL É APROVADO
        // =================================

        System.out.println("\n=== APROVANDO DANIEL ===");

        novaMatricula.registrarResultado(
                ResultadoAcademico.APROVADO
        );

        System.out.println(novaMatricula);


        // =================================
        // 13. TENTANDO NOVA MATRÍCULA
        // APÓS APROVAÇÃO
        // =================================

        System.out.println(
                "\n=== TESTE: MATRÍCULA APÓS APROVAÇÃO ==="
        );

        try {

            novaTurma.ofertarDisciplina(poo);

        } catch (IllegalArgumentException e) {

            // Essa turma já possui POO.
            System.out.println(
                    "Erro esperado: " + e.getMessage()
            );
        }


        // Criando outra turma para testar
        // a regra de aprovação.

        Turma terceiraTurma =
                new Turma(
                        "ESOFT6S-NA",
                        periodo2027
                );

        OfertaDisciplina terceiraOfertaPoo =
                terceiraTurma.ofertarDisciplina(poo);


        try {

            terceiraOfertaPoo.matricular(daniel);

        } catch (IllegalStateException e) {

            System.out.println(
                    "Erro esperado: " + e.getMessage()
            );
        }


        // =================================
        // 14. MATRÍCULA DA PAOLA
        // =================================

        System.out.println("\n=== MATRÍCULA DA PAOLA ===");

        Matricula matriculaPaola =
                ofertaPoo.matricular(paola);

        System.out.println(matriculaPaola);


        // =================================
        // 15. CONSULTANDO MATRÍCULAS DA OFERTA
        // =================================

        System.out.println(
                "\n=== MATRÍCULAS DA OFERTA DE POO ==="
        );

        for (Matricula matricula :
                ofertaPoo.getMatriculas()) {

            System.out.println(matricula);
        }


        // =================================
        // 16. HISTÓRICO DO DANIEL
        // =================================

        System.out.println(
                "\n=== HISTÓRICO DO DANIEL ==="
        );

        for (Matricula matricula :
                daniel.getHistorico()) {

            System.out.println(matricula);
        }


        // =================================
        // 17. HISTÓRICO DA PAOLA
        // =================================

        System.out.println(
                "\n=== HISTÓRICO DA PAOLA ==="
        );

        for (Matricula Matricula :
                paola.getHistorico()) {

            System.out.println(Matricula);
        }


        // =================================
        // FINAL
        // =================================

        System.out.println("\n=================================");
        System.out.println("     FIM DO SISTEMA");
        System.out.println("=================================");
    }
}