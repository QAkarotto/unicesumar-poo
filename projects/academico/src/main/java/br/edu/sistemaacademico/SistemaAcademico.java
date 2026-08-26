package br.edu.sistemaacademico;

import br.edu.sistemaacademico.domain.*;

public class SistemaAcademico {

    public static void main(String[] args) {

        // ── Disciplinas ──────────────────────────────────────────────────────
        Disciplina poo   = new Disciplina("POO001", "Programação Orientada a Objetos", 80);
        Disciplina bd    = new Disciplina("BD001",  "Banco de Dados", 80);
        Disciplina so    = new Disciplina("SO001",  "Sistemas Operacionais", 60);
        Disciplina er    = new Disciplina("ER001",  "Engenharia de Requisitos", 60);

        // ── Turmas ───────────────────────────────────────────────────────────
        Turma turma2026_2 = new Turma("ESOFT4S-NA", "2026/2");
        Turma turma2027_1 = new Turma("ESOFT4S-NA", "2027/1");

        // ── Oferta de disciplinas ─────────────────────────────────────────────
        System.out.println("=== Ofertando disciplinas ===");
        OfertaDisciplina ofertaPoo  = turma2026_2.oferecer(poo);
        OfertaDisciplina ofertaBd   = turma2026_2.oferecer(bd);
        OfertaDisciplina ofertaSo   = turma2026_2.oferecer(so);
        OfertaDisciplina ofertaEr   = turma2026_2.oferecer(er);

        turma2026_2.getOfertas().forEach(o ->
                System.out.println("  - " + o.getDisciplina().getNome())
        );

        // Tentativa de oferta duplicada
        System.out.println("\n=== Tentativa de oferta duplicada ===");
        try {
            turma2026_2.oferecer(poo);
        } catch (IllegalArgumentException e) {
            System.out.println("Bloqueado: " + e.getMessage());
        }

        // ── Alunos ────────────────────────────────────────────────────────────
        Aluno goku  = new Aluno("RA001", "Goku");
        Aluno vegeta = new Aluno("RA002", "Vegeta");

        // ── Matrículas ────────────────────────────────────────────────────────
        System.out.println("\n=== Matrículas ===");
        Matricula mGokuPoo = ofertaPoo.matricular(goku);
        Matricula mGokuBd  = ofertaBd.matricular(goku);
        Matricula mVegetaPoo = ofertaPoo.matricular(vegeta);
        System.out.println("Matriculado: " + mGokuPoo);
        System.out.println("Matriculado: " + mGokuBd);
        System.out.println("Matriculado: " + mVegetaPoo);

        // Tentativa de matrícula duplicada na mesma oferta
        System.out.println("\n=== Tentativa de matrícula duplicada ===");
        try {
            ofertaPoo.matricular(goku);
        } catch (IllegalStateException e) {
            System.out.println("Bloqueado: " + e.getMessage());
        }

        // ── Resultados ────────────────────────────────────────────────────────
        System.out.println("\n=== Registrando resultados ===");
        mGokuPoo.registrarResultado(ResultadoAcademico.APROVADO);
        mGokuBd.registrarResultado(ResultadoAcademico.REPROVADO);
        mVegetaPoo.registrarResultado(ResultadoAcademico.REPROVADO);
        System.out.println("Resultado Goku em POO: " + mGokuPoo.getResultado());
        System.out.println("Resultado Goku em BD:  " + mGokuBd.getResultado());

        // Tentativa de alterar resultado já registrado
        System.out.println("\n=== Tentativa de alterar resultado já registrado ===");
        try {
            mGokuPoo.registrarResultado(ResultadoAcademico.REPROVADO);
        } catch (IllegalStateException e) {
            System.out.println("Bloqueado: " + e.getMessage());
        }

        // ── Nova turma 2027/1 ─────────────────────────────────────────────────
        System.out.println("\n=== 2027/1 — nova oferta das mesmas disciplinas ===");
        OfertaDisciplina ofertaPoo271  = turma2027_1.oferecer(poo);
        OfertaDisciplina ofertaBd271   = turma2027_1.oferecer(bd);

        // Goku foi APROVADO em POO — não pode se matricular novamente
        System.out.println("\n=== Tentativa de nova matrícula após aprovação ===");
        try {
            ofertaPoo271.matricular(goku);
        } catch (IllegalStateException e) {
            System.out.println("Bloqueado: " + e.getMessage());
        }

        // Goku foi REPROVADO em BD — pode cursar novamente
        System.out.println("\n=== Nova matrícula após reprovação (BD) ===");
        Matricula mGokuBd271 = ofertaBd271.matricular(goku);
        System.out.println("Matriculado: " + mGokuBd271);

        // Vegeta foi REPROVADO em POO — pode cursar novamente
        Matricula mVegetaPoo271 = ofertaPoo271.matricular(vegeta);
        System.out.println("Matriculado: " + mVegetaPoo271);

        // ── Histórico dos alunos ──────────────────────────────────────────────
        System.out.println("\n=== Histórico de Goku ===");
        goku.getHistorico().forEach(m -> System.out.println("  " + m));

        System.out.println("\n=== Histórico de Vegeta ===");
        vegeta.getHistorico().forEach(m -> System.out.println("  " + m));

        // ── Consulta de matrículas de uma oferta ──────────────────────────────
        System.out.println("\n=== Matrículas em POO (2026/2) ===");
        ofertaPoo.getMatriculas().forEach(m ->
                System.out.println("  " + m.getAluno().getNome() + " — " + m.getResultado())
        );
    }
}
