package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MatriculaTest {

    private final PeriodoLetivo periodo = new PeriodoLetivo(2026, Semestre.SEGUNDO);
    private final Turma turma = new Turma("ADSIS4S", periodo);
    private final Disciplina disciplina = new Disciplina("POO", "Programação Orientada a Objetos", 80);
    private final OfertaDisciplina oferta = turma.ofertarDisciplina(disciplina);
    private final Aluno aluno = new Aluno("RA001", "Paola", "paola@email.com");

    @Test
    void deveIniciarSemResultado() {
        var matricula = oferta.matricular(aluno);

        assertNull(matricula.getResultado());
    }

    @Test
    void deveRegistrarResultadoAprovado() {
        var matricula = oferta.matricular(aluno);

        matricula.concluir(ResultadoAcademico.APROVADO);

        assertEquals(ResultadoAcademico.APROVADO, matricula.getResultado());
    }

    @Test
    void deveRegistrarResultadoReprovado() {
        var matricula = oferta.matricular(aluno);

        matricula.concluir(ResultadoAcademico.REPROVADO);

        assertEquals(ResultadoAcademico.REPROVADO, matricula.getResultado());
    }

    @Test
    void deveLancarExcecaoAoConcluirComResultadoNulo() {
        var matricula = oferta.matricular(aluno);

        assertThrows(IllegalArgumentException.class, () -> matricula.concluir(null));
    }

    @Test
    void deveLancarExcecaoAoTentarConcluirDuasVezes() {
        var matricula = oferta.matricular(aluno);
        matricula.concluir(ResultadoAcademico.APROVADO);

        assertThrows(IllegalStateException.class, () -> matricula.concluir(ResultadoAcademico.REPROVADO));
    }

    @Test
    void toStringDeveIndicarEmAndamentoAntesDeConcluir() {
        var matricula = oferta.matricular(aluno);

        assertTrue(matricula.toString().contains("EM ANDAMENTO"));
    }

    @Test
    void toStringDeveIndicarResultadoAposConcluir() {
        var matricula = oferta.matricular(aluno);

        matricula.concluir(ResultadoAcademico.APROVADO);

        assertTrue(matricula.toString().contains("APROVADO"));
    }
}