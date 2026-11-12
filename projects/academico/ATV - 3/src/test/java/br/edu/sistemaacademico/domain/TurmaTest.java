package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TurmaTest {

    @Test
    void deveCriarTurmaComDadosValidos() {
        PeriodoLetivo periodo = new PeriodoLetivo(
                2026,
                Semestre.PRIMEIRO
        );

        Turma turma = new Turma(
                "T1",
                periodo
        );

        assertNotNull(turma);
        assertEquals("T1", turma.getCodigo());
        assertEquals(periodo, turma.getPeriodoLetivo());
        assertTrue(turma.getOfertas().isEmpty());
    }

    @Test
    void deveOfertarDisciplinaNaTurma() {
        PeriodoLetivo periodo = new PeriodoLetivo(
                2026,
                Semestre.PRIMEIRO
        );

        Turma turma = new Turma(
                "T1",
                periodo
        );

        Disciplina disciplina = new Disciplina(
                "POO",
                "Programacao Orientada a Objetos",
                80
        );

        OfertaDisciplina oferta = turma.ofertarDisciplina(
                disciplina
        );

        assertNotNull(oferta);
        assertEquals(turma, oferta.getTurma());
        assertEquals(disciplina, oferta.getDisciplina());
        assertEquals(1, turma.getOfertas().size());
        assertTrue(turma.getOfertas().contains(oferta));
    }

    @Test
    void devePermitirMultiplasDisciplinasNaTurma() {
        PeriodoLetivo periodo = new PeriodoLetivo(
                2026,
                Semestre.PRIMEIRO
        );

        Turma turma = new Turma(
                "T1",
                periodo
        );

        Disciplina disciplina1 = new Disciplina(
                "POO",
                "Programacao Orientada a Objetos",
                80
        );

        Disciplina disciplina2 = new Disciplina(
                "BD",
                "Banco de Dados",
                80
        );

        OfertaDisciplina oferta1 = turma.ofertarDisciplina(
                disciplina1
        );

        OfertaDisciplina oferta2 = turma.ofertarDisciplina(
                disciplina2
        );

        assertEquals(2, turma.getOfertas().size());
        assertTrue(turma.getOfertas().contains(oferta1));
        assertTrue(turma.getOfertas().contains(oferta2));
    }

    @Test
    void naoDeveOfertarMesmaDisciplinaDuasVezes() {
        PeriodoLetivo periodo = new PeriodoLetivo(
                2026,
                Semestre.PRIMEIRO
        );

        Turma turma = new Turma(
                "T1",
                periodo
        );

        Disciplina disciplina = new Disciplina(
                "POO",
                "Programacao Orientada a Objetos",
                80
        );

        turma.ofertarDisciplina(disciplina);

        assertThrows(
                IllegalStateException.class,
                () -> turma.ofertarDisciplina(disciplina)
        );
    }
}