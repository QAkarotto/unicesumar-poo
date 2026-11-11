package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MatriculaTest {

    @Test
    void deveCriarMatriculaComDadosValidos() {
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

        Matricula matricula = new Matricula(
                aluno,
                oferta
        );

        assertNotNull(matricula);
        assertNotNull(matricula.getCodigo());
        assertEquals(aluno, matricula.getAluno());
        assertEquals(oferta, matricula.getOfertaDisciplina());
        assertNull(matricula.getResultado());
    }

    @Test
    void naoDeveCriarMatriculaSemAluno() {
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

        assertThrows(
                IllegalArgumentException.class,
                () -> new Matricula(null, oferta)
        );
    }

    @Test
    void naoDeveCriarMatriculaSemOfertaDisciplina() {
        Aluno aluno = new Aluno(
                "RA001",
                "Igor",
                "igor@email.com"
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> new Matricula(aluno, null)
        );
    }

    @Test
    void deveConcluirMatriculaComoAprovado() {
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

        Matricula matricula = new Matricula(
                aluno,
                oferta
        );

        matricula.concluir(ResultadoAcademico.APROVADO);

        assertEquals(
                ResultadoAcademico.APROVADO,
                matricula.getResultado()
        );
    }

    @Test
    void deveConcluirMatriculaComoReprovado() {
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

        Matricula matricula = new Matricula(
                aluno,
                oferta
        );

        matricula.concluir(ResultadoAcademico.REPROVADO);

        assertEquals(
                ResultadoAcademico.REPROVADO,
                matricula.getResultado()
        );
    }

    @Test
    void naoDeveConcluirMatriculaSemResultado() {
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

        Matricula matricula = new Matricula(
                aluno,
                oferta
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> matricula.concluir(null)
        );
    }

    @Test
    void naoDeveConcluirMatriculaDuasVezes() {
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

        Matricula matricula = new Matricula(
                aluno,
                oferta
        );

        matricula.concluir(ResultadoAcademico.APROVADO);

        assertThrows(
                IllegalStateException.class,
                () -> matricula.concluir(ResultadoAcademico.REPROVADO)
        );
    }
}