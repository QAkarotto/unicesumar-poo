package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TurmaTest {

    private final PeriodoLetivo periodo = new PeriodoLetivo(2026, Semestre.SEGUNDO);

    @Test
    void deveCriarTurmaValida() {
        var turma = new Turma("ADSIS4S", periodo);

        assertEquals("ADSIS4S", turma.getCodigo());
        assertEquals(periodo, turma.getPeriodoLetivo());
        assertTrue(turma.getOfertas().isEmpty());
    }

    @Test
    void deveLancarExcecaoQuandoCodigoForNuloOuVazio() {
        assertThrows(IllegalArgumentException.class, () -> new Turma(null, periodo));
        assertThrows(IllegalArgumentException.class, () -> new Turma(" ", periodo));
    }

    @Test
    void deveLancarExcecaoQuandoPeriodoLetivoForNulo() {
        assertThrows(IllegalArgumentException.class, () -> new Turma("ADSIS4S", null));
    }

    @Test
    void deveOfertarDisciplina() {
        var turma = new Turma("ADSIS4S", periodo);
        var disciplina = new Disciplina("POO", "Programação Orientada a Objetos", 80);

        var oferta = turma.ofertarDisciplina(disciplina);

        assertEquals(1, turma.getOfertas().size());
        assertEquals(disciplina, oferta.getDisciplina());
        assertEquals(turma, oferta.getTurma());
    }

    @Test
    void deveImpedirOfertaDuplicadaDaMesmaDisciplina() {
        var turma = new Turma("ADSIS4S", periodo);
        var disciplina = new Disciplina("POO", "Programação Orientada a Objetos", 80);
        turma.ofertarDisciplina(disciplina);

        assertThrows(IllegalArgumentException.class, () -> turma.ofertarDisciplina(disciplina));
    }

    @Test
    void deveLancarExcecaoQuandoOfertarDisciplinaNula() {
        var turma = new Turma("ADSIS4S", periodo);

        assertThrows(IllegalArgumentException.class, () -> turma.ofertarDisciplina(null));
    }

    @Test
    void toStringDeveConterCodigoEPeriodoLetivo() {
        var turma = new Turma("ADSIS4S", periodo);

        var texto = turma.toString();

        assertTrue(texto.contains("ADSIS4S"));
        assertTrue(texto.contains(periodo.toString()));
    }
}