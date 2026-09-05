package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PeriodoLetivoTest {

    @Test
    void deveCriarPeriodoLetivoValido() {
        PeriodoLetivo periodo = new PeriodoLetivo(2026, Semestre.PRIMEIRO);

        assertEquals(2026, periodo.getAno());
        assertEquals(Semestre.PRIMEIRO, periodo.getSemestre());
    }

    @Test
    void naoDeveCriarPeriodoComAnoInvalido() {
        assertThrows(IllegalArgumentException.class, () -> new PeriodoLetivo(0, Semestre.PRIMEIRO));
    }

    @Test
    void naoDeveCriarPeriodoComSemestreNulo() {
        assertThrows(IllegalArgumentException.class, () -> new PeriodoLetivo(2026, null));
    }
}
