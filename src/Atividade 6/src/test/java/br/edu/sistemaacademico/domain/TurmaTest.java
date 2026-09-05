package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TurmaTest {

    @Test
    void deveOfertarDisciplinaNaTurma() {
        Turma turma = new Turma("T1", new PeriodoLetivo(2026, Semestre.PRIMEIRO));
        Disciplina disciplina = new Disciplina("POO01", "Programação Orientada a Objetos", 80);

        OfertaDisciplina oferta = turma.ofertarDisciplina(disciplina);

        assertEquals(1, turma.getOfertas().size());
        assertEquals(disciplina, oferta.getDisciplina());
        assertEquals(turma, oferta.getTurma());
    }

    @Test
    void naoDeveOfertarMesmaDisciplinaDuasVezesNaTurma() {
        Turma turma = new Turma("T1", new PeriodoLetivo(2026, Semestre.PRIMEIRO));
        Disciplina disciplina = new Disciplina("POO01", "Programação Orientada a Objetos", 80);
        turma.ofertarDisciplina(disciplina);

        assertThrows(IllegalArgumentException.class, () -> turma.ofertarDisciplina(disciplina));
    }

    @Test
    void naoDeveCriarTurmaComCodigoVazio() {
        PeriodoLetivo periodo = new PeriodoLetivo(2026, Semestre.SEGUNDO);

        assertThrows(IllegalArgumentException.class, () -> new Turma(" ", periodo));
    }

    @Test
    void naoDeveCriarTurmaComPeriodoNulo() {
        assertThrows(IllegalArgumentException.class, () -> new Turma("T2", null));
    }
}
