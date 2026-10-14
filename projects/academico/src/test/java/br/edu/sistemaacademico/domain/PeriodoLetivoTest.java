package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PeriodoLetivoTest {

    @Test
    void deveCriarPeriodoLetivoValido() {
        PeriodoLetivo periodo = new PeriodoLetivo(2026, Semestre.PRIMEIRO);

        assertEquals(2026, periodo.getAno());
        assertEquals(Semestre.PRIMEIRO, periodo.getSemestre());
        assertEquals("2026/1", periodo.toString());
    }

    @Test
    void naoDeveAceitarAnoZeroOuNegativo() {
        assertThrows(IllegalArgumentException.class,
                () -> new PeriodoLetivo(0, Semestre.PRIMEIRO));

        assertThrows(IllegalArgumentException.class,
                () -> new PeriodoLetivo(-1, Semestre.SEGUNDO));
    }

    @Test
    void naoDeveAceitarSemestreNulo() {
        assertThrows(IllegalArgumentException.class,
                () -> new PeriodoLetivo(2026, null));
    }

    @Test
    void deveCompararPeriodosPelosValores() {
        PeriodoLetivo primeiro = new PeriodoLetivo(2026, Semestre.PRIMEIRO);
        PeriodoLetivo equivalente = new PeriodoLetivo(2026, Semestre.PRIMEIRO);
        PeriodoLetivo diferente = new PeriodoLetivo(2026, Semestre.SEGUNDO);

        assertEquals(primeiro, equivalente);
        assertEquals(primeiro.hashCode(), equivalente.hashCode());
        assertNotEquals(primeiro, diferente);
        assertNotEquals(primeiro, null);
        assertNotEquals(primeiro, "2026/1");
        assertEquals(primeiro, primeiro);
    }
}