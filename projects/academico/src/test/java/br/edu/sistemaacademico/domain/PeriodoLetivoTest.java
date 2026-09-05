package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PeriodoLetivoTest {

    @Test
    void deveCriarPeriodoLetivoValido() {
        var periodo = new PeriodoLetivo(2026, Semestre.SEGUNDO);

        assertEquals(2026, periodo.getAno());
        assertEquals(Semestre.SEGUNDO, periodo.getSemestre());
    }

    @Test
    void deveLancarExcecaoQuandoAnoNaoForPositivo() {
        assertThrows(IllegalArgumentException.class, () -> new PeriodoLetivo(0, Semestre.PRIMEIRO));
        assertThrows(IllegalArgumentException.class, () -> new PeriodoLetivo(-2026, Semestre.PRIMEIRO));
    }

    @Test
    void deveLancarExcecaoQuandoSemestreForNulo() {
        assertThrows(IllegalArgumentException.class, () -> new PeriodoLetivo(2026, null));
    }

    @Test
    void deveRepresentarToStringComNumeroDoSemestre() {
        var primeiro = new PeriodoLetivo(2026, Semestre.PRIMEIRO);
        var segundo = new PeriodoLetivo(2026, Semestre.SEGUNDO);

        assertEquals("2026/1", primeiro.toString());
        assertEquals("2026/2", segundo.toString());
    }
}