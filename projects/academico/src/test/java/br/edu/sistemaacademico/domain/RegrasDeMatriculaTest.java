package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RegrasDeMatriculaTest {

    private final Disciplina poo = new Disciplina("POO", "Programação Orientada a Objetos", 80);
    private final Aluno aluno = new Aluno("RA001", "Paola", "paola@email.com");

    @Test
    void devePermitirNovaMatriculaNaMesmaDisciplinaAposReprovacao() {
        var turmaA = new Turma("TURMA-A", new PeriodoLetivo(2025, Semestre.SEGUNDO));
        var ofertaA = turmaA.ofertarDisciplina(poo);
        var primeiraMatricula = ofertaA.matricular(aluno);
        primeiraMatricula.concluir(ResultadoAcademico.REPROVADO);

        var turmaB = new Turma("TURMA-B", new PeriodoLetivo(2026, Semestre.PRIMEIRO));
        var ofertaB = turmaB.ofertarDisciplina(poo);

        var segundaMatricula = ofertaB.matricular(aluno);

        assertEquals(aluno, segundaMatricula.getAluno());
        assertEquals(2, aluno.getMatriculas().size());
    }

    @Test
    void deveImpedirNovaMatriculaNaMesmaDisciplinaAposAprovacaoMesmoEmOutraTurma() {
        var turmaA = new Turma("TURMA-A", new PeriodoLetivo(2025, Semestre.SEGUNDO));
        var ofertaA = turmaA.ofertarDisciplina(poo);
        var matricula = ofertaA.matricular(aluno);
        matricula.concluir(ResultadoAcademico.APROVADO);

        var turmaB = new Turma("TURMA-B", new PeriodoLetivo(2026, Semestre.PRIMEIRO));
        var ofertaB = turmaB.ofertarDisciplina(poo);

        assertThrows(IllegalStateException.class, () -> ofertaB.matricular(aluno));
    }

    @Test
    void devePermitirMatriculasEmDisciplinasDiferentesParaOMesmoAluno() {
        var turma = new Turma("TURMA-A", new PeriodoLetivo(2026, Semestre.PRIMEIRO));
        var bancoDados = new Disciplina("BD", "Banco de Dados", 80);
        var ofertaPoo = turma.ofertarDisciplina(poo);
        var ofertaBd = turma.ofertarDisciplina(bancoDados);

        ofertaPoo.matricular(aluno);
        ofertaBd.matricular(aluno);

        assertEquals(2, aluno.getMatriculas().size());
    }
}