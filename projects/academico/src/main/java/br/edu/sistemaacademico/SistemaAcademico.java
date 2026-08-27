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

        var paola = new Aluno(
                "RA2026001",
                "Paola Oliveira",
                "paola.oliveira@email.com"
        );

        var bruno = new Aluno(
                "RA2026002",
                "Bruno Santos",
                "bruno.santos@email.com"
        );

        var poo = new Disciplina(
                "POO",
                "Programação Orientada a Objetos",
                80
        );

        var bancoDados = new Disciplina(
                "BD",
                "Banco de Dados",
                80
        );

        var periodo2025_2 = new PeriodoLetivo(
                2025,
                Semestre.SEGUNDO
        );

        var periodo2026_1 = new PeriodoLetivo(
                2026,
                Semestre.PRIMEIRO
        );

        var periodo2026_2 = new PeriodoLetivo(
                2026,
                Semestre.SEGUNDO
        );

        var turma2025 = new Turma(
                "ESOFT4S-NA",
                periodo2025_2
        );

        var turma2026A = new Turma(
                "ESOFT4S-NB",
                periodo2026_1
        );

        var turma2026B = new Turma(
                "ADSIS4S",
                periodo2026_2
        );

        System.out.println("=== OFERTA DE DISCIPLINAS ===");

        var poo2025 = turma2025.ofertarDisciplina(poo);

        var poo2026A = turma2026A.ofertarDisciplina(poo);
        var bancoDados2026A = turma2026A.ofertarDisciplina(bancoDados);

        var poo2026B = turma2026B.ofertarDisciplina(poo);

        System.out.println("Disciplinas da turma "
                + turma2026A.getCodigo() + ": "
                + turma2026A.getOfertas());

        validar(
                turma2026A.getOfertas().size() == 2,
                "A turma deve permitir várias disciplinas ofertadas."
        );

        System.out.println();
        System.out.println("=== DISCIPLINA DUPLICADA ===");

        esperarFalha(
                "A mesma disciplina não pode ser ofertada duas vezes na mesma turma.",
                () -> turma2026A.ofertarDisciplina(poo)
        );

        System.out.println();
        System.out.println("=== PRIMEIRA MATRÍCULA ===");

        var primeiraMatricula = poo2025.matricular(paola);

        primeiraMatricula.concluir(
                ResultadoAcademico.REPROVADO
        );

        System.out.println(
                "Paola concluiu POO em 2025/2 com resultado: "
                        + primeiraMatricula.getResultado()
        );

        System.out.println();
        System.out.println("=== NOVA MATRÍCULA APÓS REPROVAÇÃO ===");

        var segundaMatricula = poo2026A.matricular(paola);

        System.out.println(
                "Nova matrícula realizada: "
                        + segundaMatricula
        );

        validar(
                paola.getMatriculas().size() == 2,
                "O aluno deve manter seu histórico de matrículas."
        );

        segundaMatricula.concluir(
                ResultadoAcademico.APROVADO
        );

        System.out.println(
                "Paola concluiu novamente POO com resultado: "
                        + segundaMatricula.getResultado()
        );

        System.out.println();
        System.out.println("=== MATRÍCULA APÓS APROVAÇÃO ===");

        esperarFalha(
                "Aluno aprovado não pode cursar novamente a mesma disciplina.",
                () -> poo2026B.matricular(paola)
        );

        System.out.println();
        System.out.println("=== MATRÍCULA DUPLICADA ===");

        var matriculaBruno = poo2026A.matricular(bruno);

        esperarFalha(
                "Aluno não pode possuir duas matrículas na mesma oferta.",
                () -> poo2026A.matricular(bruno)
        );

        System.out.println();
        System.out.println("=== CONSULTAS ===");

        System.out.println(
                "Matrículas de Paola: "
                        + paola.getMatriculas()
        );

        System.out.println(
                "Matrículas de POO - ESOFT4S-NB: "
                        + poo2026A.getMatriculas()
        );

        System.out.println(
                "Matrículas de Banco de Dados - ESOFT4S-NB: "
                        + bancoDados2026A.getMatriculas()
        );

        System.out.println();
        System.out.println("=== VALIDAÇÃO CONCLUÍDA ===");
    }

    private static void validar(
            boolean condicao,
            String mensagem
    ) {
        if (!condicao) {
            throw new AssertionError(mensagem);
        }

        System.out.println("[OK] " + mensagem);
    }

    private static void esperarFalha(
            String mensagem,
            Runnable operacao
    ) {
        try {
            operacao.run();
            throw new AssertionError(
                    "A operação deveria ter sido impedida: " + mensagem
            );
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println(
                    "[OK] " + mensagem
            );
            System.out.println(
                    "     Motivo: " + e.getMessage()
            );
        }
    }
}