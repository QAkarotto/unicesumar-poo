package br.edu.sistemaacademico.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class TurmaTest {

    @Test
    void deveOfertarDisciplina() {
        Turma t = new Turma("T1", new PeriodoLetivo(2026, Semestre.PRIMEIRO));
        Disciplina d = new Disciplina("POO", "Programação OO", 80);

        OfertaDisciplina o = t.ofertarDisciplina(d);

        assertEquals(1, t.getOfertas().size());
        assertTrue(t.getOfertas().contains(o));
    }

    @Test
    void naoDeveOfertarMesmaDisciplinaDuasVezes() {
        Turma t = new Turma("T1", new PeriodoLetivo(2026, Semestre.PRIMEIRO));
        Disciplina d = new Disciplina("POO", "Programação OO", 80);
        t.ofertarDisciplina(d);

        assertThrows(IllegalArgumentException.class, () -> t.ofertarDisciplina(d));
        assertEquals(1, t.getOfertas().size());
    }
}
