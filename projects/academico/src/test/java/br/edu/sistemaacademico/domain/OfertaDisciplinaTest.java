package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OfertaDisciplinaTest {

    @Test
    void deveCriarOfertaDeDisciplina() {
        Disciplina disciplina = new Disciplina(
                "POO",
                "Programacao Orientada a Objetos",
                80
        );

        PeriodoLetivo periodo = new PeriodoLetivo(
                2026,
                Semestre.PRIMEIRO
        );

        Turma turma = new Turma(
                "T1",
                periodo
        );

        OfertaDisciplina oferta = new OfertaDisciplina(
                turma,
                disciplina
        );

        assertNotNull(oferta);
        assertEquals(turma, oferta.getTurma());
        assertEquals(disciplina, oferta.getDisciplina());
        assertTrue(oferta.getMatriculas().isEmpty());
    }

    @Test
    void deveMatricularAlunoNaOferta() {
        Aluno aluno = new Aluno(
                "RA001",
                "Igor",
                "igor@email.com"
        );

        Disciplina disciplina = new Disciplina(
                "POO",
                "Programacao Orientada a Objetos",
                80
        );

        PeriodoLetivo periodo = new PeriodoLetivo(
                2026,
                Semestre.PRIMEIRO
        );

        Turma turma = new Turma(
                "T1",
                periodo
        );

        OfertaDisciplina oferta = new OfertaDisciplina(
                turma,
                disciplina
        );

        Matricula matricula = oferta.matricular(aluno);

        assertNotNull(matricula);
        assertEquals(aluno, matricula.getAluno());
        assertEquals(oferta, matricula.getOfertaDisciplina());
        assertEquals(1, oferta.getMatriculas().size());
        assertTrue(oferta.getMatriculas().contains(matricula));
    }

    @Test
    void naoDevePermitirMatriculaDuplicada() {
        Aluno aluno = new Aluno(
                "RA001",
                "Igor",
                "igor@email.com"
        );

        Disciplina disciplina = new Disciplina(
                "POO",
                "Programacao Orientada a Objetos",
                80
        );

        PeriodoLetivo periodo = new PeriodoLetivo(
                2026,
                Semestre.PRIMEIRO
        );

        Turma turma = new Turma(
                "T1",
                periodo
        );

        OfertaDisciplina oferta = new OfertaDisciplina(
                turma,
                disciplina
        );

        oferta.matricular(aluno);

        assertThrows(
                IllegalStateException.class,
                () -> oferta.matricular(aluno)
        );
    }

    @Test
    void devePermitirNovaMatriculaAposReprovacao() {
        Aluno aluno = new Aluno(
                "RA001",
                "Igor",
                "igor@email.com"
        );

        Disciplina disciplina = new Disciplina(
                "POO",
                "Programacao Orientada a Objetos",
                80
        );

        PeriodoLetivo periodo1 = new PeriodoLetivo(
                2026,
                Semestre.PRIMEIRO
        );

        Turma turma1 = new Turma(
                "T1",
                periodo1
        );

        OfertaDisciplina oferta1 = new OfertaDisciplina(
                turma1,
                disciplina
        );

        Matricula primeiraMatricula = oferta1.matricular(aluno);

        primeiraMatricula.concluir(
                ResultadoAcademico.REPROVADO
        );

        PeriodoLetivo periodo2 = new PeriodoLetivo(
                2026,
                Semestre.SEGUNDO
        );

        Turma turma2 = new Turma(
                "T2",
                periodo2
        );

        OfertaDisciplina oferta2 = new OfertaDisciplina(
                turma2,
                disciplina
        );

        Matricula segundaMatricula = oferta2.matricular(aluno);

        assertNotNull(segundaMatricula);
        assertEquals(aluno, segundaMatricula.getAluno());
        assertEquals(oferta2, segundaMatricula.getOfertaDisciplina());
    }

    @Test
    void naoDevePermitirNovaMatriculaAposAprovacao() {
        Aluno aluno = new Aluno(
                "RA001",
                "Igor",
                "igor@email.com"
        );

        Disciplina disciplina = new Disciplina(
                "POO",
                "Programacao Orientada a Objetos",
                80
        );

        PeriodoLetivo periodo1 = new PeriodoLetivo(
                2026,
                Semestre.PRIMEIRO
        );

        Turma turma1 = new Turma(
                "T1",
                periodo1
        );

        OfertaDisciplina oferta1 = new OfertaDisciplina(
                turma1,
                disciplina
        );

        Matricula matricula = oferta1.matricular(aluno);

        matricula.concluir(
                ResultadoAcademico.APROVADO
        );

        PeriodoLetivo periodo2 = new PeriodoLetivo(
                2026,
                Semestre.SEGUNDO
        );

        Turma turma2 = new Turma(
                "T2",
                periodo2
        );

        OfertaDisciplina oferta2 = new OfertaDisciplina(
                turma2,
                disciplina
        );

        assertThrows(
                IllegalStateException.class,
                () -> oferta2.matricular(aluno)
        );
    }
}